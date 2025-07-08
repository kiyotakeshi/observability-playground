package observability.com.example.serviceA.controller

import observability.com.example.serviceA.entity.Employee
import observability.com.example.serviceA.repository.EmployeeRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/employees")
class EmployeeController(
    private val employeeRepository: EmployeeRepository
) {
    
    @GetMapping
    fun getAllEmployees(): List<Employee> {
        return employeeRepository.findAll()
    }
    
    @GetMapping("/{id}")
    fun getEmployeeById(@PathVariable id: Long): Employee? {
        return employeeRepository.findById(id).orElse(null)
    }
}