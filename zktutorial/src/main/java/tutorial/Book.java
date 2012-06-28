package tutorial;

public class Book {

	private Integer id;
	private String name;
	private String thumbnail;
	private String description;
	private Float price;
	
	public Book(){
		super();
	}
	
	public Book(Integer id, String name, String description,  String thumbnail, Float price){
		this.id = id;
		this.name = name;
		this.thumbnail = thumbnail;
		this.description = description;
		this.price = price;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Float getPrice() {
		return price;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
	
	
}
