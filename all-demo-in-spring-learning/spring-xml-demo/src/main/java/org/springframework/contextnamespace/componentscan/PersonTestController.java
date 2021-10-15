package org.springframework.contextnamespace.componentscan;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.contextnamespace.DerivedComponent;

import javax.annotation.Resource;

@Slf4j
//@Controller
@Data
@DerivedComponent
public class PersonTestController {

//    @Autowired
    @Resource
    private PersonService personService;
}
