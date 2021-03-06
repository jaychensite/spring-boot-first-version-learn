package org.springframework.custom;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderLog;
import org.springframework.custom.annotation.MyComponentScan;
import org.springframework.custom.domain.MyConfigurationClass;
import org.springframework.custom.utils.MyComponentScanParser;
import org.springframework.custom.utils.MyConfigurationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;


@Slf4j
public class MyConfigurationClassParser {
    private BeanNameGenerator componentScanBeanNameGenerator = new AnnotationBeanNameGenerator();

    private ResourceLoader resourceLoader = new DefaultResourceLoader();

    private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
    private Environment environment;
    private BeanDefinitionRegistry registry;


    private MyComponentScanParser componentScanParser;


    public MyConfigurationClassParser(Environment environment, BeanDefinitionRegistry registry) {
        this.environment = environment;
        this.registry = registry;
        this.componentScanParser = new MyComponentScanParser(componentScanBeanNameGenerator,
                environment,registry);
    }

    /**
     * ??????{@link MyConfigurationClassParser} ????????????
     * @param configCandidates
     */
    public void parse(Set<BeanDefinitionHolder> configCandidates) {
        for (BeanDefinitionHolder holder : configCandidates) {
            BeanDefinition bd = holder.getBeanDefinition();
            String className = bd.getBeanClassName();

            try {
                processConfigurationClass(className);
            }
            catch (IOException ex) {
                throw new BeanDefinitionStoreException("Failed to load bean class: " + bd.getBeanClassName(), ex);
            }
        }
    }


    protected void processConfigurationClass(String className) throws IOException {
        MetadataReader metadataReader = MyConfigurationUtils.getMetadataReader(className);
        AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();

        /**
         * ?????????????????????????????????{@link MyComponentScan}
         */
        Map<String, Object> annotationAttributes = annotationMetadata.getAnnotationAttributes(MyComponentScan.class.getName(), true);
        AnnotationAttributes componentScan = AnnotationAttributes.fromMap(annotationAttributes);

        /**
         * ?????????????????????{@link MyComponentScan},?????????????????????
         */
        if (componentScan != null) {
            /**
             * ??????????????????base package????????????bean????????????????????????beanDefinition???bean registry
             */
            // the config class is annotated with @ComponentScan -> perform the scan immediately
            Set<BeanDefinitionHolder> scannedBeanDefinitions =
                    this.componentScanParser.parse(componentScan, annotationMetadata.getClassName());

            /**
             * ?????????????????????bean definition????????????????????????
             */
            if (!CollectionUtils.isEmpty(scannedBeanDefinitions)) {
                this.parse(scannedBeanDefinitions);
            }
        }

    }



}
