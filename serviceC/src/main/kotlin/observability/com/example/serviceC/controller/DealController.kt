package observability.com.example.serviceC.controller

import observability.com.example.serviceC.codegen.types.Deal
import observability.com.example.serviceC.codegen.types.DealInput
import observability.com.example.serviceC.service.DealService
import org.slf4j.LoggerFactory
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import graphql.schema.DataFetchingEnvironment
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.math.BigDecimal

@Controller
class DealController(
    private val dealService: DealService
) {
    companion object {
        private val logger = LoggerFactory.getLogger(DealController::class.java)
    }
    
    @QueryMapping
    fun deals(): List<Deal> {
        return dealService.getAllDeals().map {
            Deal(
                id = it.id.toString(),
                title = it.title,
                description = it.description,
                employeeId = it.employeeId,
                companyId = it.companyId,
                amount = it.amount.toDouble(),
                status = it.status,
                createdAt = it.createdAt.toString(),
                updatedAt = it.updatedAt.toString()
            )
        }
    }

    @QueryMapping
    fun deal(@Argument id: Long): Deal? {
        val dealById = dealService.getDealById(id)
        return Deal(
            id = dealById?.id?.toString() ?: return null,
            title = dealById.title,
            description = dealById.description,
            employeeId = dealById.employeeId,
            companyId = dealById.companyId,
            amount = dealById.amount.toDouble(),
            status = dealById.status,
            createdAt = dealById.createdAt.toString(),
            updatedAt = dealById.updatedAt.toString()
        )
    }

    @MutationMapping
    fun createDeal(@Argument input: DealInput): Deal {
        val requestAttributes = RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes
        val request = requestAttributes?.request
        
        if (request != null) {
            logger.info("[serviceC] 受信したリクエストヘッダー:")
            request.headerNames.asSequence().forEach { headerName ->
                logger.info("[serviceC] Header: $headerName = ${request.getHeader(headerName)}")
            }
        }
        
        val createdDeal = dealService.createDeal(
            title = input.title,
            description = input.description,
            employeeId = input.employeeId,
            companyId = input.companyId,
            amount = BigDecimal.valueOf(input.amount),
            status = input.status
        )
        
        return Deal(
            id = createdDeal.id.toString(),
            title = createdDeal.title,
            description = createdDeal.description,
            employeeId = createdDeal.employeeId,
            companyId = createdDeal.companyId,
            amount = createdDeal.amount.toDouble(),
            status = createdDeal.status,
            createdAt = createdDeal.createdAt.toString(),
            updatedAt = createdDeal.updatedAt.toString()
        )
    }
}