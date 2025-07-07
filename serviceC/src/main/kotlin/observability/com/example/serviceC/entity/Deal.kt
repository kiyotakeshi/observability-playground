package observability.com.example.serviceC.entity

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.*
import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "deals")
data class Deal(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    
    @Column(nullable = false)
    val title: String,
    
    @Column(nullable = false)
    val description: String,
    
    @Column(nullable = false)
    val employeeId: Long,
    
    @Column(nullable = false)
    val companyId: Long,
    
    @Column(nullable = false)
    val amount: BigDecimal,
    
    @Column(nullable = false)
    val status: String,
    
    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    
    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val updatedAt: LocalDateTime = LocalDateTime.now()
) : Serializable