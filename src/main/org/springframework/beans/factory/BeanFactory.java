package main.org.springframework.beans.factory;

import main.org.springframework.beans.factory.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class BeanFactory {

    private Map<String, Object> singletons = new HashMap<>();


    public void instantiate(String basePackage) throws IOException, URISyntaxException, ClassNotFoundException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        String path = basePackage.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);

        while(resources.hasMoreElements()){
            URL resource = resources.nextElement();

            File file = new File(resource.toURI());

            for(File clasFile : file.listFiles()){
                String fileName = clasFile.getName();

                System.out.println(fileName);

                if(fileName.endsWith(".class")){
                    String className = fileName.substring(0, fileName.lastIndexOf("."));

                    Class classObject = Class.forName(basePackage + "." + className);

                    if(classObject.isAnnotationPresent(Component.class)){
                        System.out.println("Component: " + classObject);
                    }
                }
            }

        }

    }


    public Object getObject(String beanName){
        return singletons.get(beanName);
    }

}
