package org.zkoss.mvvm.data;

import java.math.BigDecimal;

public class CatalogItem {
	private Long id;
	private String label;
	private String articleNum;
	private String image;
	private BigDecimal price;

	public CatalogItem() {
	}
	
	public CatalogItem(Long id, String label, String articleNum, String image, BigDecimal price) {
		super();
		this.id = id;
		this.label = label;
		this.articleNum = articleNum;
		this.image = image;
		this.price = price;
	}
	
	public Long getId() {
		return id;
	}
	public String getLabel() {
		return label;
	}
	public String getArticleNum() {
		return articleNum;
	}
	public String getImage() {
		return image;
	}
	public BigDecimal getPrice() {
		return price;
	}
}
