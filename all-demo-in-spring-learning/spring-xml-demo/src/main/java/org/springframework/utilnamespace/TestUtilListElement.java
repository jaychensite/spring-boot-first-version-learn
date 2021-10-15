package org.springframework.utilnamespace;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.MyFastJson;

import java.util.List;
import java.util.Map;

/**
 * desc:
 *
 * @author : caokunliang
 * creat_date: 2019/12/25 0025
 * creat_time: 15:50
 **/
@Slf4j
public class TestUtilListElement {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[]{"classpath:util-namespace-test-list.xml"},false);
        context.refresh();

        Map<String, Object> map = context.getDefaultListableBeanFactory().getAllSingletonObjectMap();
        log.info("singletons:{}", JSONObject.toJSONString(map));

        List<BeanDefinition> list =
                context.getBeanFactory().getBeanDefinitionList();
        MyFastJson.printJsonStringForBeanDefinitionList(list);

        Object bean = context.getBean("testList");
        System.out.println("bean:" + bean);

        bean = context.getBean("testBeanList");
        System.out.println("bean:" + bean);

    }
}
