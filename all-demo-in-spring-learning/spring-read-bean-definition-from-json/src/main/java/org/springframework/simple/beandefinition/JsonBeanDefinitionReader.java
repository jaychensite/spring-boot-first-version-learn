package org.springframework.simple.beandefinition;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * desc:
 *
 * @author : caokunliang
 * creat_date: 2019/12/20 0020
 * creat_time: 13:20
 **/
@Slf4j
public class JsonBeanDefinitionReader {

    public static List<GenericBeanDefinition> parseBeanDefinitionsFromJson(String jsonFile) {
        String json = null;
        try {
            json = StreamUtils.copyToString(new ClassPathResource(jsonFile).getInputStream(), Charset.forName("UTF-8"));
        } catch (IOException e) {
            log.error("{}",e);
            return new ArrayList<>();
        }

        log.info("jsonfile:{}",json);
        List<GenericBeanDefinition> list = JSON.parseArray(json, GenericBeanDefinition.class);
        for (GenericBeanDefinition genericBeanDefinition : list) {
            /**
             * 1、处理beanClass
             */
            Class<?> clazz = null;
            try {
                clazz = Thread.currentThread().getContextClassLoader().loadClass(genericBeanDefinition.getBeanClassName());
            } catch (ClassNotFoundException e) {
                log.error("bean class cant be load for beandefinition: {}",genericBeanDefinition);
                throw new RuntimeException();
            }

            genericBeanDefinition.setBeanClass(clazz);

            /**
             * 2、处理constructor问题
             */
            ConstructorArgumentValues constructorArgumentValues = genericBeanDefinition.getConstructorArgumentValues();
            if (constructorArgumentValues.isEmpty()) {
                continue;
            }
            Map<Integer, ConstructorArgumentValues.ValueHolder> map = constructorArgumentValues.getIndexedArgumentValues();
            if (CollectionUtils.isEmpty(map)) {
                continue;
            }
            for (ConstructorArgumentValues.ValueHolder valueHolder : map.values()) {
                Object value = valueHolder.getValue();
                if (value instanceof JSONObject) {
                    JSONObject jsonObject = (JSONObject) value;
                    RuntimeBeanReference runtimeBeanReference = jsonObject.toJavaObject(RuntimeBeanReference.class);
                    valueHolder.setValue(runtimeBeanReference);
                }
            }
        }
        return list;
    }
}
