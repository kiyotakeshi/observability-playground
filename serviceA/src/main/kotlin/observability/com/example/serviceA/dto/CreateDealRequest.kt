package observability.com.example.serviceA.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.math.BigDecimal

data class CreateDealRequest(
    @field:NotNull(message = "employeeId は必須です")
    @field:Positive(message = "employeeId は正の数である必要があります")
    val employeeId: Long,

    @field:NotNull(message = "companyId は必須です")
    @field:Positive(message = "companyId は正の数である必要があります")
    val companyId: Long,

    @field:NotBlank(message = "title は必須です")
    val title: String,

    @field:NotBlank(message = "description は必須です")
    val description: String,

    @field:NotNull(message = "amount は必須です")
    @field:Positive(message = "amount は正の数である必要があります")
    val amount: BigDecimal,

    @field:NotBlank(message = "status は必須です")
    val status: String
)