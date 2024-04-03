package org.zkoss.reference.developer.responsive;

import java.math.BigDecimal;

public class Employee {
	private String firstName;
	private String lastName;
	private String position;
	private String office;
	private int age;
	private String startDate;
	private Integer salary;
	private int extension;
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getOffice() {
		return office;
	}
	public void setOffice(String office) {
		this.office = office;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public Integer getSalary() {
		return salary;
	}
	public void setSalary(Integer salary) {
		this.salary = salary;
	}
	public int getExtension() {
		return extension;
	}
	public void setExtension(int extension) {
		this.extension = extension;
	}
	
	public Employee(String firstName, String lastName, String position, String office, int age, String startDate,
                    Integer salary, int extension) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.position = position;
		this.office = office;
		this.age = age;
		this.startDate = startDate;
		this.salary = salary;
		this.extension = extension;
	}
}
