package org.springframework.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.GenericBeanDefinition;

import java.util.List;

@Slf4j
public class MyFastJson {

    public static void printJsonStringForBeanDefinitionList(List<BeanDefinition> list) {
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter(MutablePropertyValues.class);
        SimplePropertyPreFilter genericBeanDefinitionFilter = new SimplePropertyPreFilter(GenericBeanDefinition.class);
        SimplePropertyPreFilter typedStringValueFilter = new SimplePropertyPreFilter(TypedStringValue.class);
        filter.getExcludes().add("propertyValues");
        genericBeanDefinitionFilter.getExcludes().add("beanClass");
        typedStringValueFilter.getExcludes().add("targetType");
        log.info("bean definition list:{}", JSON.toJSONString(list,
                new SerializeFilter[]{filter,genericBeanDefinitionFilter,typedStringValueFilter},
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteNonStringKeyAsString,
                SerializerFeature.PrettyFormat));

    }
}
