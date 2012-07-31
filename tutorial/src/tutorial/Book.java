package tutorial;

public class Book {

	private Integer id;
	private String name;
	private String priview;
	private String description;
	private Float price;
	
	public Book(){
	}
	
	public Book(Integer id, String name, String description,  String preview, Float price){
		this.id = id;
		this.name = name;
		this.priview = preview;
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
	public String getPreview() {
		return priview;
	}
	public void setPreview(String preview) {
		this.priview = preview;
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
