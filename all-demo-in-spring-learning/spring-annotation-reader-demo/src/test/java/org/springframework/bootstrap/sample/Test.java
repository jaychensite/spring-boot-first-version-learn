package org.springframework.bootstrap.sample;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class Test {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        SimpleMetadataReaderFactory simpleMetadataReaderFactory = new SimpleMetadataReaderFactory();
        LinkedHashSet<String> result = new LinkedHashSet<>();

        getAnnotationSet(result, "org.springframework.test.TestController", simpleMetadataReaderFactory);
        for (String s : result) {
            System.out.println(s);
        }
//        getAnnotationByClass("org.springframework.test.TestController");
    }

    public static void getAnnotationSet(LinkedHashSet<String> result, String className, SimpleMetadataReaderFactory simpleMetadataReaderFactory) throws IOException {
        boolean contains = result.add(className);
        if (!contains) {
            return;
        }

        MetadataReader metadataReader = simpleMetadataReaderFactory.getMetadataReader(className);
        AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
        Set<String> annotationTypes = annotationMetadata.getAnnotationTypes();

        if (!CollectionUtils.isEmpty(annotationTypes)) {
            for (String annotationType : annotationTypes) {
                getAnnotationSet(result, annotationType, simpleMetadataReaderFactory);
            }
        }

    }

    public static void getAnnotationByClass(String className) throws ClassNotFoundException {
        Class<?> clazz = Class.forName(className);
        Set<String> metaAnnotationTypeNames = new LinkedHashSet<String>();
        for (Annotation metaAnnotation : clazz.getAnnotations()) {
            recusivelyCollectMetaAnnotations(metaAnnotationTypeNames, metaAnnotation);
        }
        for (String metaAnnotationTypeName : metaAnnotationTypeNames) {
            System.out.println(metaAnnotationTypeName);
        }
    }


    private static void recusivelyCollectMetaAnnotations(Set<String> visited, Annotation annotation) {
        if (visited.add(annotation.annotationType().getName())) {
            for (Annotation metaMetaAnnotation : annotation.annotationType().getAnnotations()) {
                recusivelyCollectMetaAnnotations(visited, metaMetaAnnotation);
            }
        }
    }
}
