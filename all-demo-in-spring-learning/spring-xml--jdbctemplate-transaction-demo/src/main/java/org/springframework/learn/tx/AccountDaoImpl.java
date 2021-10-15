package org.springframework.learn.tx;

import org.springframework.jdbc.core.JdbcTemplate;

public class AccountDaoImpl implements AccountDao {
    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 汇款的实现方法
    @Override
    public void out(String outUser, int money) {
        this.jdbcTemplate.update("update account set money =money-?"
                + " where username =?", money, outUser);
    }

    // 收款的实现方法
    @Override
    public void in(String inUser, int money) {
        this.jdbcTemplate.update("update account set money =money+?"
                + " where username =?", money, inUser);
    }
}