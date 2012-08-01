package tutorial;

import java.util.List;

public interface CarService {

	/**
	 * Retrieve all books in the bookstore.
	 * @return all books
	 */
	public List<Car> findAll();
	
	/**
	 * search books according to keyword.
	 * @param keyword book name's keyword
	 * @return list of book that match the keyword
	 */
	public List<Car> search(String keyword);
}
