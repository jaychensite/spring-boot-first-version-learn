package org.springframework.beans.extend.properties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.extend.properties.applicationcontext.ClassPathPropertyFileApplicationContext;
import org.springframework.simple.Employee;

import java.util.Map;

@Slf4j
public class BootStrap {
    public static void main(String[] args) {
        ClassPathPropertyFileApplicationContext context = new ClassPathPropertyFileApplicationContext("beanDefinition.properties");
        Map<String, Employee> beansOfType = context.getBeansOfType(Employee.class);
        for (Map.Entry<String, Employee> entry : beansOfType.entrySet()) {
            log.info("bean name:{},bean:{}",entry.getKey(),entry.getValue());
        }

    }
}
