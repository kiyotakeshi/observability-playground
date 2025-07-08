package observability.com.example.serviceC.service

import observability.com.example.serviceC.entity.Deal
import observability.com.example.serviceC.repository.DealRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class DealService(
    private val dealRepository: DealRepository
) {
    
    fun getAllDeals(): List<Deal> {
        return dealRepository.findAll()
    }
    
    fun getDealById(id: Long): Deal? {
        return dealRepository.findById(id).orElse(null)
    }
    
    fun createDeal(
        title: String,
        description: String,
        employeeId: Long,
        companyId: Long,
        amount: BigDecimal,
        status: String
    ): Deal {
        val deal = Deal(
            title = title,
            description = description,
            employeeId = employeeId,
            companyId = companyId,
            amount = amount,
            status = status,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        return dealRepository.save(deal)
    }
}