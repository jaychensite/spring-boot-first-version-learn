package org.springframework.beans.extend.json;

import org.springframework.beans.extend.json.applicationcontext.ClassPathJsonApplicationContext;
import org.springframework.simple.TestService;
import org.springframework.simple.byconstructor.TestControllerByConstructor;

public class BootStrap {
    public static void main(String[] args) {
        ClassPathJsonApplicationContext context = new ClassPathJsonApplicationContext("beanDefinition.json");
        TestControllerByConstructor bean = context.getBean(TestControllerByConstructor.class);
        TestService testServiceBean = context.getBean(TestService.class);
        assert bean.getTestService() == testServiceBean;
        System.out.println("testService in TestControllerByConstructor" + bean.getTestService());
        System.out.println(testServiceBean);
    }
}
