package org.springframework.simple.byconstructor;

import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.simple.TestService;

//@ToString
public class TestControllerByConstructor {

    TestService testService;

    /**
     * 基本类型依赖
     */
    private String name;


    public TestControllerByConstructor(TestService testService, String name) {
        this.testService = testService;
        this.name = name;
    }

    public TestService getTestService() {
        return testService;
    }

    public String getName() {
        return name;
    }
}
