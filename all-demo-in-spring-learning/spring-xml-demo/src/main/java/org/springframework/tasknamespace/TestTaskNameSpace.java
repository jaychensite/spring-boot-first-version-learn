package org.springframework.tasknamespace;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.pnamespace.Users;
import org.springframework.scheduling.config.TaskExecutorFactoryBean;

import java.util.List;

@Slf4j
public class TestTaskNameSpace {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[]{"classpath:task-namespace-test.xml"},false);
        context.setAllowBeanDefinitionOverriding(true);
        context.refresh();

        List<BeanDefinition> list =
                context.getBeanFactory().getBeanDefinitionList();
        log.info("bean definition list:{}",list);

        TaskExecutorFactoryBean factoryBean = context.getBean(TaskExecutorFactoryBean.class);
        System.out.println(factoryBean);
        TaskExecutor executor = factoryBean.getObject();
        System.out.println(executor);

    }
}
