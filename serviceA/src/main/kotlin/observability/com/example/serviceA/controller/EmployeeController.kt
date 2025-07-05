package observability.com.example.serviceA.controller

import observability.com.example.serviceA.entity.Employee
import observability.com.example.serviceA.repository.EmployeeRepository
import org.springframework.web.bind.annotation.GetMapping
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
}