package org.springframework.simple;


import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SimpleBootstrapApplication  {



	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"classpath:application-context.xml"},false);
        context.setAllowBeanDefinitionOverriding(true);
		context.refresh();

        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getBeanFactory();
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(String.class);
        beanFactory.registerBeanDefinition("testService", beanDefinition);


		TestController testController = (TestController) context.getBean("testController");
		System.out.println(testController);
        Object testService = context.getBean("testService");
        System.out.println(testService);

        TestByPropertyController testByPropertyController = (TestByPropertyController) context.getBean("testByPropertyController");
        System.out.println(testByPropertyController);
	}
}
