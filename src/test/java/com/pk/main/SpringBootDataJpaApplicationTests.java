package com.pk.main;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.pk.main.controller.EmployeeController;
import com.pk.main.model.Employee;
import com.pk.main.service.EmployeeService;

/**
 * @author PranaySK
 */

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = SpringBootDataJpaApplication.class)
@WebMvcTest(value = EmployeeController.class)
class SpringBootDataJpaApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private EmployeeService employeeService;

	@Test
	public void contextLoads() {
		assertTrue(true);
	}

	@Test
	public void testRetrieveAllEmployees() throws Exception {
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

		// employeeService.getEmployees to respond back with mockEmployee list
		Mockito.when(employeeService.getEmployees()).thenReturn(mockEmployeeList);
		assertThat(employeeService.getEmployees().get(0), is(notNullValue()));

		// Send request to /employee
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/employee").accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String expected = "[{id:1,name:Emp1,tech:Java},{id:2,name:Emp2,tech:SQL},{id:3,name:Emp3,tech:JS},{id:4,name:Emp4,tech:ML},{id:5,name:Emp5,tech:AT}]";

		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), true);
	}

	@Test
	public void testRetrieveEmployeeById() throws Exception {
		Employee mockEmployee = new Employee(5, "Emp5", "AI");

		// employeeService.getEmployeeById to respond back with mockEmployee
		Mockito.when(employeeService.getEmployeeById(Mockito.anyInt())).thenReturn(mockEmployee);
		assertThat(employeeService.getEmployeeById(Mockito.anyInt()), is(notNullValue()));

		// Send request to /employee by passing employee Id as path variable
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/employee/5").accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String expected = "{id:5,name:Emp5,tech:AI}";

		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), true);
	}

	@Test
	public void testCreateEmployee() throws Exception {
		String employeeJson = "{\"id\":6,\"name\":\"Emp6\",\"tech\":\"Java\"}";
		// Send employee to be newly added as body to /employee
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/employee").accept(MediaType.APPLICATION_JSON)
				.content(employeeJson).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}

	@Test
	public void testUpdateEmployee() throws Exception {
		String exampleEmployeeJson = "{\"id\":4,\"name\":\"Emp7\",\"tech\":\"AT\"}";
		// Send employee to be updated as body to /employee
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/employee/4").accept(MediaType.APPLICATION_JSON)
				.content(exampleEmployeeJson).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}

	@Test
	public void testDeleteEmployee() throws Exception {
		// Send request to /employee by passing Id as path variable
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/employee/5").accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();

		assertEquals("Deleted successfully", response.getContentAsString());
	}

}
