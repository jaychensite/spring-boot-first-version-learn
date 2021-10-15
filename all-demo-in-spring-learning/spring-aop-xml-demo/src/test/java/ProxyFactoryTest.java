import foo.Perform;
import foo.Performer;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.impl.SLF4JLocationAwareLog;
import org.junit.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.interceptor.PerformanceMonitorInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Slf4j
public class ProxyFactoryTest {

    @Test
    public void createCglibProxy() {
        ProxyFactory proxyFactory = new ProxyFactory();
        Performer performer = new Performer();
        proxyFactory.setTarget(performer);

        Performer proxy = (Performer) proxyFactory.getProxy();

        log.info("proxy class:{}",proxy.getClass().getName());
        proxy.sing();
        log.info("proxy:{}",proxy);
    }


    @Test
    public void createJdkDynamicProxy() {
        ProxyFactory proxyFactory = new ProxyFactory();
        Performer performer = new Performer();
        proxyFactory.setTarget(performer);

        proxyFactory.addInterface(Perform.class);

        Perform proxy = (Perform) proxyFactory.getProxy();

        log.info("proxy class:{}",proxy.getClass().getName());
        proxy.sing();
        log.info("proxy:{}",proxy);
    }



    @Test
    public void createJdkDynamicProxyWithAdvisor() {
        ProxyFactory proxyFactory = new ProxyFactory();
        Performer performer = new Performer();
        proxyFactory.setTarget(performer);

        proxyFactory.addInterface(Perform.class);
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        advisor.setAdvice(new MethodInterceptor() {
            @Override
            public Object invoke(MethodInvocation invocation) throws Throwable {
                Object result = invocation.proceed();
                Method method = invocation.getMethod();
                if (method.getName().equals("sing")) {
                    System.out.println("男孩唱完要行礼");
                }
                return result;
            }
        });


//        advisor.setAdvice(new PerformanceMonitorInterceptor());
        proxyFactory.addAdvisor(advisor);

        Perform proxy = (Perform) proxyFactory.getProxy();

        ProxyFactoryTest.log.info("proxy class:{}",proxy.getClass().getName());
        proxy.sing();
//        log.info("proxy:{}",proxy);
    }

    @Test
    public  void createJdkDynamicProxyManual() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Performer performer = new Performer();
        MyCustomInvocationHandler myCustomInvocationHandler = new MyCustomInvocationHandler(performer);

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Object generatedProxy = Proxy.newProxyInstance(loader,
                new Class[]{Perform.class}, myCustomInvocationHandler);

        ((Perform)generatedProxy).sing();

    }


    @Test
    public  void createProxyWithCustomProxyFactory() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setAopProxyFactory(new MyCustomAopProxyFactory());

        Performer performer = new Performer();
        proxyFactory.setTarget(performer);

        proxyFactory.addInterface(Perform.class);
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        advisor.setAdvice(new MethodInterceptor() {
            @Override
            public Object invoke(MethodInvocation invocation) throws Throwable {
                Object result = invocation.proceed();

                Method method = invocation.getMethod();
                if (method.getName().equals("sing")) {
                    System.out.println("男孩唱完要行礼");
                }

                return result;
            }
        });


//        advisor.setAdvice(new PerformanceMonitorInterceptor());
        proxyFactory.addAdvisor(advisor);

        Perform proxy = (Perform) proxyFactory.getProxy();

        ProxyFactoryTest.log.info("proxy class:{}",proxy.getClass().getName());
        proxy.sing();

    }

    public static class MyCustomInvocationHandler implements InvocationHandler {
        Performer performer;

        public MyCustomInvocationHandler(Performer performer) {
            this.performer = performer;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("我是一个称职的代理");
            return method.invoke(performer,args);
        }
    }

}
