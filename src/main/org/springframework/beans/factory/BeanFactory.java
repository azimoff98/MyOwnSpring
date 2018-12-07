package main.org.springframework.beans.factory;

import main.javax.annotation.PostConstruct;
import main.javax.annotation.PreDestroy;
import main.org.springframework.beans.factory.annotation.Autowired;
import main.org.springframework.beans.factory.config.BeanPostProcessor;
import main.org.springframework.beans.factory.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class BeanFactory {

    private Map<String, Object> singletons = new HashMap<>();


    private List<BeanPostProcessor> postProcessors = new ArrayList<>();

    public void addPostProcessor(BeanPostProcessor postProcessor){
        postProcessors.add(postProcessor);
    }

    public Object getBean(String beanName){
        return singletons.get(beanName);
    }

    public void instantiate(String basePackage) {

        try {

            ClassLoader classLoader = ClassLoader.getSystemClassLoader();

            String path = basePackage.replace('.', '/');
            Enumeration<URL> resources = classLoader.getResources(path);

            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();

                File file = new File(resource.toURI());

                for (File classFile : file.listFiles()) {
                    String fileName = classFile.getName();

                    System.out.println(fileName);

                    if (fileName.endsWith(".class")) {
                        String className = fileName.substring(0, fileName.lastIndexOf("."));

                        Class classObject = Class.forName(basePackage + "." + className);

                        if (classObject.isAnnotationPresent(Component.class)) {
                            System.out.println("Component: " + classObject);

                            Object instance = classObject.newInstance();
                            String beanName = className.substring(0, 1).toLowerCase() + className.substring(1);
                            singletons.put(beanName, instance);

                        }
                    }
                }

            }
        }catch (IOException | URISyntaxException | IllegalAccessException| ClassNotFoundException | IllegalStateException | InstantiationException e){
            e.printStackTrace();
        }

    }

    public void populateProperties() {
        System.out.println("===populateProperties===");
        try {

            for (Object object : singletons.values()) {
                for (Field field : object.getClass().getDeclaredFields()) {
                    if (field.isAnnotationPresent(Autowired.class)) {
                        for (Object dependency : singletons.values()) {
                            if (dependency.getClass().equals(field.getType())) {
                                String setterName = "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                                System.out.println("Setter name = " + setterName);
                                Method setter = object.getClass().getMethod(setterName, dependency.getClass());
                                setter.invoke(object, dependency);
                            }
                        }

                    }
                }
            }


        }catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
            e.printStackTrace();
        }

    }

    public void injectBeanNames(){
        for (String name : singletons.keySet()){
            Object bean = singletons.get(name);
            if(bean instanceof BeanNameAware){
                ((BeanNameAware) bean).setBeanName(name);
            }
        }
    }

    public void initializeBeans(){

        for(String name : singletons.keySet()){
            Object bean = singletons.get(name);

            for(BeanPostProcessor postProcessor : postProcessors){
                postProcessor.postProcessBeforeInitialization(bean, name);
            }

            if(bean instanceof InitializingBean){
                ((InitializingBean) bean).afterPropertiesSet();
            }

            for(BeanPostProcessor postProcessor : postProcessors){
                postProcessor.postProcessAfterInitialization(bean, name);
            }
        }
    }

    public void postConstruct(){

        for(Object bean : singletons.values()){
            for(Method method : bean.getClass().getMethods()){
                if(method.isAnnotationPresent(PostConstruct.class)){
                    try{
                        method.invoke(bean);
                    }catch (IllegalAccessException | InvocationTargetException e){
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    public void close(){
        for(Object bean : singletons.values()){
            for(Method method : bean.getClass().getMethods()){
                if(method.isAnnotationPresent(PreDestroy.class)){
                    try{
                        method.invoke(bean);
                    }catch (IllegalAccessException | InvocationTargetException e){
                        e.printStackTrace();
                    }
                }
            }

            if(bean instanceof  DisposableBean){
                ((DisposableBean) bean).destroy();
            }
        }


    }

    public Map<String, Object> getSingletons() {
        return singletons;
    }

    public void setSingletons(Map<String, Object> singletons) {
        this.singletons = singletons;
    }

    public List<BeanPostProcessor> getPostProcessors() {
        return postProcessors;
    }

    public void setPostProcessors(List<BeanPostProcessor> postProcessors) {
        this.postProcessors = postProcessors;
    }
}
