package org.springframework.bootstrap;

import org.springframework.context.annotation.Configuration;

/**
 * Created by Administrator on 2019/11/19.
 */
public class Test {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ExampleConfig.class);
        application.run();
    }


    @Configuration
    static class ExampleConfig {

    }
}
