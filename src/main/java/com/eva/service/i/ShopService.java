package com.eva.service.i;

import com.eva.core.Product;

import java.util.List;

public interface ShopService {
    List<Product> getProducts(String regularExpression);

    void setProducts(List<Product> products);

    void init();
}
