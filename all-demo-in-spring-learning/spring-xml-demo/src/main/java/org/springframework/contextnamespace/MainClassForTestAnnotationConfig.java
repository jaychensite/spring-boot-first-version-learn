package org.springframework.contextnamespace;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.MyFastJson;

import java.util.List;
import java.util.Map;

@Slf4j
public class MainClassForTestAnnotationConfig {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[]{"classpath:context-namespace-test-annotation-config.xml"},false);
        context.refresh();

        Map<String, Object> map = context.getDefaultListableBeanFactory().getAllSingletonObjectMap();
        log.info("singletons:{}", JSONObject.toJSONString(map));

        List<BeanDefinition> list =
                context.getBeanFactory().getBeanDefinitionList();
        MyFastJson.printJsonStringForBeanDefinitionList(list);

        Object testService = context.getBean(TestService.class);
        System.out.println("testService bean:" + testService);

        Object bean = context.getBean(TestController.class);
        System.out.println("testController bean:" + bean);

    }
}
