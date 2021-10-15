package foo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public final class Main {

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext(
                "context-namespace-test-spring-configured.xml");
        StubEntitlementCalculationService bean = ctx.getBean(StubEntitlementCalculationService.class);
        System.out.println(bean);

        Account account = new Account();
        System.out.println(account);
    }
}
