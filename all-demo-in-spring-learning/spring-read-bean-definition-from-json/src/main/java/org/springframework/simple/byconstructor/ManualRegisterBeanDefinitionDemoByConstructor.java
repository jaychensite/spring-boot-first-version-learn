package org.springframework.simple.byconstructor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.*;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.simple.ITestService;
import org.springframework.simple.TestService;
import org.springframework.simple.beandefinition.JsonBeanDefinitionReader;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

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
         * 2. 获取bean definition列表
         */
        List<GenericBeanDefinition> list = JsonBeanDefinitionReader.parseBeanDefinitionsFromJson("beanDefinition.json");
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        /**
         * 3. 注册bean definition
         */
        AnnotationBeanNameGenerator generator = new AnnotationBeanNameGenerator();
        for (GenericBeanDefinition beanDefinition : list) {
            String beanName = generator.generateBeanName(beanDefinition, factory);
            factory.registerBeanDefinition(beanName, beanDefinition);
        }



        /**
         * 4. 获取bean
         */
        TestControllerByConstructor bean = factory.getBean(TestControllerByConstructor.class);
        log.info("TestControllerByConstructor：{}",bean);

        ITestService testService = factory.getBean(ITestService.class);
        log.info("testService bean:{}",testService);

        Assert.isTrue(bean.getTestService() == testService);


        List<BeanDefinition> beanDefinitionList = factory.getBeanDefinitionList();
        log.info("bean definition list:{}", JSON.toJSONString(beanDefinitionList, SerializerFeature.PrettyFormat));

    }



}
