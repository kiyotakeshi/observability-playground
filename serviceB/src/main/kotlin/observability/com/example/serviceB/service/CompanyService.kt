package observability.com.example.serviceB.service

import observability.com.example.serviceB.entity.Company
import observability.com.example.serviceB.repository.CompanyRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class CompanyService(
    private val companyRepository: CompanyRepository,
    private val redisTemplate: RedisTemplate<String, Any>
) {
    
    @Cacheable(value = ["companies"])
    fun getAllCompanies(): List<Company> {
        return companyRepository.findAll()
    }
    
    @Cacheable(value = ["company"], key = "#id")
    fun getCompanyById(id: Long): Company? {
        return companyRepository.findById(id).orElse(null)
    }
    
    fun getCompanyStats(): Map<String, Any> {
        val key = "company:stats"
        val cachedStats = redisTemplate.opsForHash<String, Any>().entries(key)
        
        if (cachedStats.isEmpty()) {
            val companies = companyRepository.findAll()
            val stats = mapOf(
                "totalCompanies" to companies.size,
                "industries" to companies.groupingBy { it.industry }.eachCount(),
                "companySizes" to companies.groupingBy { it.companySize }.eachCount()
            )
            
            redisTemplate.opsForHash<String, Any>().putAll(key, stats)
            redisTemplate.expire(key, 1, TimeUnit.HOURS)
            
            return stats
        }
        
        return cachedStats
    }
    
    @CacheEvict(value = ["companies", "company"], allEntries = true)
    fun clearCompanyCache() {
        redisTemplate.delete("company:stats")
    }
}