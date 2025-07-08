package observability.com.example.serviceA.config

import com.apollographql.apollo3.ApolloClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GraphQLClientConfig {

    @Value("\${servicec.graphql.url:http://localhost:8082/graphql}")
    private lateinit var serviceCGraphQLUrl: String

    @Bean
    fun apolloClient(): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl(serviceCGraphQLUrl)
            .build()
    }
}