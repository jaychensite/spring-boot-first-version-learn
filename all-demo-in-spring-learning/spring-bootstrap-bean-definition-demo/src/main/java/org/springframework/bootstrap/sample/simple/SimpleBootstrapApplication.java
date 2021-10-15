package org.springframework.bootstrap.sample.simple;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.bootstrap.CommandLineRunner;
import org.springframework.bootstrap.SpringApplication;
import org.springframework.bootstrap.context.annotation.EnableAutoConfiguration;
import org.springframework.bootstrap.sample.simple.service.HelloWorldService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@Slf4j
public class SimpleBootstrapApplication implements CommandLineRunner {
	@Autowired
	private ApplicationContext applicationContext;

	// Simple example shows how a command line spring application can execute an
	// injected bean service. Also demonstrates how you can use @Value to inject
	// command line args ('--name=whatever') or application properties

	@Autowired
	private HelloWorldService helloWorldService;

	@Override
	public void run(String... args) {
		DefaultListableBeanFactory beanFactory =
				(DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
		AnnotatedBeanDefinition annotatedBeanDefinition = (AnnotatedBeanDefinition) beanFactory.getBeanDefinition("testService");
		AnnotationMetadata metadata = annotatedBeanDefinition.getMetadata();
		Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes("org.springframework.stereotype.Component");
		log.info("annotationAttributes:{}",annotationAttributes);
//		System.out.println(this.helloWorldService.getHelloMessage());
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(SimpleBootstrapApplication.class, args);
	}
}
