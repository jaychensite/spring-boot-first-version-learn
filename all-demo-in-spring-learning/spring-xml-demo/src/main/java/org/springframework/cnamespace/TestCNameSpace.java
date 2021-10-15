package org.springframework.cnamespace;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

@Slf4j
public class TestCNameSpace {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[]{"classpath:c-namespace-test.xml"},false);
        context.setAllowBeanDefinitionOverriding(true);
        context.refresh();

        List<BeanDefinition> list =
                context.getBeanFactory().getBeanDefinitionList();
        log.info("bean definition list:{}",list);

        Object o = context.getBean(Users.class);
        System.out.println(o);
    }
}
