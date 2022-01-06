package hiber.dao;

import hiber.model.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImp implements UserDao {
    Session session = null;
    Transaction transaction = null;
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void add(User user) {
        sessionFactory.getCurrentSession().save(user);
    }

    @Override
    public User getUser(String model, int series) {
//        String sql = "SELECT id FROM Car Where model = :paramModel and series = :paramSeries";
//        Query query = sessionFactory.getCurrentSession().createQuery(sql)
//                .setParameter("paramModel", model)
//                .setParameter("paramSeries", series);
//        int id = query.getFirstResult();
//
//        String sql2 = "SELECT User FROM User where id = :paramID";
//        Query query2 = sessionFactory.getCurrentSession().createQuery(sql2)
//                .setParameter("paramID", id);
//        List<User> users = query2.getResultList();
//        String hql = "SELECT User FROM Car c INNER JOIN User u ON c.id = u.id WHERE c.model = :paramModel AND c.series = :paramSeries";

        String hql = "FROM User u LEFT OUTER JOIN FETCH u.car WHERE u.car.model = :paramModel AND u.car.series = :paramSeries";
        User user = (User) sessionFactory.getCurrentSession().createQuery(hql)
                .setParameter("paramModel", model)
                .setParameter("paramSeries", series).uniqueResult();
        return user;
    }

    @Override
    public void cleanTables() {
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            String sql1 = "DELETE FROM User";
            String sql2 = "DELETE FROM Car";
            session.createQuery(sql1).executeUpdate();
            session.createQuery(sql2).executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> listUsers() {
        TypedQuery<User> query = sessionFactory.getCurrentSession().createQuery("from User");
        return query.getResultList();
    }
}
