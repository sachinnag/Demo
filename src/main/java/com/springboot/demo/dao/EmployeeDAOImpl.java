package com.springboot.demo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.demo.entity.Employee;

@Repository
public class EmployeeDAOImpl implements EmployeeDAO {
	
	private EntityManager entityManager;
	
	@Autowired
	public EmployeeDAOImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	@Transactional
	public List<Employee> getEmployees() {
				
		Session currentSession = entityManager.unwrap(Session.class);
		
		Query<Employee> query = currentSession.createQuery("from Employee", Employee.class);
		
		List<Employee> employees = query.getResultList();
		
		return employees;
	}

}
