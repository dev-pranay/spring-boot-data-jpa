package com.pk.main.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pk.main.constants.ErrorConstants;
import com.pk.main.exceptions.DataInsertionException;
import com.pk.main.exceptions.DbException;
import com.pk.main.model.Employee;
import com.pk.main.repo.EmployeeRepo;

/**
 * @author PranaySK
 */

@Service
public class EmployeeService {

	private static Logger logger = LoggerFactory.getLogger(EmployeeService.class);

	@Autowired
	private EmployeeRepo employeeRepo;

	@Transactional
	public List<Employee> getEmployees() {
		try {
			logger.debug("Getting all employees...");
			return employeeRepo.findAll();
		} catch (Exception e) {
			throw new DbException(ErrorConstants.DATA_RET_ERROR, "Error while retrieving employees");
		}
	}

	@Transactional
	public Employee getEmployeeById(Integer id) {
		try {
			logger.debug("Getting employee with id - {}", id);
			return employeeRepo.findById(id).orElse(null);
		} catch (Exception e) {
			throw new DbException(ErrorConstants.DATA_RET_ERROR, "Error while retrieving employee");
		}
	}

	@Transactional
	public Employee updateEmployee(Integer id, Employee employee) {
		try {
			logger.debug("Updating employee... {}", id);
			Employee dbEmp = employeeRepo.findById(id).orElse(null);
			if (dbEmp == null) {
				return employeeRepo.save(employee);
			}
			dbEmp.setName(employee.getName());
			dbEmp.setTech(employee.getTech());
			return employeeRepo.save(dbEmp);
		} catch (Exception e) {
			throw new DbException(ErrorConstants.DATA_NOT_FOUND_ERROR, "Employee not found");
		}
	}

	@Transactional
	public Employee addEmployee(Employee employee) {
		try {
			logger.debug("Adding employee...");
			return employeeRepo.save(EmployeeService.toEntity(employee));
		} catch (Exception e) {
			throw new DataInsertionException(ErrorConstants.INSERTION_ERROR, "Error while adding employee");
		}
	}

	@Transactional
	public String deleteEmployeeById(Integer id) {
		try {
			logger.debug("Deleting employee with id - {}", id);
			employeeRepo.deleteById(id);
			return "Deleted successfully";
		} catch (Exception e) {
			throw new DbException(ErrorConstants.DATA_DELETION_ERROR, "Error while deleting employee");
		}
	}

	private static Employee toEntity(Employee employee) {
		logger.debug("Employee is - {}", employee);
		Employee entity = new Employee();
		entity.setId(employee.getId());
		entity.setName(employee.getName());
		entity.setTech(employee.getTech());
		return entity;
	}
}
