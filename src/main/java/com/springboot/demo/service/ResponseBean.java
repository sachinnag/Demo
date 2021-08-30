package com.springboot.demo.service;

public class ResponseBean {
	private Object employees;
	private String status;
	
	public ResponseBean() {
		
	}
	
	public ResponseBean(Object employees, String status) {
		this.employees = employees;
		this.status = status;
	}

	public Object getEmployees() {
		return employees;
	}
	
	public void setEmployees(Object employees) {
		this.employees = employees;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
}
