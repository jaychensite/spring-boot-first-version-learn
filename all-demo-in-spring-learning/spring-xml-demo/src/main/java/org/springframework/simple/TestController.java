package org.springframework.simple;

import org.springframework.beans.factory.annotation.Autowired;

public class TestController {

    TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }
}
