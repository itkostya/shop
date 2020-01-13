package com.eva.dao.impl;

import com.eva.core.Product;
import com.eva.dao.i.ProductDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class ProductDaoImpl implements ProductDao {

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void save(Product product) {
        sessionFactory.getCurrentSession().save(product);
    }

    public void update(Product product) {
        sessionFactory.getCurrentSession().update(product);
    }

    public void delete(Product product) {
        sessionFactory.getCurrentSession().delete(product);
    }

    public List<Product> getProducts() {
        Session session = sessionFactory.openSession();
        Query<Product> query = session.createQuery("from Product", Product.class);
        List<Product> list = query.getResultList();
        session.close();
        return list;
    }
}
