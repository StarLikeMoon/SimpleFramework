package demo.jdkProxyDemo.cglib;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class AlipayMethodInterceptor implements MethodInterceptor {

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        beforePay();
        Object result = methodProxy.invokeSuper(o, objects);
        afterPay();
        return result;

    }

    private void beforePay(){
        System.out.println("帮助使用者从银行取款");
    }

    private void afterPay(){
        System.out.println("银行转账给收款方");
    }
}
