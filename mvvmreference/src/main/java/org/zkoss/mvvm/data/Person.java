package org.zkoss.mvvm.data;

import org.hibernate.validator.constraints.*;

import java.util.*;

public class Person {
	private Long id;
	private String name;
	private Integer age;
	private GenderType gender;
	private String email;
	private String icon;
	private Set<CatalogItem> favoriteItems;
	
	public Person() {
	}
	
	public Person(Long id, String name, Integer age, GenderType gender, String email, String icon) {
		super();
		this.id = id;
		this.name = name;
		this.age = age;
		this.gender = gender;
		this.email = email;
		this.icon = icon;
		this.favoriteItems = new LinkedHashSet<CatalogItem>();
	}
	
	public Long getId() {
		return id;
	}
	
	@NotEmpty(message = "can not be empty")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Range(min=0, message="{no-negative}")
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public GenderType getGender() {
		return gender;
	}
	public void setGender(GenderType gender) {
		this.gender = gender;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Set<CatalogItem> getFavoriteItems() {
		return favoriteItems;
	}
}
