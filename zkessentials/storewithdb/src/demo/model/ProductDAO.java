package demo.model;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import demo.model.bean.Product;

/**
 * @author zkessentials store
 * 
 *         This class provides functionality to access the {@code Product} model
 *         storage system
 * 
 */
public class ProductDAO {

	@SuppressWarnings("unchecked")
	public List<Product> findAll() {
		Session session = StoreHibernateUtil.openSession();
        Query query = session.createQuery("from products");
        List<Product> products = query.list();

        session.close();
        return products;
	}

	@SuppressWarnings("unchecked")
	public List<Product> findAllAvailable() {
		Session session = StoreHibernateUtil.openSession();
        Transaction t = session.beginTransaction();

        Criteria criteria = session.createCriteria(Product.class).add(Restrictions.eq("available", true));
        List<Product> products = criteria.list();
        t.commit();
        session.close();

        return products;
	}

	public Product remove(long productId) {
		Session session = StoreHibernateUtil.openSession();
        Transaction t = session.beginTransaction();
        Criteria criteria = session.createCriteria(Product.class).add(Restrictions.eq("id", productId));
        Product product = (Product)criteria.uniqueResult();

        if(product != null) {
            product.setAvailable(false);
            session.update(product);
        }

        t.commit();
        session.close();

        return product;
	}

	public Product putOn(long productId) {
		Session session = StoreHibernateUtil.openSession();
        Transaction t = session.beginTransaction();
        Criteria criteria = session.createCriteria(Product.class).add(Restrictions.eq("id", productId));
        Product product = (Product)criteria.uniqueResult();

        if(product != null) {
            product.setAvailable(true);
            session.update(product);
        }

        t.commit();
        session.close();

        return product;
	}

}
