package org.springframework.simple;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.*;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;

/**
 * desc:
 *
 * @author : caokunliang
 * creat_date: 2019/12/19 0019
 * creat_time: 12:55
 **/
@Slf4j
public class ManualRegisterBeanDefinitionDemo {
    public static void main(String[] args) {
        wireDependencyByConstructor();
    }

    /**
     * 通过构造器的方式来注入依赖
     */
    private static void wireDependencyByConstructor() {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        GenericBeanDefinition iTestServiceBeanDefinition = (GenericBeanDefinition) BeanDefinitionBuilder.genericBeanDefinition(TestService.class).getBeanDefinition();
        log.info("iTestServiceBeanDefinition:{}",iTestServiceBeanDefinition);

        GenericBeanDefinition iTestControllerBeanDefinition = (GenericBeanDefinition) BeanDefinitionBuilder.genericBeanDefinition(TestController.class)
                .addConstructorArgReference("testService")
                .getBeanDefinition();

//        DefaultBeanNameGenerator generator = new DefaultBeanNameGenerator();
        AnnotationBeanNameGenerator generator = new AnnotationBeanNameGenerator();
        factory.registerBeanDefinition(generator.generateBeanName(iTestServiceBeanDefinition,factory),iTestServiceBeanDefinition);
        factory.registerBeanDefinition(generator.generateBeanName(iTestControllerBeanDefinition,factory),iTestControllerBeanDefinition);

        TestController bean = factory.getBean(TestController.class);
        log.info("{}",bean);
    }



}
