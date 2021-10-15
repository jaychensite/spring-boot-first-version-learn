package org.springframework.simple.byconstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.*;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.simple.ITestService;
import org.springframework.simple.TestService;
import org.springframework.util.Assert;

import java.util.List;

/**
 * desc:
 *
 * @author : caokunliang
 * creat_date: 2019/12/19 0019
 * creat_time: 12:55
 **/
@Slf4j
public class ManualRegisterBeanDefinitionDemoByConstructor {
    public static void main(String[] args) {
        wireDependencyByConstructor();
    }


    /**
     * 通过构造器的方式来注入依赖
     */
    private static void wireDependencyByConstructor() {
        /**
         * 1：生成bean factory
         */
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        /**
         * 2. 构造bean definition，并在bean definition中表达bean之间的依赖关系
         */
        GenericBeanDefinition iTestServiceBeanDefinition = (GenericBeanDefinition) BeanDefinitionBuilder
                .genericBeanDefinition(TestService.class).getBeanDefinition();
        log.info("iTestServiceBeanDefinition:{}",iTestServiceBeanDefinition);

        GenericBeanDefinition iTestControllerBeanDefinition = (GenericBeanDefinition) BeanDefinitionBuilder
                .genericBeanDefinition(TestControllerByConstructor.class)
                .addConstructorArgReference("testService")
                .addConstructorArgValue("wire by constructor")
                .getBeanDefinition();


        /**
         * 3. 注册bean definition
         */
//        DefaultBeanNameGenerator generator = new DefaultBeanNameGenerator();
        AnnotationBeanNameGenerator generator = new AnnotationBeanNameGenerator();
        String beanNameForTestService = generator.generateBeanName(iTestServiceBeanDefinition, factory);
        factory.registerBeanDefinition(beanNameForTestService, iTestServiceBeanDefinition);

        String beanNameForTestController = generator.generateBeanName(iTestControllerBeanDefinition, factory);
        factory.registerBeanDefinition(beanNameForTestController, iTestControllerBeanDefinition);

        /**
         * 4. 获取bean
         */
        TestControllerByConstructor bean = factory.getBean(TestControllerByConstructor.class);
        log.info("TestControllerByConstructor：{}",bean);

        ITestService testService = factory.getBean(ITestService.class);
        log.info("testService bean:{}",testService);

        Assert.isTrue(bean.getTestService() == testService);


        List<BeanDefinition> beanDefinitionList = factory.getBeanDefinitionList();
        log.info("bean definition list:{}", beanDefinitionList);

    }



}
