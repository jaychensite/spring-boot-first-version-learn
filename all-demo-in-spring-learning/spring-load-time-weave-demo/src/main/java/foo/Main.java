package foo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public final class Main {

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext(
                "context-namespace-test-load-time-weave.xml");
//        StubEntitlementCalculationService entitlementCalculationService
//                = ctx.getBean(StubEntitlementCalculationService.class);
        /**
         * 注意，这里直接new的
         */
        StubEntitlementCalculationService entitlementCalculationService = new StubEntitlementCalculationService();

        // the profiling aspect is 'woven' around this method execution
        entitlementCalculationService.calculateEntitlement();
    }
}
