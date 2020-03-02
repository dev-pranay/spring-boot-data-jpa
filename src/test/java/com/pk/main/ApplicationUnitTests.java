package com.pk.main;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.pk.main.model.Employee;
import com.pk.main.repo.EmployeeRepo;
import com.pk.main.service.EmployeeService;

/**
 * @author PranaySK
 */

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = SpringBootDataJpaApplication.class)
@TestInstance(Lifecycle.PER_CLASS)
public class ApplicationUnitTests {

	@Mock
	private EmployeeRepo mockRepo;

	@InjectMocks
	private EmployeeService employeeService;

	@BeforeAll
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void contextLoads() {
		assertTrue(true);
	}

	@Test
	public void testGetEmployeeById() {
		int id = 1;
		Employee employee = new Employee(id, "Emp1", "Java");
		Optional<Employee> opEmp = Optional.ofNullable(employee);
		when(mockRepo.findById(id)).thenReturn(opEmp);

		assertThat(employeeService.getEmployeeById(id), is(notNullValue()));
		assertEquals(1, opEmp.get().getId());

		verify(mockRepo, times(1)).findById(Mockito.anyInt());
	}

	@Test
	public void testGetAllEmployees() {
		Employee e0 = new Employee(1, "Emp1", "Java");
		Employee e1 = new Employee(2, "Emp2", "SQL");
		Employee e2 = new Employee(3, "Emp3", "JS");
		Employee e3 = new Employee(4, "Emp4", "ML");
		Employee e4 = new Employee(5, "Emp5", "AT");

		List<Employee> mockEmployeeList = new ArrayList<>();
		mockEmployeeList.add(e0);
		mockEmployeeList.add(e1);
		mockEmployeeList.add(e2);
		mockEmployeeList.add(e3);
		mockEmployeeList.add(e4);

		when(mockRepo.findAll()).thenReturn(mockEmployeeList);

		assertThat(employeeService.getEmployees(), is(notNullValue()));
		assertEquals(2, mockEmployeeList.get(1).getId());

		verify(mockRepo, times(1)).findAll();
	}

	@Test
	public void testAddEmployee() {
		Employee employee = new Employee(1, "Emp1", "Java");

		when(mockRepo.save(employee)).thenAnswer(new Answer<Employee>() {
			@Override
			public Employee answer(InvocationOnMock invocation) throws Throwable {
				Object[] arguments = invocation.getArguments();
				if (arguments != null && arguments.length > 0 && arguments[0] != null) {
					Employee employee = (Employee) arguments[0];
					employee.setId(1);
					return employee;
				}
				return null;
			}
		});

		assertThat(employeeService.addEmployee(employee), is(notNullValue()));

		verify(mockRepo, times(1)).save(Mockito.any(Employee.class));
		verify(mockRepo, never()).deleteById(Mockito.anyInt());
	}

	@Test
	public void testUpdateEmployee() {
		int id = 3;
		Employee employee = new Employee(id, "Emp6", "Java");

		when(mockRepo.save(employee)).thenAnswer(new Answer<Employee>() {
			@Override
			public Employee answer(InvocationOnMock invocation) throws Throwable {
				Object[] arguments = invocation.getArguments();
				if (arguments != null && arguments.length > 0 && arguments[0] != null) {
					Employee employee = (Employee) arguments[0];
					employee.setId(id);
					return employee;
				}
				return null;
			}
		});

		assertThat(employeeService.updateEmployee(id, employee), is(notNullValue()));
		assertEquals("Emp6", employee.getName());

		verify(mockRepo, times(2)).save(Mockito.any(Employee.class));
	}

	@Test
	public void testDeleteEmployeeById() {
		int id = 5;
		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				Object[] arguments = invocation.getArguments();
				if (arguments != null && arguments.length > 0 && arguments[0] != null) {
					System.out.println(arguments[0]);
				}
				return null;
			}
		}).when(mockRepo).deleteById(Mockito.anyInt());

		assertThat(employeeService.deleteEmployeeById(id), is("Deleted successfully"));

		verify(mockRepo, times(1)).deleteById(Mockito.anyInt());
	}

}
