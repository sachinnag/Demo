package com.springboot.demo.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="EMPLOYEE")
public class Employee {
	@Id
	@Column(name="EMPLOYEE_CODE")
	private String employeeCode;
	
	@Column(name="DEPARTMENT")
	private String department;
	
	@Column(name="SCORE")
	private int score;
	
	@Column(name="DATE_CREATED")
	private Date dateCreated;

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		
		Employee e = (Employee) obj;
		
		return this.getEmployeeCode() == e.getEmployeeCode();
	}
}
