package org.springframework.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.annotation.Teacher;
import org.springframework.test.assignable.TestInterface;
import org.springframework.test.defaultfilters.TestController;

public class BootStrap {

    public static void main(String[] args) {
//        testDefaultFilter();
//        testAnnotationFilter();
        testAssignable();
//        testAspectj();
//        testCustom();
    }


    public static void testDefaultFilter() {
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("classpath:component-scan-default-filter.xml");
        TestController bean = context.getBean(TestController.class);
        System.out.println(bean);

    }
    public static void testAnnotationFilter() {
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("classpath:component-scan-annotation-filter.xml");
        Teacher bean = context.getBean(Teacher.class);
        System.out.println(bean);

    }

    static void testAssignable() {
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("classpath:component-scan-assignable-filter.xml");
        TestInterface bean = context.getBean(TestInterface.class);
        System.out.println(bean);
    }

    static void testAspectj() {
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext(
                        "classpath:component-scan-aspectj-filter.xml");
        TestInterface bean = context.getBean(TestInterface.class);
        System.out.println(bean);
    }


    static void testCustom() {
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext(
                        "classpath:component-scan-custom-filter.xml");
        String[] beanDefinitionNames = context.getBeanDefinitionNames();

        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println(beanDefinitionName);
        }
    }
}
