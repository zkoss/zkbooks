package tutorial;

import java.util.List;

public interface BookService {

	public List<Book> findAll();
	
	public List<Book> search(String keyword);
}
