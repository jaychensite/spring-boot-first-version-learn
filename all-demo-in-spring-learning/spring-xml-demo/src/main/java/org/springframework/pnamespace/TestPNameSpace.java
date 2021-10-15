package org.springframework.pnamespace;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

@Slf4j
public class TestPNameSpace {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[]{"classpath:p-namespace-test.xml"},false);
        context.setAllowBeanDefinitionOverriding(true);
        context.refresh();

        List<BeanDefinition> list =
                context.getBeanFactory().getBeanDefinitionList();
        log.info("bean definition list:{}",list);

        Object o = context.getBean(Users.class);
        System.out.println(o);
    }
}
