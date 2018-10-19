package org.zkoss.reference.developer.mvc.model;

/**
 * @author Hawk
 * 
 */
public class Person {
	private int id;
	private String firstName = "";
	private String lastName = "";
	private boolean married = false;

	public Person() {
	}

	/**
	 * 
	 * @param id
	 * @param firstName
	 * @param lastName
	 */
	public Person(Integer id, String firstName, String lastName) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	/**
	 * 
	 * @param id
	 * @param firstName
	 * @param lastName
	 */
	public Person(Integer id, String firstName, String lastName, boolean married) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.married = married;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public boolean isMarried() {
		return married;
	}

	public void setMarried(boolean married) {
		this.married = married;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getFullName() {
		return firstName + " " + lastName;
	}
}
