package org.springframework.test.custom;

import org.springframework.stereotype.Controller;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Controller
public @interface DubboExportService {
}
