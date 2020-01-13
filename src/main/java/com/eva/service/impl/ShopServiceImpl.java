package com.eva.service.impl;

import com.eva.core.Product;
import com.eva.dao.i.ProductDao;
import com.eva.service.i.ShopService;

import java.util.ArrayList;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShopServiceImpl implements ShopService {

    private ProductDao productDao;
    private List<Product> products;

    // Getters, setters begin

    public ProductDao getProductDao() {
        return productDao;
    }

    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    // Getters, setters end

    public void init() {
        setProducts(productDao.getProducts());
    }

    public List<Product> getProducts(String regExp) {
        Pattern pattern = Pattern.compile(regExp);
        List<Product> resultProducts = new ArrayList<>();

        for (Product product : products) {
            Matcher matcher = pattern.matcher(product.getName());
            if (!matcher.matches()) {
                resultProducts.add(product);
            }
        }

        return resultProducts;
    }

}
