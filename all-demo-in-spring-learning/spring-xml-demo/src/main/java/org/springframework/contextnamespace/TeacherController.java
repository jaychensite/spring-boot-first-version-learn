package org.springframework.contextnamespace;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Created by Administrator on 2020/1/17.
 */
@Controller
@Data
@ShouldExclude
public class TeacherController {
    @Autowired
    private TeacherService teacherService;


}
