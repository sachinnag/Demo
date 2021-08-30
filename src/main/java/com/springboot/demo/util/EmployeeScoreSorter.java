package com.springboot.demo.util;

import java.util.Comparator;

import com.springboot.demo.entity.Employee;

public class EmployeeScoreSorter implements Comparator<Employee> {

	public int compare(Employee o1, Employee o2) {
		if(o1.getScore() < o2.getScore())
			return 1;
		else if(o1.getScore() > o2.getScore())
			return -1;
		else
			return 1;
	}
	
}
