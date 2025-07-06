package observability.com.example.serviceB.entity

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDateTime

@Entity
@Table(name = "companies")
data class Company(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    
    @Column(nullable = false)
    val companyName: String,
    
    @Column(nullable = false)
    val industry: String,
    
    @Column(nullable = false)
    val companySize: String,
    
    @Column(nullable = false)
    val address: String,
    
    @Column(nullable = false)
    val phone: String,
    
    @Column(nullable = false)
    val website: String,
    
    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    
    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val updatedAt: LocalDateTime = LocalDateTime.now()
) : Serializable