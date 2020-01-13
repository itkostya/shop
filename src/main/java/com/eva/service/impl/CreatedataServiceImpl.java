package com.eva.service.impl;

import com.eva.core.Product;
import com.eva.dao.i.ProductDao;
import com.eva.service.i.CreatedataService;

public class CreatedataServiceImpl implements CreatedataService {

    private ProductDao productDao;

    // Getters, setters begin

    public ProductDao getProductDao() {
        return productDao;
    }

    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

    // Getters, setters end

    public void createProducts(Long numberOfProducts) {
        for (Long i = 0L; i < numberOfProducts; i++) {
            Product product = new Product("name " + i.toString(), "description " + i.toString());
            productDao.save(product);
        }
    }
}
