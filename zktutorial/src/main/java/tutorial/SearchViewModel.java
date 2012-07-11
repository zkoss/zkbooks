package tutorial;

import java.util.List;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

public class SearchViewModel {

	private BookService bookService = new BookServiceImpl();
	
	private List<Book> bookList;
	
	private String keyword;
	
	private Book selectedBook;
	
	
	public List<Book> getBookList(){
		return bookList;
	}
	
	@NotifyChange("bookList")
	@Command
	public void search(){
		bookList = bookService.search(keyword);
		for (Book b: bookList){
			System.out.println(b.getName());
		}
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
		System.out.println(keyword);
	}


	public void setSelectedBook(Book selectedBook) {
		this.selectedBook = selectedBook;
	}
	public Book getSelectedBook() {
		return selectedBook;
	}
	
//	@Init
//	public void init(){
//		bookList = bookService.findAll();
//	}
	
//	public String getKeyword() {
//		return keyword;
//	}
}
