package observability.com.example.serviceA.entity

import jakarta.persistence.*

@Entity
@Table(name = "employees")
data class Employee(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    
    @Column(nullable = false)
    val name: String,
    
    @Column(nullable = false)
    val email: String,
    
    @Column(nullable = false)
    val department: String,
    
    @Column(nullable = false)
    val position: String
)