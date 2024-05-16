package org.zkoss.reference.developer.responsiveDesign;

public class DataEntry {

	private String userId;
	private String firstName;
	private int age;
	private String position;
	private String department;
	private String deskNumber;
	
	public DataEntry(String userId, String firstName, int age, String position, String department,
			String deskNumber) {
		this.userId = userId;
		this.firstName = firstName;
		this.age = age;
		this.position = position;
		this.department = department;
		this.deskNumber = deskNumber;
	}

	public String getUserId() {
		return userId;
	}
	public String getFirstName() {
		return firstName;
	}
	public int getAge() {
		return age;
	}
	public String getPosition() {
		return position;
	}
	public String getDepartment() {
		return department;
	}
	public String getDeskNumber() {
		return deskNumber;
	}
	
}