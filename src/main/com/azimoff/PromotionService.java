package main.com.azimoff;

import main.org.springframework.beans.factory.BeanNameAware;
import main.org.springframework.beans.factory.stereotype.Component;
import main.org.springframework.context.ApplicationListener;
import main.org.springframework.context.event.ContextClosedEvent;

@Component
public class PromotionService  implements BeanNameAware, ApplicationListener<ContextClosedEvent> {

    private String beanName;

    @Override
    public void setBeanName(String name) {
        beanName = name;
    }


    public String getBeanName(){
        return beanName;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        System.out.println(">>> ContextClosed EVENT");
    }
}
