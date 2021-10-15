package foo;

public final class Main {

    public static void main(String[] args) {
        /**
         * 注意，这里直接new的
         */
        StubEntitlementCalculationService entitlementCalculationService = new StubEntitlementCalculationService();

        entitlementCalculationService.calculateEntitlement();
    }
}
