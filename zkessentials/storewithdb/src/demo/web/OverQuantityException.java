package demo.web;

/**
 * @author zkessentials store
 * 
 *         This class provides an exception class for when a user wants to buy
 *         items over the quantity
 * 
 */
public class OverQuantityException extends Exception {

	private static final long serialVersionUID = -2360503874317276337L;

	public OverQuantityException(String errMesg) {
		super(errMesg);
	}

}
