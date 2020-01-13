package com.eva.dao.i;

import com.eva.core.Product;
import com.eva.dao.HibernateDAO;

import java.util.List;

public interface ProductDao extends HibernateDAO<Product> {
    List<Product> getProducts();
}
