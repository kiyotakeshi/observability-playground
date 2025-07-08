package observability.com.example.serviceA.dto

import java.math.BigDecimal

data class CreateDealResponse(
    val id: String,
    val title: String,
    val description: String,
    val employeeId: Long,
    val companyId: Long,
    val amount: BigDecimal,
    val status: String,
    val createdAt: String,
    val updatedAt: String
)