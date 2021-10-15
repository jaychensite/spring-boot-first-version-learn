package org.springframework.simple;

public class TestByPropertyController {

    TestService testService;

    public void setTestService(TestService testService) {
        this.testService = testService;
    }
}
