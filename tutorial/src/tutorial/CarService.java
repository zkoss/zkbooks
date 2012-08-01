package tutorial;

import java.util.List;

public interface CarService {

	/**
	 * Retrieve all cars in the catalog.
	 * @return all cars
	 */
	public List<Car> findAll();
	
	/**
	 * search cars according to keyword.
	 * @param keyword car's name keyword
	 * @return list of car that match the keyword
	 */
	public List<Car> search(String keyword);
}
