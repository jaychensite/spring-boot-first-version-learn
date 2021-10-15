package org.springframework.simple.byproperty;

import lombok.ToString;
import org.springframework.simple.TestService;

@ToString
public class TestControllerWireByProperty {
    /**
     * bean引用依赖
     */
    private TestService t;

    /**
     * 基本类型依赖
     */
    private String name;

    public void setT(TestService t) {
        this.t = t;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TestService getT() {
        return t;
    }

    public String getName() {
        return name;
    }
}
