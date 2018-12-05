package main.com.azimoff;

import main.org.springframework.beans.factory.BeanNameAware;
import main.org.springframework.beans.factory.stereotype.Component;

@Component
public class PromotionService  implements BeanNameAware {

    private String beanName;

    @Override
    public void setBeanName(String name) {
        beanName = name;
    }


    public String getBeanName(){
        return beanName;
    }

}
