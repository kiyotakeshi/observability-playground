package observability.com.example.serviceC

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ServiceCApplication

fun main(args: Array<String>) {
	runApplication<ServiceCApplication>(*args)
}
