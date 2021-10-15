package org.springframework.learn.tx;

public interface AccountService {
    // 转账
    public void transfer(String outUser, String inUser, int money);
}