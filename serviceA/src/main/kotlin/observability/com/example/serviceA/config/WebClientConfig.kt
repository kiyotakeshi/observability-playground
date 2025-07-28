package observability.com.example.serviceA.config

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.RestTemplate
import java.time.Duration

@Configuration
class RestClientConfig {

    private val logger = LoggerFactory.getLogger(RestClientConfig::class.java)

    @Bean("serviceBRestTemplate")
    fun serviceBRestTemplate(): RestTemplate {
        val factory = SimpleClientHttpRequestFactory().apply {
            setConnectTimeout(5000)
            setReadTimeout(5000)
        }
        val restTemplate = RestTemplate(factory)
        restTemplate.interceptors.add(createLoggingInterceptor("serviceB"))
        return restTemplate
    }

    @Bean("serviceCRestTemplate")
    fun serviceCRestTemplate(): RestTemplate {
        val factory = SimpleClientHttpRequestFactory().apply {
            setConnectTimeout(10000)
            setReadTimeout(10000)
        }
        val restTemplate = RestTemplate(factory)
        restTemplate.interceptors.add(createLoggingInterceptor("serviceC"))
        return restTemplate
    }

    private fun createLoggingInterceptor(serviceName: String): ClientHttpRequestInterceptor {
        return ClientHttpRequestInterceptor { request, body, execution ->
            logRequestHeaders(serviceName, request)
            val response = execution.execute(request, body)
            logResponseHeaders(serviceName, response)
            response
        }
    }

    private fun logRequestHeaders(serviceName: String, request: HttpRequest) {
        logger.info("[$serviceName] HTTP Request Headers:")
        request.headers.forEach { (name, values) ->
            logger.info("[$serviceName] Request Header: $name = ${values.joinToString(", ")}")
            
            // トレーシング関連のヘッダを特別にログ出力
            // java-agent が traceparent header を付与するのはこのタイミングよりも後だった
            if (name.lowercase() in listOf("traceparent", "tracestate", "b3", "x-trace-id", "x-span-id")) {
                logger.info("[$serviceName] 🔍 TRACING HEADER DETECTED: $name = ${values.joinToString(", ")}")
            }
        }
    }

    private fun logResponseHeaders(serviceName: String, response: ClientHttpResponse) {
        logger.info("[$serviceName] HTTP Response Headers:")
        response.headers.forEach { (name, values) ->
            logger.info("[$serviceName] Response Header: $name = ${values.joinToString(", ")}")
        }
    }
}