package observability.com.example.serviceC.repository

import observability.com.example.serviceC.entity.Deal
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DealRepository : JpaRepository<Deal, Long>