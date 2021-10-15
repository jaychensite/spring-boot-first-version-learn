package org.springframework.simple;

import lombok.Data;

/**
 * desc:
 *
 * @author : caokunliang
 * creat_date: 2019/12/23 0023
 * creat_time: 11:15
 **/
@Data
public class Employee {
    /**
     * 员工组名
     */
    private String group;
    /**
     * 是否配电话
     */
    private Boolean usesDialUp;

    /**
     * 部门
     */
    private String department;

    /**
     * 经理
     */
    private Employee manager;

}
