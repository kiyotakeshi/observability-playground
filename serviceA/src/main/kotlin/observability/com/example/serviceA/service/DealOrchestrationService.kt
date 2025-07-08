package observability.com.example.serviceA.service

import observability.com.example.serviceA.client.CompanyServiceClient
import observability.com.example.serviceA.client.CreateDealInput
import observability.com.example.serviceA.client.DealServiceClient
import observability.com.example.serviceA.dto.CreateDealRequest
import observability.com.example.serviceA.dto.CreateDealResponse
import observability.com.example.serviceA.repository.EmployeeRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class DealOrchestrationService(
    private val employeeRepository: EmployeeRepository,
    private val companyServiceClient: CompanyServiceClient,
    private val dealServiceClient: DealServiceClient
) {
    private val logger = LoggerFactory.getLogger(DealOrchestrationService::class.java)

    fun createDeal(request: CreateDealRequest): CreateDealResponse {
        logger.info("deal 作成プロセスを開始: employeeId={}, companyId={}, title={}", 
            request.employeeId, request.companyId, request.title)

        try {
            // 1. Employee の存在チェック
            validateEmployeeExists(request.employeeId)
            
            // 2. Company の存在チェック
            validateCompanyExists(request.companyId)
            
            // 3. Deal の作成
            val dealResponse = createDealInServiceC(request)
            
            logger.info("deal 作成プロセスが正常に完了: dealId={}, employeeId={}, companyId={}", 
                dealResponse.id, dealResponse.employeeId, dealResponse.companyId)
            
            return CreateDealResponse(
                id = dealResponse.id,
                title = dealResponse.title,
                description = dealResponse.description,
                employeeId = dealResponse.employeeId,
                companyId = dealResponse.companyId,
                amount = BigDecimal.valueOf(dealResponse.amount),
                status = dealResponse.status,
                createdAt = dealResponse.createdAt,
                updatedAt = dealResponse.updatedAt
            )
        } catch (ex: Exception) {
            logger.error("deal 作成プロセスでエラーが発生: employeeId={}, companyId={}", 
                request.employeeId, request.companyId, ex)
            throw ex
        }
    }

    private fun validateEmployeeExists(employeeId: Long) {
        logger.debug("employee の存在チェックを開始: employeeId={}", employeeId)
        
        val exists = employeeRepository.existsById(employeeId)
        if (!exists) {
            logger.warn("employee が存在しません: employeeId={}", employeeId)
            throw IllegalArgumentException("指定された employee が存在しません: employeeId=$employeeId")
        }
        logger.debug("employee の存在チェック完了: employeeId={}", employeeId)
    }

    private fun validateCompanyExists(companyId: Long) {
        logger.debug("company の存在チェックを開始: companyId={}", companyId)
        
        val exists = companyServiceClient.checkCompanyExists(companyId)
        if (!exists) {
            logger.warn("company が存在しません: companyId={}", companyId)
            throw IllegalArgumentException("指定された company が存在しません: companyId=$companyId")
        }
        logger.debug("company の存在チェック完了: companyId={}", companyId)
    }

    private fun createDealInServiceC(request: CreateDealRequest): observability.com.example.serviceA.client.DealGraphQLResponse {
        logger.debug("serviceC で deal の作成を開始: title={}", request.title)
        
        val dealInput = CreateDealInput(
            title = request.title,
            description = request.description,
            employeeId = request.employeeId,
            companyId = request.companyId,
            amount = request.amount.toDouble(),
            status = request.status
        )

        val dealResponse = dealServiceClient.createDeal(dealInput)
        logger.debug("serviceC での deal 作成が完了: dealId={}", dealResponse.id)
        return dealResponse
    }
}