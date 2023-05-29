package repositories;

import entities.Product;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import utils.HibernateSessionFactory;

import java.util.List;

public class ProductRepo {
    public static String save(Product product) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(product);
            transaction.commit();
        } catch (Exception e) {
            return "Some error " + e.getMessage();
        }
        return "Product has been successfully saved to the DB\n" + product;
    }

    public static String delete(long id) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Product product = session.get(Product.class, id);
            session.remove(product);
            transaction.commit();
        } catch (Exception e) {
            return "Deletion failed";
        }
        return "Product has been successfully deleted from the DB";
    }

    public static List<Product> getAll() {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            return session.createQuery("from Product", Product.class).list();
        }
    }

    public static Product getById(long id) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            return session.get(Product.class, id);
        }
    }

    public static List<Product> getFromCategory(Product.Category category) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            Query<Product> query = session.createQuery("from Product where category = :category", Product.class);
            return query.setParameter("category", category).list();
        }
    }
}
