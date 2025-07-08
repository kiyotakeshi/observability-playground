package observability.com.example.serviceA.client

import com.apollographql.apollo3.ApolloClient
import kotlinx.coroutines.runBlocking
import observability.com.example.serviceA.graphql.CreateDealMutation
import observability.com.example.serviceA.graphql.type.DealInput
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class DealServiceClient(
    private val apolloClient: ApolloClient
) {
    private val logger = LoggerFactory.getLogger(DealServiceClient::class.java)

    fun createDeal(input: CreateDealInput): DealGraphQLResponse {
        logger.info("serviceC に deal の作成を開始: employeeId={}, companyId={}, title={}", 
            input.employeeId, input.companyId, input.title)

        val dealInput = DealInput(
            title = input.title,
            description = input.description,
            employeeId = input.employeeId,
            companyId = input.companyId,
            amount = input.amount,
            status = input.status
        )

        return try {
            val response = runBlocking {
                apolloClient.mutation(CreateDealMutation(dealInput)).execute()
            }

            if (response.hasErrors()) {
                val errors = response.errors?.joinToString(", ") { it.message }
                logger.error("serviceC での deal 作成でエラーが発生: errors={}", errors)
                throw RuntimeException("Deal 作成でエラーが発生しました: $errors")
            }

            response.data?.createDeal?.let { deal ->
                val result = DealGraphQLResponse(
                    id = deal.id,
                    title = deal.title,
                    description = deal.description,
                    employeeId = deal.employeeId,
                    companyId = deal.companyId,
                    amount = deal.amount,
                    status = deal.status,
                    createdAt = deal.createdAt,
                    updatedAt = deal.updatedAt
                )
                logger.info("serviceC で deal の作成が成功: dealId={}, employeeId={}, companyId={}", 
                    result.id, result.employeeId, result.companyId)
                result
            } ?: throw RuntimeException("Deal の作成に失敗しました: データが空です")

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