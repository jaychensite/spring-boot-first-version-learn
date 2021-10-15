package org.springframework.custom.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionDefaults;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.custom.annotation.MyComponent;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Slf4j
public class MyComponentScanParser {
    static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

    private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();


    private String resourcePattern = DEFAULT_RESOURCE_PATTERN;

    private final List<TypeFilter> includeFilters = new LinkedList<TypeFilter>();

    private BeanDefinitionDefaults beanDefinitionDefaults = new BeanDefinitionDefaults();

    public MyComponentScanParser(BeanNameGenerator componentScanBeanNameGenerator, Environment environment, BeanDefinitionRegistry registry) {
        this.componentScanBeanNameGenerator = componentScanBeanNameGenerator;
        this.environment = environment;
        this.registry = registry;
    }

    public Set<BeanDefinitionHolder> parse(AnnotationAttributes componentScan, String className) {
        String basePackage = componentScan.getString("value");

        includeFilters.add(new AnnotationTypeFilter(MyComponent.class));

        Set<BeanDefinitionHolder> beanDefinitions = new LinkedHashSet<BeanDefinitionHolder>();

        /**
         * 获取包下的全部bean definition
         */
        Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
        /**
         * 对扫描回来的bean，进行一定的处理，然后注册到bean registry
         */
        for (BeanDefinition candidate : candidates) {
            String generateBeanName = componentScanBeanNameGenerator.generateBeanName(candidate, registry);

            if (candidate instanceof AbstractBeanDefinition) {
                ((AbstractBeanDefinition)candidate).applyDefaults(this.beanDefinitionDefaults);
            }

            if (candidate instanceof AnnotatedBeanDefinition) {
                AnnotationConfigUtils.processCommonDefinitionAnnotations((AnnotatedBeanDefinition) candidate);
            }

            boolean b = checkCandidate(generateBeanName, candidate);
            if (b) {
                beanDefinitions.add(new BeanDefinitionHolder(candidate,generateBeanName));
            }
        }

        /**
         * 注册到bean definition registry
         */
        for (BeanDefinitionHolder beanDefinitionHolder : beanDefinitions) {
            registry.registerBeanDefinition(beanDefinitionHolder.getBeanName(),beanDefinitionHolder.getBeanDefinition());
        }

        return beanDefinitions;
    }



    protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition) throws IllegalStateException {
        if (!this.registry.containsBeanDefinition(beanName)) {
            return true;
        }
        return false;
    }

    public Set<BeanDefinition> findCandidateComponents(String basePackage) {
        Set<BeanDefinition> candidates = new LinkedHashSet<BeanDefinition>();
        try {
            String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                    resolveBasePackage(basePackage) + "/" + this.resourcePattern;
            Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);
            for (Resource resource : resources) {
                log.info("Scanning " + resource);

                if (!resource.isReadable()) {
                    continue;
                }

                MetadataReader metadataReader = MyConfigurationUtils.getMetadataReader(resource);
                if (isCandidateComponent(metadataReader)) {
                    ScannedGenericBeanDefinition sbd = new ScannedGenericBeanDefinition(metadataReader);
                    sbd.setResource(resource);
                    sbd.setSource(resource);
                    candidates.add(sbd);
                }
                else {
                    log.info("Ignored because not matching any filter: " + resource);
                }
            }
        }
        catch (IOException ex) {
            throw new BeanDefinitionStoreException("I/O failure during classpath scanning", ex);
        }

        return candidates;
    }

    private BeanNameGenerator componentScanBeanNameGenerator;
    private Environment environment;
    private BeanDefinitionRegistry registry;


    protected String resolveBasePackage(String basePackage) {
        return ClassUtils.convertClassNameToResourcePath(environment.resolveRequiredPlaceholders(basePackage));
    }


    protected boolean isCandidateComponent(MetadataReader metadataReader) throws IOException {
        for (TypeFilter tf : this.includeFilters) {
            if (tf.match(metadataReader, MyConfigurationUtils.getMetadataReaderFactory())) {
                return true;
            }
        }
        return false;
    }
}
