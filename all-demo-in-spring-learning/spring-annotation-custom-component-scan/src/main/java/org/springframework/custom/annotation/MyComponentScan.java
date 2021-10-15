package org.springframework.custom.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyComponentScan {

    /*
     * 要扫描的包名
     */
    String value();

}
