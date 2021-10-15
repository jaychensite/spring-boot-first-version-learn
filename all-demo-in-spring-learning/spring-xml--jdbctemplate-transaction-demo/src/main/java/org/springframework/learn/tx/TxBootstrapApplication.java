package org.springframework.learn.tx;


import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

public class TxBootstrapApplication {



	public static void main(String[] args) throws Exception {
// 获得Spring容器，并操作
		String xmlPath = "application-context.xml";
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				xmlPath);
		final AccountService accountService = (AccountService) applicationContext
				.getBean("accountService");
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				accountService.transfer("zhangsan", "lisi", 50);
			}
		});
		thread.setName("test-thread");
		thread.start();

		accountService.transfer("zhangsan", "lisi", 100);
		LockSupport.park();
	}
}
