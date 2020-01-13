package test.testShopService;

import com.eva.core.Product;
import com.eva.service.i.ShopService;
import com.eva.service.impl.ShopServiceImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class TestShopService {

    private ShopService shopService;

    public TestShopService() {
        this.shopService = new ShopServiceImpl();
    }

    @Test
    public void checkGetProductsEmpty() {
        List<Product> productList = new LinkedList<>();
        shopService.setProducts(productList);
        List<Product> resultList = shopService.getProducts("^E.*$");
        Assert.assertEquals(resultList.size(), 0);
    }

    @Test
    public void checkGetProductsAvailableBeginning() {
        List<Product> productList = new LinkedList<>();
        productList.add(new Product("Korob", ""));
        productList.add(new Product("Zoo", ""));
        productList.add(new Product("Hello", ""));
        shopService.setProducts(productList);

        List<Product> resultList = shopService.getProducts("^E.*$");
        Assert.assertEquals(resultList.size(), 3);
    }

    @Test
    public void checkGetProductsNotAvailableBeginning() {
        List<Product> productList = new LinkedList<>();
        productList.add(new Product("EAST", ""));
        productList.add(new Product("E", ""));
        productList.add(new Product("Error", ""));
        shopService.setProducts(productList);

        List<Product> resultList = shopService.getProducts("^E.*$");
        Assert.assertEquals(resultList.size(), 0);
    }

    @Test
    public void checkGetProductsNotAvailableMiddle() {
        List<Product> productList = new LinkedList<>();
        productList.add(new Product("Hector", ""));
        productList.add(new Product("v5n", ""));
        productList.add(new Product("ava", ""));
        shopService.setProducts(productList);

        List<Product> resultList = shopService.getProducts("^.*[eva].*$");
        Assert.assertEquals(resultList.size(), 0);
    }

    @Test
    public void checkGetProductsAvailableSub() {
        List<Product> productList = new LinkedList<>();
        productList.add(new Product("Thor", ""));
        productList.add(new Product("Prikol", ""));
        productList.add(new Product("Cool", ""));
        productList.add(new Product("Hector", ""));
        productList.add(new Product("v5n", ""));
        productList.add(new Product("ava", ""));
        shopService.setProducts(productList);

        List<Product> resultList = shopService.getProducts("^.*[eva].*$");
        Assert.assertEquals(resultList.size(), 3);

        // Check elements in this list
        List<Product> expectedList = new LinkedList<>();
        expectedList.add(new Product("Thor", ""));
        expectedList.add(new Product("Prikol", ""));
        expectedList.add(new Product("Cool", ""));

        Assert.assertEquals(expectedList, resultList);
    }

}
