package org.springframework.contextnamespace;

import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

@Component
@Import({MainClassForTestAnnotationConfig.class})
public class PersonService {
    private String personname1;
}
