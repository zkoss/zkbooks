package org.zkoss.reference.developer.spring.security.model;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 
 * @author Ian YT Tsai (zanyking)
 *
 */
public class Article {
	private long id = -1L;
	private String title;
	private String content;
	private String author;
	private Date lastModifiedDate;
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	public Article() {}
	
	/**
	 * 
	 * @param title
	 * @param content
	 * @param author
	 * @param lastModifiedDate
	 */
	public Article(String title, String content, String author, Date lastModifiedDate) {
		this.title = title;
		this.content = content;
		this.author = author;
		this.lastModifiedDate = lastModifiedDate;
	}


	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getLastModifiedDateString(){
		return dateFormat.format(lastModifiedDate);
	}
	public String toString() {
		return "Article [id=" + id + ", title=" + title + ", content="
				+ content + ", author=" + author + "]";
	}
	
	
}
