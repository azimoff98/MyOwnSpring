package main.com.azimoff;

import main.org.springframework.beans.factory.BeanFactory;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException, IOException, URISyntaxException {

        BeanFactory beanFactory = new BeanFactory();

        beanFactory.instantiate("main.com.azimoff");

    }
}
