package observability.com.example.serviceA.client

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

@Component
class DealServiceClient(
    @Qualifier("serviceCRestTemplate")
    private val restTemplate: RestTemplate
) {
    private val logger = LoggerFactory.getLogger(DealServiceClient::class.java)
    private val serviceCBaseUrl = "http://localhost:8082"

    fun createDeal(input: CreateDealInput): DealGraphQLResponse {
        logger.info("serviceC に deal の作成を開始: employeeId={}, companyId={}, title={}", 
            input.employeeId, input.companyId, input.title)

        val graphqlQuery = """
            mutation CreateDeal(${'$'}input: DealInput!) {
                createDeal(input: ${'$'}input) {
                    id
                    title
                    description
                    employeeId
                    companyId
                    amount
                    status
                    createdAt
                    updatedAt
                }
            }
        """.trimIndent()

        val requestBody = mapOf(
            "query" to graphqlQuery,
            "variables" to mapOf("input" to input)
        )

        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }
        val httpEntity = HttpEntity(requestBody, headers)

        return try {
            val response = restTemplate.exchange(
                "$serviceCBaseUrl/graphql",
                HttpMethod.POST,
                httpEntity,
                GraphQLResponse::class.java
            )

            val graphqlResponse = response.body
                ?: throw RuntimeException("Deal の作成に失敗しました: レスポンスが空です")

            if (graphqlResponse.errors != null && graphqlResponse.errors.isNotEmpty()) {
                logger.error("serviceC での deal 作成でエラーが発生: errors={}", graphqlResponse.errors)
                throw RuntimeException("Deal 作成でエラーが発生しました: ${graphqlResponse.errors.joinToString(", ") { it.message }}")
            }

            graphqlResponse.data?.createDeal?.let { deal ->
                logger.info("serviceC で deal の作成が成功: dealId={}, employeeId={}, companyId={}", 
                    deal.id, deal.employeeId, deal.companyId)
                deal
            } ?: throw RuntimeException("Deal の作成に失敗しました: データが空です")

        } catch (ex: HttpClientErrorException) {
            logger.error("serviceC との通信でHTTPエラーが発生: status={}, body={}", 
                ex.statusCode, ex.responseBodyAsString, ex)
            throw RuntimeException("Deal サービスとの通信に失敗しました: ${ex.statusCode}", ex)
        } catch (ex: Exception) {
            logger.error("serviceC との通信で予期しないエラーが発生", ex)
            throw RuntimeException("Deal サービスとの通信に失敗しました", ex)
        }
    }
}

data class CreateDealInput(
    val title: String,
    val description: String,
    val employeeId: Long,
    val companyId: Long,
    val amount: Double,
    val status: String
)

data class GraphQLResponse(
    val data: GraphQLData?,
    val errors: List<GraphQLError>?
)

data class GraphQLData(
    val createDeal: DealGraphQLResponse?
)

data class GraphQLError(
    val message: String,
    val path: List<String>? = null,
    val extensions: Map<String, Any>? = null
)

data class DealGraphQLResponse(
    val id: String,
    val title: String,
    val description: String,
    val employeeId: Long,
    val companyId: Long,
    val amount: Double,
    val status: String,
    val createdAt: String,
    val updatedAt: String
)