package tutorial;

import java.util.List;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

public class SearchViewModel {

	private BookService bookService = new BookServiceImpl();
	
	private List<Book> bookList;
	
	private String keyword;
	
	private Book selectedBook;
	
	@Init
	public void init(){
		bookList = bookService.findAll();
	}
	
	public List<Book> getBookList(){
		return bookList;
	}
	
	@NotifyChange("bookList")
	@Command
	public void search(){
		bookList = bookService.search(keyword);
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Book getSelectedBook() {
		return selectedBook;
	}

	public void setSelectedBook(Book selectedBook) {
		this.selectedBook = selectedBook;
	}
}
