/*
 * Copyright 2012-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.bootstrap;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.filter.AbstractTypeHierarchyTraversingFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.Assert;

/**
 * 从不同的源加载bean definition，包括xml和java config方式。
 * 作为一个简单的，基于注解beanDefinition读取器、xml beanDefinition读取器、classpath beanDefinition读取器
 * 的门面。
 * Loads bean definitions from underlying sources, including XML and JavaConfig. Acts as a
 * simple facade over {@link AnnotatedBeanDefinitionReader},
 * {@link XmlBeanDefinitionReader} and {@link ClassPathBeanDefinitionScanner}. See
 * {@link SpringApplication} for the types of sources that are supported.
 * 
 * @author Phillip Webb
 * @see #setBeanNameGenerator(BeanNameGenerator)
 */
class BeanDefinitionLoader {

	private static final ResourceLoader DEFAULT_RESOURCE_LOADER = new DefaultResourceLoader();

	private Object[] sources;

	private AnnotatedBeanDefinitionReader annotatedReader;

	/**
	 * 用来从xml中读取bean definition
	 */
	private XmlBeanDefinitionReader xmlReader;
	/**
	 * 从classpath下扫描bean definition
	 */
	private ClassPathBeanDefinitionScanner scanner;

	/**
	 * 资源加载器
	 */
	private ResourceLoader resourceLoader;

	/**
	 * Create a new {@link BeanDefinitionLoader} that will load beans into the specified
	 * {@link BeanDefinitionRegistry}.
	 * @param registry the bean definition registry that will contain the loaded beans
	 * @param sources the bean sources
	 */
	public BeanDefinitionLoader(BeanDefinitionRegistry registry, Object... sources) {
		Assert.notNull(registry, "Registry must not be null");
		Assert.notEmpty(sources, "Sources must not be empty");
		this.sources = sources;
		this.annotatedReader = new AnnotatedBeanDefinitionReader(registry);
		this.xmlReader = new XmlBeanDefinitionReader(registry);
		this.scanner = new ClassPathBeanDefinitionScanner(registry);
		this.scanner.addExcludeFilter(new ClassExcludeFilter(sources));
	}

	/**
	 * Set the bean name generator to be used by the underlying readers and scanner.
	 * @param beanNameGenerator the bean name generator
	 */
	public void setBeanNameGenerator(BeanNameGenerator beanNameGenerator) {
		this.annotatedReader.setBeanNameGenerator(beanNameGenerator);
		this.xmlReader.setBeanNameGenerator(beanNameGenerator);
		this.scanner.setBeanNameGenerator(beanNameGenerator);
	}

	/**
	 * Set the resource loader to be used by the underlying readers and scanner.
	 * @param resourceLoader the resource loader
	 */
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
		this.xmlReader.setResourceLoader(resourceLoader);
		this.scanner.setResourceLoader(resourceLoader);
	}

	/**
	 * Set the environment to be used by the underlying readers and scanner.
	 * @param environment
	 */
	public void setEnvironment(ConfigurableEnvironment environment) {
		this.annotatedReader.setEnvironment(environment);
		this.xmlReader.setEnvironment(environment);
		this.scanner.setEnvironment(environment);
	}

	/**
	 * Load the sources into the reader.
	 * @return the number of loaded beans
	 */
	public int load() {
		int count = 0;
		for (Object source : this.sources) {
			count += load(source);
		}
		return count;
	}

	private int load(Object source) {
		Assert.notNull(source, "Source must not be null");
		/**
		 * 如果是传进来一个class，说明是java 配置类的方式来启动应用上下文，这里
		 * 因此要使用 基于注解的reader
		 */
		if (source instanceof Class<?>) {
			this.annotatedReader.register((Class<?>) source);
			return 1;
		}

		/**
		 * 如果是一个resource，说明是一个xml文件，要使用xml reader来加载bean definition
		 */
		if (source instanceof Resource) {
			return this.xmlReader.loadBeanDefinitions((Resource) source);
		}

		/**
		 * 如果传进来的是一个包名，则使用classpath scan的方式，类比component scan
		 */
		if (source instanceof Package) {
			// FIXME register the scanned package for data to pick up
			return this.scanner.scan(((Package) source).getName());
		}

		if (source instanceof CharSequence) {
			try {
				return load(Class.forName(source.toString()));
			} catch (ClassNotFoundException e) {
			}

			Resource loadedResource = (this.resourceLoader != null ? this.resourceLoader
					: DEFAULT_RESOURCE_LOADER).getResource(source.toString());
			if (loadedResource != null && loadedResource.exists()) {
				return load(loadedResource);
			}
			Package packageResource = Package.getPackage(source.toString());
			if (packageResource != null) {
				return load(packageResource);
			}
		}

		throw new IllegalArgumentException("Invalid source '" + source + "'");
	}

	/**
	 * Simple {@link TypeFilter} used to ensure that specified {@link Class} sources are
	 * not accidentally re-added during scanning.
	 */
	private static class ClassExcludeFilter extends AbstractTypeHierarchyTraversingFilter {

		private Set<String> classNames = new HashSet<String>();

		public ClassExcludeFilter(Object... sources) {
			super(false, false);
			for (Object source : sources) {
				if (source instanceof Class<?>) {
					this.classNames.add(((Class<?>) source).getName());
				}
			}
		}

		@Override
		protected boolean matchClassName(String className) {
			return this.classNames.contains(className);
		}
	}

}
