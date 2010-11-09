package demo.web;

import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import demo.model.UserDAO;
import demo.model.bean.User;

/**
 * @author zkessentials store
 * 
 *         This class provides a class which manages user authentication
 * 
 */
public class UserCredentialManager {

	private static final String KEY_USER_MODEL = UserCredentialManager.class
			.getName()
			+ "_MODEL";
	private UserDAO userDAO;
	private User user;

	private UserCredentialManager() {
		userDAO = new UserDAO();
	}

	public static UserCredentialManager getIntance() {
		return getIntance(Sessions.getCurrent());
	}

	public static UserCredentialManager getIntance(Session zkSession) {
		synchronized (zkSession) {
			UserCredentialManager userModel = (UserCredentialManager) zkSession
					.getAttribute(KEY_USER_MODEL);
			if (userModel == null) {
				zkSession.setAttribute(KEY_USER_MODEL,
						userModel = new UserCredentialManager());
			}
			return userModel;
		}
	}

	public synchronized void login(String name, String password) {
		User tempUser = userDAO.findUserByName(name);
		if (tempUser != null && tempUser.getPassword().equals(password)) {
			user = tempUser;
		} else {
			user = null;
		}
	}

	public synchronized void logOff() {
		this.user = null;
	}

	public synchronized User getUser() {
		return user;
	}

	public synchronized boolean isAuthenticated() {
		return user != null;
	}

}
