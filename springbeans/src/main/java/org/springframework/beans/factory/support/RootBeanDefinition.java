/*
 * Copyright 2002-2013 the original author or authors.
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

package org.springframework.beans.factory.support;

import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.util.Assert;

/**
 * A root bean definition represents the merged bean definition that backs
 * a specific bean in a Spring BeanFactory at runtime.
 * 一个顶级bean definition代表了一个合并后的bean definition，在运行时，就在背后默默支撑一个
 * 特定bean。
 * It might have been created from multiple original bean definitions that inherit from each other,
 * typically registered as {@link GenericBeanDefinition GenericBeanDefinitions}.
 * A root bean definition is essentially the 'unified' bean definition view at runtime.
 * 它可能从多个原始的，相互继承的bean definition创建而来，一般被注册为{@link GenericBeanDefinition GenericBeanDefinitions}
 * 一个root bean definition 是在程序运行时的一个统一的、通用的bean definition视图。
 *
 * <p>Root bean definitions may also be used for registering individual bean definitions
 * in the configuration phase.
 * root bean definition也可能被用来注册单独的bean definition在配置阶段。
 *
 * However, since Spring 2.5, the preferred way to register
 * bean definitions programmatically is the {@link GenericBeanDefinition} class.
 * GenericBeanDefinition has the advantage that it allows to dynamically define
 * parent dependencies, not 'hard-coding' the role as a root bean definition.
 * 然而，从spring 2.5后，更推荐的编程方式注册bean definition的方式是通过 {@link GenericBeanDefinition}
 * {@link GenericBeanDefinition} 的优势在于，它允许动态定义父依赖，而不是硬编码。
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @see GenericBeanDefinition
 * @see ChildBeanDefinition
 */
@SuppressWarnings("serial")
public class RootBeanDefinition extends AbstractBeanDefinition {

	private final Set<Member> externallyManagedConfigMembers =
			Collections.newSetFromMap(new ConcurrentHashMap<Member, Boolean>(0));

