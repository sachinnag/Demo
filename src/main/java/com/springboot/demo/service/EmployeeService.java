package com.springboot.demo.service;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.springboot.demo.entity.Employee;
import com.springboot.demo.util.AppConstants;
import com.springboot.demo.util.EmployeeScoreSorter;
import com.springboot.demo.util.Utils;

public class EmployeeService {
	
	/** This method internally invokes mapRecords() method to split and merge the employees data based on the criteria
	 * and returns the list.
	 * 
	 * @param employees is the list of employees fetched from Database
	 * @return the employee objects into a list, else in case of errors it returns a single list containing original data
	 */
	public static List<Employee> mapEmployeeRecords(List<Employee> employees) {
		try {
			return mapRecords(employees);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return employees;
	}
	
	/** This method internally invokes mapRecords() method to split and merge the employees data based on the criteria.
	 * And then it invokes the chunkedOutput() method with the mapped data to break it down into chunks 
	 * and returns the specific chunk of data.
	 * 
	 * @param employees is the list of employees fetched from Database
	 * @param chunk is the number of employee objects stored in a list forming multiple chunks
	 * @return the employee objects broken into chunks, else in case of errors it returns a single list containing original data or mapped data
	 */
	public static List<Employee> mapEmployeeRecords(List<Employee> employees, int chunk) {
		List<Employee> mappedEmployees = null;
		try {
			mappedEmployees = mapRecords(employees);
			if(chunk > 0) {
				return chunkedOutput(mappedEmployees, chunk);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		if(Utils.isNotEmpty(mappedEmployees))
			return mappedEmployees;
		else
			return employees;
	}
	
	/** This method maps the data into multiple employees list based on the criteria.
	 * The first criteria - the collectEmpWithDept stores the list of employees which are with a specific department.
	 * <br>The second criteria - the collectEmpWithDate stores the list of employees which are created within last 14 days from the current date.
	 * <br>The last criteria -  the collectEmpSortWithScore stores the list of employees which are in descending order based on the score.
	 * The collectEmpSortWithScore list does not include the employees which are in the lists such as collectEmpWithDept and collectEmpWithDate.
	 * <br><br>And finally it invokes the mergedRecords() method internally to merge these lists into a single list with the given positions and returns the list.
	 * 
	 * @param employees is the list of employees fetched from the Database
	 * @return the list of mapped and merged employees
	 */
	public static List<Employee> mapRecords(List<Employee> employees) {
		Set<Employee> collectEmpSortWithScore = new TreeSet<>(new EmployeeScoreSorter());
		List<Employee> collectEmpWithDept =  new LinkedList<>();
		List<Employee> collectEmpWithDate = new LinkedList<>();
		
		for(Employee e : employees) {
			if(AppConstants.DEPT_BELTECH.equalsIgnoreCase(e.getDepartment()))
				collectEmpWithDept.add(e);
			else if(e.getDateCreated().after(getAfterDate()))
				collectEmpWithDate.add(e);
			else
				collectEmpSortWithScore.add(e);
		}
		
		return mergedRecords(new LinkedList<Employee>(collectEmpSortWithScore), 
				collectEmpWithDept, collectEmpWithDate);
	}
	
	/** This method merges the given lists to a single list based on the number of consecutive employees objects from the empWithScore list 
	 * to be merged with the other employee objects from different list.
	 * <br>After each consecutive employee objects, maximum of 2 elements merged into empWithScore at every 5th & 6th indices and 
	 * removed from the empWithDept, and followed by the maximum of 2 elements merged into empWithScore at every 7th and 8th indices 
	 * and removed from the empWithDate.
	 * <br>Again, skips the consecutive employees objects and merges the element and this keeps on repeating until the whole list is iterated.
	 * 
	 * @param empWithScore is the list of employees sorted by score and does not include employees that are stored in empWithDept or empWithDate 
	 * @param empWithDept is the list of employees containing employees with specific department
	 * @param empWithDate is the list of employees containing employees created within last 14 days from the current date
	 * @return the list of merged employees
	 */
	public static List<Employee> mergedRecords(List<Employee> empWithScore, List<Employee> empWithDept, List<Employee> empWithDate) {
		// initialized with the number of consecutive employees objects to be merged with the other employee objects in different list
		int scoreEmpCount = AppConstants.NUM_OF_CONSECUTIVE_INDICES;
		
		// loops until the scoreEmpCount (index) is within the bounds of the range from 0 (inclusive) to list size (exclusive)
		while(Utils.ifIndexExists(scoreEmpCount, empWithScore.size())) {
			Employee emp_5th = null;
			Employee emp_6th = null;
			Employee emp_7th = null;
			Employee emp_8th = null;
			
			if(Utils.isNotEmpty(empWithDept)) {
				// gets and removes the an element from the LinkedList
				emp_5th = empWithDept.remove(0);
				// merges the element into the empWithScore list in the scoreEmpCount (index) and post incremented
				empWithScore.add(scoreEmpCount++, emp_5th);
				
				// again checks if the list is not empty
				if(Utils.isNotEmpty(empWithDept)) {
					// gets and removes the an element from the LinkedList
					emp_6th = empWithDept.remove(0);
					// merges the element into the empWithScore in the scoreEmpCount (index) and post incremented
					empWithScore.add(scoreEmpCount++, emp_6th);
				}
			}
			
			if(Utils.isNotEmpty(empWithDate)) {
				emp_7th = empWithDate.remove(0);
				empWithScore.add(scoreEmpCount++, emp_7th);
				
				if(Utils.isNotEmpty(empWithDate)) {
					emp_8th = empWithDate.remove(0);
					empWithScore.add(scoreEmpCount++, emp_8th);
				}
			}
			
			// after merging other elements into the empWithScore list, the count adds up the number of consecutive employee objects
			scoreEmpCount += AppConstants.NUM_OF_CONSECUTIVE_INDICES;
		}
		
		return empWithScore;
	}
	
	/** This method breaks down the complete processed employees list into chunks with the specified chunk size
	 * and returns back specific chunk with the given chunk value.
	 * 
	 * @param employees is the list of processed employees
	 * @param chunk is the value given to get the nth chunk of data
	 * @return chunkedEmployees is the list of employees in a chunk
	 */
	public static List<Employee> chunkedOutput(List<Employee> employees, int chunk) {		
		// holds the total number of chunks
		int empChunksCount = (employees.size() / AppConstants.CHUNK_SIZE) + 1;
		
		// holds the starting index of the nth chunk
		int fromEmpCount = (chunk-1) * AppConstants.CHUNK_SIZE;
		
		if(chunk < empChunksCount) { // if the nth chunk is less than total number of chunks
			// holds the end index of the nth chunk
			int toEmpCount = fromEmpCount + AppConstants.CHUNK_SIZE;
			
			// returns the sub-list with the calculated start and end index
			return employees.subList(fromEmpCount, toEmpCount);
		}
		else if(chunk == empChunksCount) { // if the nth chunk is equal to the total number of chunks
			// the nth chunk is the last chunk of employee objects, hence this chunk may contain less or equal 
			// number of objects when compared to chunk size and returns the sub-list with the calculated start index and end index
			return employees.subList(fromEmpCount, employees.size());
		}
		else { // if the nth chunk is greater than the total number of chunks, then it is the invalid nth chunk request
			return employees;
		}
	}
	
	/** This method returns the date as the 14th day starting from the current date.
	 * 
	 * @return the date of the 14th day
	 */
	public static Date getAfterDate() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, AppConstants.AFTER_DAY);
		return new Date(cal.getTimeInMillis());
	}
}
