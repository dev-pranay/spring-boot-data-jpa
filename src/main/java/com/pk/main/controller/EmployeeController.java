package com.pk.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pk.main.model.Employee;
import com.pk.main.service.EmployeeService;

/**
 * @author PranaySK
 */

@RestController
@RequestMapping("/")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@GetMapping("employee")
	public List<Employee> getAllEmployees() {
		return employeeService.getEmployees();
	}

	@GetMapping("employee/{id}")
	public Employee getEmployee(@PathVariable("id") Integer id) {
		return employeeService.getEmployeeById(id);
	}

	@PostMapping(path = "employee", consumes = "application/json")
	public Employee addEmployee(@RequestBody Employee employee) {
		return employeeService.addEmployee(employee);
	}

	@PutMapping(path = "employee/{id}", consumes = "application/json")
	public Employee updateEmployee(@PathVariable("id") Integer id, @RequestBody Employee employee) {
		return employeeService.updateEmployee(id, employee);
	}

	@DeleteMapping(path = "employee/{id}")
	public String deleteEmployee(@PathVariable("id") Integer id) {
		employeeService.deleteEmployeeById(id);
		return "Deleted successfully";
	}

}
