package tutorial;

import java.util.List;
import org.zkoss.bind.annotation.*;

public class SearchViewModel {

	private BookService bookService = new BookServiceImpl();
	
	private String keyword;
	private List<Book> bookList;
	private Book selectedBook;
	
	
	
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getKeyword() {
		return keyword;
	}

	public List<Book> getBookList(){
		return bookList;
	}
	
	public void setBookList(List<Book> bookList) {
		this.bookList = bookList;
	}
	
	public void setSelectedBook(Book selectedBook) {
		this.selectedBook = selectedBook;
	}
	public Book getSelectedBook() {
		return selectedBook;
	}

	
	@NotifyChange("bookList")
	@Command
	public void search(){
		bookList = bookService.search(keyword);
	}
}
