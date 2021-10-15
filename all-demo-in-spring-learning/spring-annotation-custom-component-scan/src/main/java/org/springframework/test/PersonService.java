package org.springframework.test;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.custom.annotation.MyComponent;
import org.springframework.custom.annotation.MyComponentScan;
import org.springframework.stereotype.Component;

@MyComponent
@MyComponentScan(value = "org.springframework.test1")
public class PersonService {
    private String personname1;
}
