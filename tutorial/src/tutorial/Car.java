package tutorial;

public class Car {

	private Integer id;
	private String name;
	private String priview;
	private String description;
	private Integer price;
	
	public Car(){
	}
	
	public Car(Integer id, String name, String description,  String preview, Integer price){
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
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	
	
}
