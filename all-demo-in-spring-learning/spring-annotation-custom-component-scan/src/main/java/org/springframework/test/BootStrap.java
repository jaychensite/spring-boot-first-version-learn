package org.springframework.test;

import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.custom.MyConfigurationClassPostProcessor;
import org.springframework.custom.annotation.MyComponentScan;
import org.springframework.custom.annotation.MyConfiguration;
import org.springframework.test1.AnotherPersonService;

@MyConfiguration
@MyComponentScan(value = "org.springframework.test")
public class BootStrap {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        AnnotatedGenericBeanDefinition beanDefinition = new AnnotatedGenericBeanDefinition(BootStrap.class);
        context.registerBeanDefinition(BootStrap.class.getName(), beanDefinition);

        /**
         * 注册一个beanFactoryPostProcessor
         */
        RootBeanDefinition def = new RootBeanDefinition(MyConfigurationClassPostProcessor.class);
        def.setSource(null);
        context.registerBeanDefinition(MyConfigurationClassPostProcessor.class.getName(),
                def);

        context.refresh();

        PersonService bean = context.getBean(PersonService.class);
        System.out.println(bean);
        /**
         *
         */
        AnotherPersonService anotherPersonService = context.getBean(AnotherPersonService.class);
        System.out.println(anotherPersonService);
    }
}
