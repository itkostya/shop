package com.eva.web.views;

import com.eva.core.Product;
import com.eva.service.i.ShopService;
import com.eva.web.tools.Constants;
import com.eva.web.tools.PageContainer;
import com.eva.web.tools.SemaphoreManager;
import com.eva.web.tools.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Controller
@RequestMapping(value = {PageContainer.SHOP_PAGE})
public class ShopController {

    private SemaphoreManager semaphoreManager;  // Max threads available
    private ShopService shopService;
    private AtomicInteger threads;  // Checking threads

    @Autowired
    public ShopController(ShopService shopService) {
        this.threads = new AtomicInteger(0);
        this.semaphoreManager = new SemaphoreManager(Constants.MAX_SHOP_CONTROLLER_THREADS);
        this.shopService = shopService;
    }

    protected ModelAndView doGet() {
        return new ModelAndView(PageContainer.SHOP_PRODUCT_JSP);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = PageContainer.SHOP_PRODUCT_SINGLE_PAGE)
    protected Map<String, Object> productsSingleRequest(@RequestParam(value = "nameFilter") String nameFilter) {
        Map<String, Object> responseMap = new HashMap<>();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        List<Product> products = shopService.getProducts(nameFilter);
        Integer numberOfElements = products.size();

        responseMap.put("numberOfElements", numberOfElements);
        responseMap.put("infoResult", "Time: " + stopWatch.getElapsedTime() + " ms. Products size: " + numberOfElements);

        if (numberOfElements <= Constants.MAX_PRODUCTS_ON_SHOP_PAGE) {
            List<String> productColumns = new LinkedList<>();
            productColumns.add("name");
            productColumns.add("description");
            responseMap.put("productColumns", productColumns);
            responseMap.put("productTable", products);
        }

        return responseMap;
    }

    @RequestMapping(method = RequestMethod.POST, value = PageContainer.SHOP_PRODUCT_MULTI_PAGE)
    protected ModelAndView productsMultiRequest(@RequestParam(value = "nameFilter") String nameFilter,
                                                @RequestParam(value = "threadNumber") Integer threadNumber,
                                                @RequestParam(value = "queryNumber") Integer queryNumber) {
        semaphoreManager.acquire();
        threads.incrementAndGet();

        System.out.println("Parallel queries: " + threads.get());
        System.out.println("thread [" + threadNumber + "] query [" + queryNumber + "] started time: " + System.currentTimeMillis() + " (nameFilter=" + nameFilter + ")");

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        List<Product> products = shopService.getProducts(nameFilter);
        Integer numberOfElements = products.size();

        ModelAndView model = new ModelAndView(PageContainer.CREATE_CHECK_JSP);
        model.addObject("infoResult", "Parallel queries: " + threads.get() + "(processing on server). Thread [" + threadNumber + "] query [" + queryNumber + "] completed");
        model.addObject("numberOfElements", numberOfElements);

//      When responses are too fast we can uncomment this and check how many of them are available in controller
//        if (threadNumber > 0) {
//            try {
//                System.out.println("thread ["+threadNumber+"] query ["+queryNumber+"] sleeping ");
//                TimeUnit.SECONDS.sleep(threadNumber);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println("thread ["+threadNumber+"] query ["+queryNumber+"] working ");
//        }

        System.out.println("thread [" + threadNumber + "] query [" + queryNumber + "] finish time: " + System.currentTimeMillis());

        threads.decrementAndGet();
        semaphoreManager.release();

        return model;
    }
}