	private final Set<String> externallyManagedInitMethods =
			Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>(0));

	private final Set<String> externallyManagedDestroyMethods =
			Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>(0));

	private BeanDefinitionHolder decoratedDefinition;

	boolean allowCaching = true;

	private volatile Class<?> targetType;

	boolean isFactoryMethodUnique = false;

	final Object constructorArgumentLock = new Object();

	/** Package-visible field for caching the resolved constructor or factory method */
	Object resolvedConstructorOrFactoryMethod;

	/** Package-visible field that marks the constructor arguments as resolved */
	boolean constructorArgumentsResolved = false;

	/** Package-visible field for caching fully resolved constructor arguments */
	Object[] resolvedConstructorArguments;

	/** Package-visible field for caching partly prepared constructor arguments */
	Object[] preparedConstructorArguments;

	final Object postProcessingLock = new Object();

	/** Package-visible field that indicates MergedBeanDefinitionPostProcessor having been applied */
	boolean postProcessed = false;

	/** Package-visible field that indicates a before-instantiation post-processor having kicked in */
	volatile Boolean beforeInstantiationResolved;


	/**
	 * Create a new RootBeanDefinition, to be configured through its bean
	 * properties and configuration methods.
	 * @see #setBeanClass
	 * @see #setBeanClassName
	 * @see #setScope
	 * @see #setAutowireMode
	 * @see #setDependencyCheck
	 * @see #setConstructorArgumentValues
	 * @see #setPropertyValues
	 */
	public RootBeanDefinition() {
		super();
	}

	/**
	 * Create a new RootBeanDefinition for a singleton.
	 * @param beanClass the class of the bean to instantiate
	 */
	public RootBeanDefinition(Class beanClass) {
		super();
		setBeanClass(beanClass);
	}

	/**
	 * Create a new RootBeanDefinition with the given singleton status.
	 * @param beanClass the class of the bean to instantiate
	 * @param singleton the singleton status of the bean
	 * @deprecated since Spring 2.5, in favor of {@link #setScope}
	 */
	@Deprecated
	public RootBeanDefinition(Class beanClass, boolean singleton) {
		super();
		setBeanClass(beanClass);
		setSingleton(singleton);
	}

	/**
	 * Create a new RootBeanDefinition for a singleton,
	 * using the given autowire mode.
	 * @param beanClass the class of the bean to instantiate
	 * @param autowireMode by name or type, using the constants in this interface
	 * @deprecated as of Spring 3.0, in favor of {@link #setAutowireMode} usage
	 */
	@Deprecated
	public RootBeanDefinition(Class beanClass, int autowireMode) {
		super();
		setBeanClass(beanClass);
		setAutowireMode(autowireMode);
	}

	/**
	 * Create a new RootBeanDefinition for a singleton,
	 * using the given autowire mode.
	 * @param beanClass the class of the bean to instantiate
	 * @param autowireMode by name or type, using the constants in this interface
	 * @param dependencyCheck whether to perform a dependency check for objects
	 * (not applicable to autowiring a constructor, thus ignored there)
	 */
	public RootBeanDefinition(Class beanClass, int autowireMode, boolean dependencyCheck) {
		super();
		setBeanClass(beanClass);
		setAutowireMode(autowireMode);
		if (dependencyCheck && getResolvedAutowireMode() != AUTOWIRE_CONSTRUCTOR) {
			setDependencyCheck(RootBeanDefinition.DEPENDENCY_CHECK_OBJECTS);
		}
	}

	/**
	 * Create a new RootBeanDefinition for a singleton,
	 * providing property values.
	 * @param beanClass the class of the bean to instantiate
	 * @param pvs the property values to apply
	 * @deprecated as of Spring 3.0, in favor of {@link #getPropertyValues} usage
	 */
	@Deprecated
	public RootBeanDefinition(Class beanClass, MutablePropertyValues pvs) {
		super(null, pvs);
		setBeanClass(beanClass);
	}

	/**
	 * Create a new RootBeanDefinition with the given singleton status,
	 * providing property values.
	 * @param beanClass the class of the bean to instantiate
	 * @param pvs the property values to apply
	 * @param singleton the singleton status of the bean
	 * @deprecated since Spring 2.5, in favor of {@link #setScope}
	 */
	@Deprecated
	public RootBeanDefinition(Class beanClass, MutablePropertyValues pvs, boolean singleton) {
		super(null, pvs);
		setBeanClass(beanClass);
		setSingleton(singleton);
	}

	/**
	 * Create a new RootBeanDefinition for a singleton,
	 * providing constructor arguments and property values.
	 * @param beanClass the class of the bean to instantiate
	 * @param cargs the constructor argument values to apply
	 * @param pvs the property values to apply
	 */
	public RootBeanDefinition(Class beanClass, ConstructorArgumentValues cargs, MutablePropertyValues pvs) {
		super(cargs, pvs);
		setBeanClass(beanClass);
	}

	/**
	 * Create a new RootBeanDefinition for a singleton,
	 * providing constructor arguments and property values.
	 * <p>Takes a bean class name to avoid eager loading of the bean class.
	 * @param beanClassName the name of the class to instantiate
	 */
	public RootBeanDefinition(String beanClassName) {
		setBeanClassName(beanClassName);
	}

	/**
	 * Create a new RootBeanDefinition for a singleton,
	 * providing constructor arguments and property values.
	 * <p>Takes a bean class name to avoid eager loading of the bean class.
	 * @param beanClassName the name of the class to instantiate
	 * @param cargs the constructor argument values to apply
	 * @param pvs the property values to apply
	 */
	public RootBeanDefinition(String beanClassName, ConstructorArgumentValues cargs, MutablePropertyValues pvs) {
		super(cargs, pvs);
		setBeanClassName(beanClassName);
	}

	/**
	 * Create a new RootBeanDefinition as deep copy of the given
	 * bean definition.
	 * @param original the original bean definition to copy from
	 */
	public RootBeanDefinition(RootBeanDefinition original) {
		this((BeanDefinition) original);
	}

	/**
	 * Create a new RootBeanDefinition as deep copy of the given
	 * bean definition.
	 * @param original the original bean definition to copy from
	 */
	RootBeanDefinition(BeanDefinition original) {
		super(original);
		if (original instanceof RootBeanDefinition) {
			RootBeanDefinition originalRbd = (RootBeanDefinition) original;
			this.decoratedDefinition = originalRbd.decoratedDefinition;
			this.allowCaching = originalRbd.allowCaching;
			this.targetType = originalRbd.targetType;
			this.isFactoryMethodUnique = originalRbd.isFactoryMethodUnique;
		}
	}


	public String getParentName() {
		return null;
	}

	public void setParentName(String parentName) {
		if (parentName != null) {
			throw new IllegalArgumentException("Root bean cannot be changed into a child bean with parent reference");
		}
	}

	/**
	 * Specify the target type of this bean definition, if known in advance.
	 */
	public void setTargetType(Class<?> targetType) {
		this.targetType = targetType;
	}

	/**
	 * Return the target type of this bean definition, if known
	 * (either specified in advance or resolved on first instantiation).
	 */
	public Class<?> getTargetType() {
		return this.targetType;
	}

	/**
	 * Specify a factory method name that refers to a non-overloaded method.
	 */
	public void setUniqueFactoryMethodName(String name) {
		Assert.hasText(name, "Factory method name must not be empty");
		setFactoryMethodName(name);
		this.isFactoryMethodUnique = true;
	}

	/**
	 * Check whether the given candidate qualifies as a factory method.
	 */
	public boolean isFactoryMethod(Method candidate) {
		return (candidate != null && candidate.getName().equals(getFactoryMethodName()));
	}

	/**
	 * Return the resolved factory method as a Java Method object, if available.
	 * @return the factory method, or {@code null} if not found or not resolved yet
	 */
	public Method getResolvedFactoryMethod() {
		synchronized (this.constructorArgumentLock) {
			Object candidate = this.resolvedConstructorOrFactoryMethod;
			return (candidate instanceof Method ? (Method) candidate : null);
		}
	}


	public void registerExternallyManagedConfigMember(Member configMember) {
		this.externallyManagedConfigMembers.add(configMember);
	}

	public boolean isExternallyManagedConfigMember(Member configMember) {
		return this.externallyManagedConfigMembers.contains(configMember);
	}

	public void registerExternallyManagedInitMethod(String initMethod) {
		this.externallyManagedInitMethods.add(initMethod);
	}

	public boolean isExternallyManagedInitMethod(String initMethod) {
		return this.externallyManagedInitMethods.contains(initMethod);
	}

	public void registerExternallyManagedDestroyMethod(String destroyMethod) {
		this.externallyManagedDestroyMethods.add(destroyMethod);
	}

	public boolean isExternallyManagedDestroyMethod(String destroyMethod) {
		return this.externallyManagedDestroyMethods.contains(destroyMethod);
	}

	public void setDecoratedDefinition(BeanDefinitionHolder decoratedDefinition) {
		this.decoratedDefinition = decoratedDefinition;
	}

	public BeanDefinitionHolder getDecoratedDefinition() {
		return this.decoratedDefinition;
	}


	@Override
	public RootBeanDefinition cloneBeanDefinition() {
		return new RootBeanDefinition(this);
	}

	@Override
	public boolean equals(Object other) {
		return (this == other || (other instanceof RootBeanDefinition && super.equals(other)));
	}

	@Override
	public String toString() {
		return "Root bean: " + super.toString();
	}

}
