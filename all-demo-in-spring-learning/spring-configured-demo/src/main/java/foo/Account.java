package foo;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable(autowire = Autowire.BY_TYPE)
@Data
public class Account {

    @Autowired
    private StubEntitlementCalculationService stubEntitlementCalculationService;
}
