package observability.com.example.serviceB.controller

import observability.com.example.serviceB.entity.Company
import observability.com.example.serviceB.service.CompanyService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/companies")
class CompanyController(
    private val companyService: CompanyService
) {
    
    @GetMapping
    fun getAllCompanies(): List<Company> {
        return companyService.getAllCompanies()
    }
    
    @GetMapping("/{id}")
    fun getCompanyById(@PathVariable id: Long): ResponseEntity<Company> {
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