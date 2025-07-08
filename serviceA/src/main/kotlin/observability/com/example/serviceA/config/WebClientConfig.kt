package observability.com.example.serviceA.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.RestTemplate
import java.time.Duration

@Configuration
class RestClientConfig {

    @Bean("serviceBRestTemplate")
    fun serviceBRestTemplate(): RestTemplate {
        val factory = SimpleClientHttpRequestFactory().apply {
            setConnectTimeout(5000)
            setReadTimeout(5000)
        }
        return RestTemplate(factory)
    }

    @Bean("serviceCRestTemplate")
    fun serviceCRestTemplate(): RestTemplate {
        val factory = SimpleClientHttpRequestFactory().apply {
            setConnectTimeout(10000)
            setReadTimeout(10000)
        }
        return RestTemplate(factory)
    }
}