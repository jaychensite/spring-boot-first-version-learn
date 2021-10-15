/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.bootstrap.context.embedded;

import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.AnnotationScopeMetadataResolver;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ScopeMetadataResolver;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.Assert;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

/**
 * {@link EmbeddedWebApplicationContext} that accepts annotated classes as input - in
 * particular {@link org.springframework.context.annotation.Configuration
 * <code>@Configuration</code>} -annotated classes, but also plain
 * {@link org.springframework.stereotype.Component <code>@Component</code>} classes and
 * JSR-330 compliant classes using {@code javax.inject} annotations.
 *
 * 翻译：{@link EmbeddedWebApplicationContext}，接收被注解的class作为输入源
 * （类似于以前的spring容器需要一个xml一样），尤其是{@link org.springframework.context.annotation.Configuration}
 * 注解的类，但是也可以是普通的{@link org.springframework.stereotype.Component}和{@code javax.inject}注解的类
 *
 *
 * Allows for
 * registering classes one by one (specifying class names as config location) as well as
 * for classpath scanning (specifying base packages as config location).
 *
 * 允许一个一个地注册类（手动指定类名），或者通过classpath扫描（指定base包名作为配置地址)
 * <p>
 * Note: In case of multiple {@code @Configuration} classes, later {@code @Bean}
 * definitions will override ones defined in earlier loaded files. This can be leveraged
 * to deliberately override certain bean definitions via an extra Configuration class.
 *
 * 在有多个{@code @Configuration}类时，后面的@bean方法定义会覆盖前面的。因此，可以通过这个方式，通过外部的
 * Configuration类去覆盖特定的bean definition。（就像可以通过命令行参数来覆盖spring boot的application.properties
 * 中的一样）
 *
 * @author Phillip Webb
 * @since 4.0
 * @see #register(Class...)
 * @see #scan(String...)
 * @see EmbeddedWebApplicationContext
 * @see AnnotationConfigWebApplicationContext
 */
public class AnnotationConfigEmbeddedWebApplicationContext extends
		EmbeddedWebApplicationContext {
	/**
	 * 一个精准加载类定义
	 */
	private final AnnotatedBeanDefinitionReader reader;

	/**
	 * 一个用于类路径扫描，批量装载
	 */
	private final ClassPathBeanDefinitionScanner scanner;

	/**
	 * Create a new {@link AnnotationConfigEmbeddedWebApplicationContext} that needs to be
	 * populated through {@link #register} calls and then manually {@linkplain #refresh
	 * refreshed}.
	 */
	public AnnotationConfigEmbeddedWebApplicationContext() {
		/**
		 * 这里有点叼啊，还在构造函数里就把this传出去了，并发编程实战里提到了，这可能会造成问题，意思就是可能把一个还没有
		 * 构造完成的对象给传递出去了，要是泄露到其他线程就糟了
		 */
		this.reader = new AnnotatedBeanDefinitionReader(this);
		this.scanner = new ClassPathBeanDefinitionScanner(this);
	}

	/**
	 * Create a new {@link AnnotationConfigEmbeddedWebApplicationContext}, deriving bean
	 * definitions from the given annotated classes and automatically refreshing the
	 * context.
	 * 创建一个新的{@link AnnotationConfigEmbeddedWebApplicationContext}，从指定的注解class中扩展bean定义，
	 * 并自动刷新上下文
	 * @param annotatedClasses one or more annotated classes, e.g. {@link Configuration
	 * <code>@Configuration</code>} classes
	 */
	public AnnotationConfigEmbeddedWebApplicationContext(Class<?>... annotatedClasses) {
		this();
		register(annotatedClasses);
		refresh();
	}

	/**
	 * Create a new {@link AnnotationConfigEmbeddedWebApplicationContext}, scanning for
	 * bean definitions in the given packages and automatically refreshing the context.
	 * @param basePackages the packages to check for annotated classes
	 */
	public AnnotationConfigEmbeddedWebApplicationContext(String... basePackages) {
		this();
		scan(basePackages);
		refresh();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Delegates given environment to underlying {@link AnnotatedBeanDefinitionReader} and
	 * {@link ClassPathBeanDefinitionScanner} members.
	 */
	@Override
	public void setEnvironment(ConfigurableEnvironment environment) {
		super.setEnvironment(environment);
		this.reader.setEnvironment(environment);
		this.scanner.setEnvironment(environment);
	}

	/**
	 * Provide a custom {@link BeanNameGenerator} for use with
	 * {@link AnnotatedBeanDefinitionReader} and/or {@link ClassPathBeanDefinitionScanner}
	 * , if any.
	 * <p>
	 * Default is
	 * {@link org.springframework.context.annotation.AnnotationBeanNameGenerator}.
	 * <p>
	 * Any call to this method must occur prior to calls to {@link #register(Class...)}
	 * and/or {@link #scan(String...)}.
	 * @see AnnotatedBeanDefinitionReader#setBeanNameGenerator
	 * @see ClassPathBeanDefinitionScanner#setBeanNameGenerator
	 */
	public void setBeanNameGenerator(BeanNameGenerator beanNameGenerator) {
		this.reader.setBeanNameGenerator(beanNameGenerator);
		this.scanner.setBeanNameGenerator(beanNameGenerator);
		this.getBeanFactory().registerSingleton(
				AnnotationConfigUtils.CONFIGURATION_BEAN_NAME_GENERATOR,
				beanNameGenerator);
	}

	/**
	 * Set the {@link ScopeMetadataResolver} to use for detected bean classes.
	 * <p>
	 * The default is an {@link AnnotationScopeMetadataResolver}.
	 * <p>
	 * Any call to this method must occur prior to calls to {@link #register(Class...)}
	 * and/or {@link #scan(String...)}.
	 */
	public void setScopeMetadataResolver(ScopeMetadataResolver scopeMetadataResolver) {
		this.reader.setScopeMetadataResolver(scopeMetadataResolver);
		this.scanner.setScopeMetadataResolver(scopeMetadataResolver);
	}

	/**
	 * Register one or more annotated classes to be processed. Note that
	 * {@link #refresh()} must be called in order for the context to fully process the new
	 * class.
	 * <p>
	 * Calls to {@link #register} are idempotent; adding the same annotated class more
	 * than once has no additional effect.
	 * @param annotatedClasses one or more annotated classes, e.g. {@link Configuration
	 * <code>@Configuration</code>} classes
	 * @see #scan(String...)
	 * @see #refresh()
	 */
	public void register(Class<?>... annotatedClasses) {
		Assert.notEmpty(annotatedClasses,
				"At least one annotated class must be specified");
		this.reader.register(annotatedClasses);
	}

	/**
	 * Perform a scan within the specified base packages. Note that {@link #refresh()}
	 * must be called in order for the context to fully process the new class.
	 * @param basePackages the packages to check for annotated classes
	 * @see #register(Class...)
	 * @see #refresh()
	 */
	public void scan(String... basePackages) {
		Assert.notEmpty(basePackages, "At least one base package must be specified");
		this.scanner.scan(basePackages);
	}

	@Override
	protected void prepareRefresh() {
		this.scanner.clearCache();
		super.prepareRefresh();
	}

}
