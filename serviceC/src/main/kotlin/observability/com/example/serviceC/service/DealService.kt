package observability.com.example.serviceC.service

import observability.com.example.serviceC.entity.Deal
import observability.com.example.serviceC.repository.DealRepository
import org.springframework.stereotype.Service

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
}