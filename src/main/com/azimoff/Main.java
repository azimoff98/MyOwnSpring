package main.com.azimoff;


import main.org.springframework.beans.factory.BeanFactory;
import main.org.springframework.beans.factory.config.CustomPostProcessor;
import main.org.springframework.context.ApplicationContext;



public class Main {

    public static void main(String[] args) {
      new Main();
    }

    public Main(){
        try{
            testContext();
        }catch (ReflectiveOperationException e){
            e.printStackTrace();
        }
    }

    void testBeanFactory() throws ReflectiveOperationException{
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

        System.out.println(promotionService.getClass());

        beanFactory.close();

    }

    void testContext() throws ReflectiveOperationException {
        ApplicationContext applicationContext = new ApplicationContext("main.com.azimoff");
        applicationContext.close();
    }
}
