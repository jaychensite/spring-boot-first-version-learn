package org.springframework.bootstrap.sample.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by Administrator on 2019/12/7.
 */
@Component
public class TestMetadataReader {
    static Logger logger = LoggerFactory.getLogger(TestMetadataReader.class);

    public static void main(String[] args) throws IOException {
        SimpleMetadataReaderFactory factory = new SimpleMetadataReaderFactory();
        MetadataReader reader = factory.getMetadataReader("org.springframework.bootstrap.sample.service.TestMetadataReader");
        AnnotationMetadata annotationMetadata = reader.getAnnotationMetadata();
        ClassMetadata classMetadata = reader.getClassMetadata();
        Resource resource = reader.getResource();
        logger.info("{},{},{}",annotationMetadata,classMetadata,resource);
    }
}
