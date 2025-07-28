package observability.com.example.serviceA.config

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.http.HttpRequest
import com.apollographql.apollo3.api.http.HttpResponse
import com.apollographql.apollo3.network.http.HttpInterceptor
import com.apollographql.apollo3.network.http.HttpInterceptorChain
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GraphQLClientConfig {

    private val logger = LoggerFactory.getLogger(GraphQLClientConfig::class.java)

    @Value("\${servicec.graphql.url:http://localhost:8082/graphql}")
    private lateinit var serviceCGraphQLUrl: String

    @Bean
    fun apolloClient(): ApolloClient {
        logger.info("[serviceC] GraphQL クライアントを初期化中: URL = $serviceCGraphQLUrl")
        return ApolloClient.Builder()
            .serverUrl(serviceCGraphQLUrl)
            .addHttpHeader("User-Agent", "serviceA-client/1.0")
            .addHttpInterceptor(GraphQLTracingInterceptor())
            .build()
    }

    inner class GraphQLTracingInterceptor : HttpInterceptor {
        override suspend fun intercept(request: HttpRequest, chain: HttpInterceptorChain): HttpResponse {
            logger.info("[serviceC] GraphQL Request Headers:")
            request.headers.forEach { (name, value) ->
                logger.info("[serviceC] Request Header: $name = $value")
                
                // トレーシング関連のヘッダを特別にログ出力
                // java-agent が traceparent header を付与するのはこのタイミングよりも後だった
                if (name.lowercase() in listOf("traceparent", "tracestate", "b3", "x-trace-id", "x-span-id")) {
                    logger.info("[serviceC] 🔍 TRACING HEADER DETECTED: $name = $value")
                }
            }
            
            val response = chain.proceed(request)
            
            logger.info("[serviceC] GraphQL Response Headers:")
            response.headers.forEach { (name, value) ->
                logger.info("[serviceC] Response Header: $name = $value")
            }
            
            return response
        }
    }
}