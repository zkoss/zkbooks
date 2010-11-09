package demo.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import demo.model.bean.User;

/**
 * @author zkessentials store
 * 
 *         This class provides functionality to access the {@code User} model
 *         storage system
 * 
 */
public class UserDAO {
	private static final Map<Long, User> dbModel = Collections
			.synchronizedMap(new HashMap<Long, User>());

	public static final User USER1 = new User(1L, "ian", "ian", "user");
	public static final User USER2 = new User(2L, "zk", "zk", "admin");
	public static final User USER3 = new User(3L, "tom", "tom", "user");

	static {
		dbModel.put(USER1.getId(), USER1);
		dbModel.put(USER2.getId(), USER2);
		dbModel.put(USER3.getId(), USER3);
	}

	public List<User> findAll() {
		return new ArrayList<User>(dbModel.values());
	}

	public User findById(long id) {
		return dbModel.get(id);
	}

	public User findUserByName(String name) {
		for (User user : dbModel.values()) {
			if (user.getName().equals(name)) {
				return user;
			}
		}
		return null;
	}

}
