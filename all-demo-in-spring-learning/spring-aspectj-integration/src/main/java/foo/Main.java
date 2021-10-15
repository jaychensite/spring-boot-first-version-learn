package foo;

import java.lang.instrument.Instrumentation;

public final class Main {


    public static void main(String[] args) {
        InstrumentationLoadTimeWeaver.init();

        /**
         * 注意，这里直接new的
         */
        StubEntitlementCalculationService entitlementCalculationService = new StubEntitlementCalculationService();

        entitlementCalculationService.calculateEntitlement();
    }
}
