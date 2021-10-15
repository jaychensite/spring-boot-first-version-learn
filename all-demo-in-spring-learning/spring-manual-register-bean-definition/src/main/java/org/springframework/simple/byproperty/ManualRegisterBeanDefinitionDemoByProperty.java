package org.springframework.simple.byproperty;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.simple.ITestService;
import org.springframework.simple.TestService;
import org.springframework.util.Assert;

import java.util.List;

/**
 * desc:
 * 通过property的方式注入
 * @author : caokunliang
 * creat_date: 2019/12/19 0019
 * creat_time: 12:55
 **/
@Slf4j
public class ManualRegisterBeanDefinitionDemoByProperty {
    public static void main(String[] args) {
        wireDependencyByProperty();
    }


    /**
     * 通过property属性的方式来注入依赖
     */
    private static void wireDependencyByProperty() {
        /**
         * 1：生成bean factory
         */
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        /**
         * 2. 构造bean definition，并在bean definition中表达bean之间的依赖关系
         */
        GenericBeanDefinition iTestServiceBeanDefinition = (GenericBeanDefinition) BeanDefinitionBuilder.genericBeanDefinition(TestService.class).getBeanDefinition();
        log.info("iTestServiceBeanDefinition:{}",iTestServiceBeanDefinition);


        GenericBeanDefinition iTestControllerBeanDefinition = (GenericBeanDefinition) BeanDefinitionBuilder
                .genericBeanDefinition(TestControllerWireByProperty.class)
                .addPropertyReference("t","testService")
                .addPropertyValue("name","just test")
                .getBeanDefinition();

        /**
         * 3. 注册bean definition
         */
//        DefaultBeanNameGenerator generator = new DefaultBeanNameGenerator();
        AnnotationBeanNameGenerator generator = new AnnotationBeanNameGenerator();
        factory.registerBeanDefinition(generator.generateBeanName(iTestServiceBeanDefinition,factory),iTestServiceBeanDefinition);
        factory.registerBeanDefinition(generator.generateBeanName(iTestControllerBeanDefinition,factory),iTestControllerBeanDefinition);

        /**
         * 4. 获取bean
         */
        TestControllerWireByProperty bean = factory.getBean(TestControllerWireByProperty.class);
        log.info("TestControllerByConstructor：{}",bean);

        ITestService testService = factory.getBean(ITestService.class);
        log.info("testService bean:{}",testService);

        Assert.isTrue(bean.getT() == testService);

        List<BeanDefinition> beanDefinitionList = factory.getBeanDefinitionList();

    }



}
