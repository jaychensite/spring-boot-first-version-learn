package org.springframework.bootstrap.sample.simple.dto;

import lombok.Data;


@Data
public class SpringDefinition {

    /**
     * bean class名
     */
    String beanClassName;

    /**
     * 工厂bean的名称
     */
    String factoryBeanName;

    /**
     * 工厂方法的名称
     */
    String factoryMethodName;

    /**
     * singleton/prototype等
     */
    String scope;

    /**
     * 是否延迟初始化
     */
    boolean isLazyInit;

    /**
     * 依赖的bean
     */
    String[] dependsOn;

    /**
     * bean的角色，比如：1：框架；2：应用
     */
    int role;

    /**
     * 是否为主候选bean
     */
    boolean primary;


}
