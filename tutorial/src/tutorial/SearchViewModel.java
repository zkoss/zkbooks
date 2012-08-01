package tutorial;

import java.util.List;
import org.zkoss.bind.annotation.*;

public class SearchViewModel {
	
	private String keyword;
	private List<Car> bookList;
	private Car selectedBook;
	
	private CarService bookService = new CarServiceImpl();
	
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getKeyword() {
		return keyword;
	}

	public List<Car> getBookList(){
		return bookList;
	}
	
	public void setBookList(List<Car> bookList) {
		this.bookList = bookList;
	}
	
	public void setSelectedBook(Car selectedBook) {
		this.selectedBook = selectedBook;
	}
	public Car getSelectedBook() {
		return selectedBook;
	}

	
	@Command
	@NotifyChange("bookList")
	public void search(){
		bookList = bookService.search(keyword);
	}
}
