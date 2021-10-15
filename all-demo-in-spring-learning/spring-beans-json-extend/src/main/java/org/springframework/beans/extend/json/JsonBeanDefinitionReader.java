package org.springframework.beans.extend.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.core.NamedThreadLocal;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StreamUtils;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;

/**
 * 类似
 * {@link org.springframework.beans.factory.xml.XmlBeanDefinitionReader}
 * 只是本类是去json文件里读取bean definition
 *
 */
@Slf4j
public class JsonBeanDefinitionReader extends AbstractBeanDefinitionReader {
    private final ThreadLocal<Set<EncodedResource>> resourcesCurrentlyBeingLoaded =
            new NamedThreadLocal<Set<EncodedResource>>("json bean definition resources currently being loaded");


    public JsonBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    @Override
    public int loadBeanDefinitions(Resource resource) throws BeanDefinitionStoreException {
        Set<EncodedResource> currentResources = this.resourcesCurrentlyBeingLoaded.get();
        if (currentResources == null) {
            currentResources = new HashSet<EncodedResource>(4);
            this.resourcesCurrentlyBeingLoaded.set(currentResources);
        }

        EncodedResource encodedResource = new EncodedResource(resource);
        if (!currentResources.add(encodedResource)) {
            throw new BeanDefinitionStoreException(
                    "Detected cyclic loading of " + encodedResource + " - check your import definitions!");
        }
        String json = null;
        try (InputStream inputStream = encodedResource.getResource().getInputStream()) {
            json = StreamUtils.copyToString(inputStream, Charset.forName("UTF-8"));
        } catch (IOException e) {
            log.error("{}",e);
            return 0;
        } finally {
            currentResources.remove(encodedResource);
            if (currentResources.isEmpty()) {
                this.resourcesCurrentlyBeingLoaded.remove();
            }
        }

        List<GenericBeanDefinition> list = JSON.parseArray(json, GenericBeanDefinition.class);
        if (CollectionUtils.isEmpty(list)) {
            return 0;
        }
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

        setBeanNameGenerator(new AnnotationBeanNameGenerator());
        BeanNameGenerator beanNameGenerator = getBeanNameGenerator();
        BeanDefinitionRegistry registry = getRegistry();
        for (GenericBeanDefinition genericBeanDefinition : list) {
            String beanName = beanNameGenerator.generateBeanName(genericBeanDefinition, registry);
            registry.registerBeanDefinition(beanName,genericBeanDefinition);
        }

        return list.size();
    }
}
