package entities;

import org.hibernate.CacheMode;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.Test;
import utils.HibernateSessionFactory;

import javax.swing.text.html.parser.Entity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserTest {

    @Test
    public void detachChildEntityTest() {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            User user = session.get(User.class, 4);
            Order order = user.getOrders().get(0);
            session.flush();
            assertThat(session.contains(user)).isTrue();
            assertThat(session.contains(order)).isTrue();
            session.detach(user);
            assertThat(session.contains(user)).isFalse();
            assertThat(session.contains(order)).isFalse();
            transaction.commit();
        }
    }

    @Test
    public void firstLevelCache() {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            Product product1 = session.get(Product.class, 1);
            Product product2 = session.get(Product.class, 1);
            assertThat(product1 == product2).isTrue();
        }
    }

    @Test
    public void secondLevelCacheOff() {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        User user1 = session.get(User.class, 5);
        session.close();
        session = HibernateSessionFactory.getSessionFactory().openSession();
        User user2 = session.get(User.class, 5);
        session.close();
        assertThat(user1 == user2).isFalse();
        assertThat(user1.equals(user2)).isTrue();
    }

    @Test
    public void secondLevelCacheOn() {
        TestEntity test1;
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            test1 = session.find(TestEntity.class, 1);
        }
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            assertThat(session.getSessionFactory().getCache().containsEntity(TestEntity.class, 1)).isTrue();
        }
    }

    // should be one request to the db
    @Test
    public void queryLevelCache() {
        TestEntity entity1;
        TestEntity entity2;
        Query<TestEntity> query;
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            query = session.createQuery("from Test_Entity where id = 1", TestEntity.class);
            query.setCacheable(true);
            entity1 = query.getSingleResult();
        }
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            query = session.createQuery("from Test_Entity where id = 1", TestEntity.class);
            query.setCacheable(true);
            entity2 = query.getSingleResult();
        }
        System.out.println(entity1.equals(entity2));
        System.out.println(entity1 == entity2);
    }


    @Test
    public void cleanCacheManually() {
        TestEntity test1;
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            test1 = session.find(TestEntity.class, 1);
        }
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            assertThat(session.getSessionFactory().getCache().containsEntity(TestEntity.class, 1)).isTrue();
            session.getSessionFactory().getCache().evictQueryRegion("entities.TestEntity");
            assertThat(session.getSessionFactory().getCache().containsEntity(TestEntity.class, 1L)).isFalse();
            test1 = session.get(TestEntity.class, 1);
            session.getSessionFactory().getCache().evictEntityData(TestEntity.class, 1L);
            assertThat(session.getSessionFactory().getCache().containsEntity(TestEntity.class, 1)).isFalse();
        }
    }
}