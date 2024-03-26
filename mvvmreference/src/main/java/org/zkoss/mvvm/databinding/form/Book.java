package org.zkoss.mvvm.databinding.form;

import java.util.*;

public class Book {
	private Set<Category> categories = new LinkedHashSet<Category>();
	private String author;
	private String name;
	
//	public Book() {}
	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}
	public Set<Category> getCategories() {
		return categories;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getAuthor() {
		return author;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
}
