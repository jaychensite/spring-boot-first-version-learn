package org.springframework.learn.tx;


public interface AccountDao {
    // 汇款
    public void out(String outUser, int money);

    // 收款
    public void in(String inUser, int money);
}