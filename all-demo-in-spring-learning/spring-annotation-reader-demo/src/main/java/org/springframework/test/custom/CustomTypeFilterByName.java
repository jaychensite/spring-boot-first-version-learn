package org.springframework.test.custom;

import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;

/**
 * 自定义的类型匹配器，如果注解了我们的DubboExportService，就匹配；否则不匹配
 */
public class CustomTypeFilterByName implements TypeFilter {
    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        boolean b = metadataReader.getAnnotationMetadata().hasAnnotation(DubboExportService.class.getName());
        if (b) {
            return true;
        }

        return false;
    }
}
