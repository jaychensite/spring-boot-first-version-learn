package org.springframework.custom.domain;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.custom.utils.MyConfigurationUtils;
import org.springframework.util.Assert;

import java.io.IOException;


@Data
@Slf4j
public class MyConfigurationClass {


    private final AnnotationMetadata metadata;

    private final Resource resource;

    private String beanName;


    public MyConfigurationClass(String className,String beanName) {
        MetadataReader metadataReader = MyConfigurationUtils.getMetadataReader(className);


        this.metadata = metadataReader.getAnnotationMetadata();
        this.resource = metadataReader.getResource();
        this.beanName = beanName;
    }
}
