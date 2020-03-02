package com.pk.main.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pk.main.model.Employee;

/**
 * @author PranaySK
 */

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Integer> {

}
