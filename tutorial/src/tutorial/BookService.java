package tutorial;

import java.util.List;

public interface BookService {

	/**
	 * Retrieve all books in the bookstore.
	 * @return all books
	 */
	public List<Book> findAll();
	
	/**
	 * search books according to keyword.
	 * @param keyword book name's keyword
	 * @return list of book that match the keyword
	 */
	public List<Book> search(String keyword);
}
