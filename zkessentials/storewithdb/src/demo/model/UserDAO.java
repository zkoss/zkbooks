package demo.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

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

	@SuppressWarnings("unchecked")
	public List<User> findAll() {
		Session session = StoreHibernateUtil.openSession();
        Query query = session.createQuery("from users");
        List<User> users = query.list();

        session.close();
        return users;
	}

	public User findById(long id) {
		Session session = StoreHibernateUtil.openSession();
        Transaction t = session.beginTransaction();
        Criteria criteria = session.createCriteria(User.class).add(Restrictions.eq("id", id));
        User user = (User)criteria.uniqueResult();
        t.commit();
        session.close();

        return user;
	}

	public User findUserByName(String name) {
		Session session = StoreHibernateUtil.openSession();
        Transaction t = session.beginTransaction();
        Criteria criteria = session.createCriteria(User.class).add(Restrictions.eq("name", name));
        User user = (User)criteria.uniqueResult();
        t.commit();
        session.close();

        return user;
	}

}
