package foo;

import org.springframework.aop.aspectj.AspectJAfterAdvice;
import org.springframework.aop.config.SimpleBeanFactoryAwareAspectInstanceFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.MyFastJson;

import java.util.List;

public final class Main {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
                "context-namespace-test-aop.xml");

        Egg egg = (Egg) ctx.getBean(Egg.class);
        egg.incubate();
        System.out.println(egg.getClass());

        Chick chick = ctx.getBean(Chick.class);
        System.out.println(chick.getEgg().getClass());
    }
}
