package repositories;

import entities.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import utils.HibernateSessionFactory;

import java.util.List;

public class CriteriaTestRepo {

    /* 3 основных правила:
        - Ключевые операторы типа SELECT, FROM, WHERE вызываются у объекта CriteriaQuery.
        - Вспомогательные операторы типа AND, OR, DESC вызываются у объекта CriteriaBuilder.
        - Имена полей берутся через get() у объекта Root.
     */
    public static List<User> getAllUsers() {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);

            // Root<User> root = criteriaQuery.from(User.class);
            criteriaQuery.select(criteriaQuery.from(User.class));

            Query<User> query = session.createQuery(criteriaQuery);
            return query.getResultList();
        }
    }

    public static User getUserById(long id) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
            Root<User> root = criteriaQuery.from(User.class);
            criteriaQuery.select(root);
            criteriaQuery.where(builder.equal(root.get("id"), id));

            Query<User> query = session.createQuery(criteriaQuery);
            return query.getSingleResultOrNull();
        }
    }

    public static User changeUsername(long id, String name) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaUpdate<User> criteriaUpdate = builder.createCriteriaUpdate(User.class);
            Root<User> root = criteriaUpdate.from(User.class);
            criteriaUpdate.set("username", name);
            criteriaUpdate.where(builder.equal(root.get("id"), id));
            Transaction transaction = session.beginTransaction();
            session.createMutationQuery(criteriaUpdate).executeUpdate();
            transaction.commit();
            return session.get(User.class, id);
        }
    }
}
