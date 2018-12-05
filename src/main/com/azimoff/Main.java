package main.com.azimoff;

import main.org.springframework.beans.factory.BeanFactory;
import main.org.springframework.beans.factory.config.CustomPostProcessor;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException, IOException, URISyntaxException {

        BeanFactory beanFactory = new BeanFactory();
        beanFactory.addPostProcessor(new CustomPostProcessor());
        beanFactory.instantiate("main.com.azimoff");
        beanFactory.populateProperties();
        beanFactory.injectBeanNames();
        beanFactory.initializeBeans();

        ProductService productService = (ProductService) beanFactory.getBean("productService");
        System.out.println(productService);

        PromotionService promotionService = productService.getPromotionService();
        System.out.println(promotionService);
        System.out.println("Bean name = " + promotionService.getBeanName());

    }
}
