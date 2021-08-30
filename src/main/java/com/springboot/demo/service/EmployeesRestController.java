package com.springboot.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.demo.dao.EmployeeDAO;
import com.springboot.demo.entity.Employee;
import com.springboot.demo.util.Utils;

@RestController
@RequestMapping("/api")
public class EmployeesRestController {

	@Autowired
	private EmployeeDAO employeeDAO;
	
	/**
	 * This method fetches the employees data from the database and returns the processed data based on criteria.
	 * This method hits the database for only one time.
	 * 
	 * <br>{
	 * <br>method_name = getEmployees
	 * <br>body_uri = /beltech/api/employees
	 * <br>type = GET
	 * <br>}
	 * @return the ResponseBean
	 */
	@GetMapping("/employees")
	public ResponseBean getEmployees() {
		List<Employee> employees = employeeDAO.getEmployees();
		
		if(Utils.isNotEmpty(employees)) {
			ResponseBean response = new ResponseBean();
			response.setEmployees(EmployeeService.mapEmployeeRecords(employees));
			response.setStatus(HttpStatus.OK.toString());
			return response;
		}
		
		return new ResponseBean("No Content", HttpStatus.NO_CONTENT.toString());
	}
	
	/**
	 * This method fetches the employees data from the database and returns the specified chunk of processed data 
	 * based on criteria. This method hits the database for only one time.
	 * 
	 * <br>{
	 * <br>method_name = getEmployeesInChunk
	 * <br>body_uri = /beltech/api/employees/?chunk={n}
	 * <br>type = GET
	 * <br>}
	 * @return the ResponseBean
	 */
	@GetMapping("/employees/")
	public ResponseBean getEmployeesInChunk(@RequestParam(name="chunk") int n) {
		List<Employee> employees = employeeDAO.getEmployees();
		
		if(Utils.isNotEmpty(employees)) {
			ResponseBean response = new ResponseBean();
			response.setEmployees(EmployeeService.mapEmployeeRecords(employees, n));
			response.setStatus(HttpStatus.OK.toString());
			return response;
		}
		
		return new ResponseBean("No Content", HttpStatus.NO_CONTENT.toString());
	}
}
