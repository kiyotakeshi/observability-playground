package observability.com.example.serviceC.controller

import observability.com.example.serviceC.codegen.types.Deal
import observability.com.example.serviceC.service.DealService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class DealController(
    private val dealService: DealService
) {
    
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
}