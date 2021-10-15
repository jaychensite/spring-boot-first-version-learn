import lombok.extern.slf4j.Slf4j;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Advisor;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.aop.framework.*;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MyCustomAopProxyFactory extends DefaultAopProxyFactory {


    public AopProxy createAopProxy(final AdvisedSupport config) throws AopConfigException {
        Class targetClass = config.getTargetClass();
        if (targetClass == null) {
            throw new AopConfigException("TargetSource cannot determine target class: " +
                    "Either an interface or a target is required for proxy creation.");
        }
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        final Class<?>[] targetInterfaces = ClassUtils.getAllInterfacesForClass(targetClass, classLoader);

        if (targetInterfaces.length != 0) {
            return new AopProxy() {
                @Override
                public Object getProxy() {
                    return Proxy.newProxyInstance(classLoader, targetInterfaces, new MyInvocationHandler(config));
                }

                @Override
                public Object getProxy(ClassLoader classLoader) {
                    return null;
                }
            };
        }

        return null;

    }

    public static class MyInvocationHandler implements InvocationHandler {
        AdvisedSupport config;

        public MyInvocationHandler(AdvisedSupport config) {
            this.config = config;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Object target = config.getTargetSource().getTarget();
            Advisor[] advisors = config.getAdvisors();

            List<Object> interceptors = new ArrayList<Object>(3);
            for (Advisor advisor : advisors) {
                if (!(advisor instanceof PointcutAdvisor)) {
                    continue;
                }
                PointcutAdvisor pointcutAdvisor = (PointcutAdvisor) advisor;

                Pointcut pointcut = pointcutAdvisor.getPointcut();
                //判断是否能匹配切点
                boolean matches = pointcut.getClassFilter().matches(target.getClass());
                if (!matches) {
                    log.warn("class  not match");
                    continue;
                }

                //判断方法是否能匹配切点
                MethodMatcher methodMatcher = pointcut.getMethodMatcher();
                boolean methodMatches = methodMatcher.matches(method, target.getClass());
                if (!methodMatches) {
                    log.warn("method not match");
                    continue;
                }

                Advice advice = advisor.getAdvice();
                if (advice instanceof MethodInterceptor) {
                    interceptors.add((MethodInterceptor) advice);
                }
            }

            if (CollectionUtils.isEmpty(interceptors)) {
                //直接调用目标
                return null;
            }

            MyReflectiveMethodInvocation invocation = new MyReflectiveMethodInvocation(proxy, target, method, args, target.getClass(), interceptors);
            Object o = invocation.proceed();

            return o;
        }
    }


    public static class MyReflectiveMethodInvocation extends ReflectiveMethodInvocation {

        /**
         * Construct a new ReflectiveMethodInvocation with the given arguments.
         *
         * @param proxy                                the proxy object that the invocation was made on
         * @param target                               the target object to invoke
         * @param method                               the method to invoke
         * @param arguments                            the arguments to invoke the method with
         * @param targetClass                          the target class, for MethodMatcher invocations
         * @param interceptorsAndDynamicMethodMatchers interceptors that should be applied,
         *                                             along with any InterceptorAndDynamicMethodMatchers that need evaluation at runtime.
         *                                             MethodMatchers included in this struct must already have been found to have matched
         *                                             as far as was possibly statically. Passing an array might be about 10% faster,
         */
        public MyReflectiveMethodInvocation(Object proxy, Object target, Method method, Object[] arguments, Class targetClass, List<Object> interceptorsAndDynamicMethodMatchers) {
            super(proxy, target, method, arguments, targetClass, interceptorsAndDynamicMethodMatchers);
        }
    }

}
