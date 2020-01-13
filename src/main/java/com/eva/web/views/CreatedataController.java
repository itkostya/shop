package com.eva.web.views;

import com.eva.service.i.CreatedataService;
import com.eva.service.i.ShopService;
import com.eva.web.tools.PageContainer;
import com.eva.web.tools.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = {PageContainer.CREATE_CHECK_PAGE})
public class CreatedataController {

    private CreatedataService createdataService;
    private ShopService shopService;

    @Autowired
    public CreatedataController(CreatedataService createdataService, ShopService shopService) {
        this.createdataService = createdataService;
        this.shopService = shopService;
    }

    @RequestMapping(method = RequestMethod.GET)
    protected ModelAndView doGet() {
        return new ModelAndView(PageContainer.CREATE_CHECK_JSP);
    }

    @RequestMapping(method = RequestMethod.POST, value = PageContainer.CREATE)
    protected ModelAndView postCreateProducts(@RequestParam Long numberOfElements) {

        ModelAndView model = new ModelAndView(PageContainer.CREATE_CHECK_JSP);
        // Create products
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        createdataService.createProducts(numberOfElements);
        StringBuffer infoResult = new StringBuffer("Created " + numberOfElements + " elements: " + stopWatch.getElapsedTimeSecs() + " sec. ");
        // Init product table
        stopWatch.start();
        shopService.init();
        infoResult.append("Init product table: ").append(stopWatch.getElapsedTimeSecs()).append(" sec.");

        model.addObject("infoResult", infoResult);

        return model;
    }
}
