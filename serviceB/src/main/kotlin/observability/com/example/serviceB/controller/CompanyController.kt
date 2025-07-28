package observability.com.example.serviceB.controller

import observability.com.example.serviceB.entity.Company
import observability.com.example.serviceB.service.CompanyService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import jakarta.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/companies")
class CompanyController(
    private val companyService: CompanyService
) {
    companion object {
        private val logger = LoggerFactory.getLogger(CompanyController::class.java)
    }
    
    @GetMapping
    fun getAllCompanies(): List<Company> {
        return companyService.getAllCompanies()
    }
    
    @GetMapping("/{id}")
    fun getCompanyById(@PathVariable id: Long, request: HttpServletRequest): ResponseEntity<Company> {
        logger.info("[serviceB] 受信したリクエストヘッダー:")
        request.headerNames.asSequence().forEach { headerName ->
            logger.info("[serviceB] Header: $headerName = ${request.getHeader(headerName)}")
        }
        
        val company = companyService.getCompanyById(id)
        return if (company != null) {
            ResponseEntity.ok(company)
        } else {
            ResponseEntity.notFound().build()
        }
    }
    
    @GetMapping("/stats")
    fun getCompanyStats(): Map<String, Any> {
        return companyService.getCompanyStats()
    }
    
    @DeleteMapping("/cache")
    fun clearCache(): ResponseEntity<String> {
        companyService.clearCompanyCache()
        return ResponseEntity.ok("Cache cleared successfully")
    }
}