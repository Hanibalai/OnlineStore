package repositories;

import entities.Order;
import entities.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import utils.HibernateSessionFactory;
import java.util.List;

public class UserRepo {

    public static String save(User user) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
        } catch (ConstraintViolationException e) {
            throw new RuntimeException("User with the same ID already exists in the DB");
        }
        return "User has been successfully saved to the DB\n" + user;
    }

    public static String delete(long id) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            session.remove(user);
            transaction.commit();
        } catch (Exception e) {
            return "Deletion failed";
        }
        return "User has been successfully deleted from the DB";
    }

    public static List<User> getAll() {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            return session.createQuery("from Customer", User.class).list();
        }
    }

    public static User getById(long id) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            return session.get(User.class, id);
        }
    }

    public static List<Order> getOrders(long id) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        List<Order> orders = session.get(User.class, id).getOrders();
        transaction.commit();
        closeSession();
        return orders;
    }

    public static String addOrder(long id, Order order) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            order.setUser(user);
            session.persist(order);
            user.getOrders().add(order);
            session.merge(user);
            transaction.commit();
        } catch (Exception e) {
            return "Addition error: " + e.getMessage();
        }
        return "Product has been successfully added\n" + order;
    }

    private static void closeSession() {
        HibernateSessionFactory.getSessionFactory().getCurrentSession().close();
    }
}
