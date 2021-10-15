package org.springframework.custom;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;

import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.custom.utils.MyConfigurationUtils;
import org.springframework.util.CollectionUtils;

import java.util.LinkedHashSet;
import java.util.Set;

@Slf4j
public class MyConfigurationClassPostProcessor implements BeanDefinitionRegistryPostProcessor,
        ResourceLoaderAware, BeanClassLoaderAware, EnvironmentAware {

    private ClassLoader classLoader;

    private Environment environment;

    private ResourceLoader resourceLoader;

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        log.info("postProcessBeanDefinitionRegistry...");
        /**
         * 1: 找到标注了{@link org.springframework.custom.annotation.MyConfiguration}注解的类
         * 这些类就是我们的配置类
         * 我们通过这些类，可以发现更多的bean definition
         */
        Set<BeanDefinitionHolder> beanDefinitionHolders = new LinkedHashSet<BeanDefinitionHolder>();
        for (String beanName : registry.getBeanDefinitionNames()) {
            BeanDefinition beanDef = registry.getBeanDefinition(beanName);
            if (MyConfigurationUtils.checkConfigurationClassCandidate(beanDef)) {
                beanDefinitionHolders.add(new BeanDefinitionHolder(beanDef, beanName));
            }
        }

        if (CollectionUtils.isEmpty(beanDefinitionHolders)) {
            return;
        }

        MyConfigurationClassParser parser = new MyConfigurationClassParser(environment,registry);
        parser.parse(beanDefinitionHolders);


    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }


    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }


}
