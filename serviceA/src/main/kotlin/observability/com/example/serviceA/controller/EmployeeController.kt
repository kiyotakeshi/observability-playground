package observability.com.example.serviceA.controller

import jakarta.validation.Valid
import observability.com.example.serviceA.dto.CreateDealRequest
import observability.com.example.serviceA.entity.Employee
import observability.com.example.serviceA.repository.EmployeeRepository
import observability.com.example.serviceA.service.DealOrchestrationService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/employees")
class EmployeeController(
    private val employeeRepository: EmployeeRepository,
    private val dealOrchestrationService: DealOrchestrationService
) {
    private val logger = LoggerFactory.getLogger(EmployeeController::class.java)
    
    @GetMapping
    fun getAllEmployees(): List<Employee> {
        return employeeRepository.findAll()
    }
    
    @GetMapping("/{id}")
    fun getEmployeeById(@PathVariable id: Long): Employee? {
        return employeeRepository.findById(id).orElse(null)
    }
    
    @PostMapping("/deals")
    fun createDeal(@Valid @RequestBody request: CreateDealRequest): ResponseEntity<Any> {
        logger.info("deal 作成リクエスト受信: employeeId={}, companyId={}, title={}", 
            request.employeeId, request.companyId, request.title)
        
        return try {
            val response = dealOrchestrationService.createDeal(request)
            logger.info("deal 作成が正常に完了: dealId={}", response.id)
            ResponseEntity.ok(response)
        } catch (ex: IllegalArgumentException) {
            logger.warn("deal 作成でバリデーションエラー: {}", ex.message)
            ResponseEntity.badRequest().body(mapOf("error" to ex.message))
        } catch (ex: Exception) {
            logger.error("deal 作成で予期しないエラーが発生", ex)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(mapOf("error" to "システムエラーが発生しました"))
        }
    }
}