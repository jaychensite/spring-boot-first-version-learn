package org.springframework.custom.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.custom.annotation.MyConfiguration;

import java.io.IOException;

@Slf4j
public class MyConfigurationUtils {

    public static MetadataReaderFactory getMetadataReaderFactory() {
        return metadataReaderFactory;
    }

    private static MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory();

    /**
     * 检测是否为{@link MyConfiguration}配置类
     * @param beanDef
     * @return
     */
    public static boolean checkConfigurationClassCandidate(BeanDefinition beanDef) {
        AnnotationMetadata metadata = null;

        // Check already loaded Class if present...
        // since we possibly can't even load the class file for this Class.
        if (beanDef instanceof AbstractBeanDefinition &&
                ((AbstractBeanDefinition) beanDef).hasBeanClass()) {
            Class<?> beanClass = ((AbstractBeanDefinition) beanDef).getBeanClass();
            metadata = new StandardAnnotationMetadata(beanClass, true);
        }
        else {
            String className = beanDef.getBeanClassName();
            if (className != null) {
                try {
                    /**
                     * 根据className，获取元数据reader
                     * 里面用了asm框架,visitor模式
                     */
                    MetadataReader metadataReader =
                            metadataReaderFactory.getMetadataReader(className);
                    metadata = metadataReader.getAnnotationMetadata();
                }
                catch (IOException ex) {
                    log.debug("Could not find class file for introspecting factory methods: " + className, ex);
                    return false;
                }
            }
        }

        /**
         * 注解了我们的{@link MyConfiguration}
         */
        if (metadata != null) {
            if (metadata.isAnnotated(MyConfiguration.class.getName())) {
                return true;
            }
        }

        return false;
    }

    public static MetadataReader getMetadataReader(String className) {
        MetadataReaderFactory metadataReaderFactory = getMetadataReaderFactory();
        MetadataReader metadataReader = null;
        try {
            metadataReader = metadataReaderFactory.getMetadataReader(className);
        } catch (IOException e) {
            log.error("e:{}",e);
            throw new RuntimeException();
        }
        return metadataReader;
    }


    public static MetadataReader getMetadataReader(Resource resource) {
        MetadataReaderFactory metadataReaderFactory = getMetadataReaderFactory();
        MetadataReader metadataReader = null;
        try {
            metadataReader = metadataReaderFactory.getMetadataReader(resource);
        } catch (IOException e) {
            log.error("e:{}",e);
            throw new RuntimeException();
        }
        return metadataReader;
    }

}
