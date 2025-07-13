package observability.com.example.serviceA.client

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

@Component
class CompanyServiceClient(
    @Qualifier("serviceBRestTemplate")
    private val restTemplate: RestTemplate
) {
    private val logger = LoggerFactory.getLogger(CompanyServiceClient::class.java)
    
    @Value("\${serviceb.url:http://localhost:8081}")
    private lateinit var serviceBBaseUrl: String

    fun checkCompanyExists(companyId: Long): Boolean {
        logger.info("serviceB に company の存在チェックを開始: companyId={}", companyId)
        
        return try {
            val response = restTemplate.getForObject(
                "$serviceBBaseUrl/companies/{id}",
                CompanyResponse::class.java,
                companyId
            )
            
            if (response != null) {
                logger.info("serviceB から company を取得成功: companyId={}, companyName={}", 
                    response.id, response.companyName)
                true
            } else {
                logger.warn("serviceB で company が見つかりません: companyId={}", companyId)
                false
            }
        } catch (ex: HttpClientErrorException) {
            when (ex.statusCode) {
                HttpStatusCode.valueOf(404) -> {
                    logger.warn("serviceB で company が見つかりません: companyId={}", companyId)
                    false
                }
                else -> {
                    logger.error("serviceB との通信でHTTPエラーが発生: companyId={}, status={}", 
                        companyId, ex.statusCode, ex)
                    throw RuntimeException("Company サービスとの通信に失敗しました", ex)
                }
            }
        } catch (ex: Exception) {
            logger.error("serviceB との通信で予期しないエラーが発生: companyId={}", companyId, ex)
            throw RuntimeException("Company サービスとの通信に失敗しました", ex)
        }
    }
}

data class CompanyResponse(
    val id: Long,
    val companyName: String,
    val industry: String,
    val companySize: String,
    val address: String,
    val phone: String,
    val website: String,
    val createdAt: String,
    val updatedAt: String
)