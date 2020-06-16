package demo.jdkProxyDemo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 实现InvocationHandler接口的支付宝横切逻辑封装，相当于切面
 */
public class AlipayInvocationHandler implements InvocationHandler {

    // 被代理的对象，即目标类
    private Object targetObject;

    public AlipayInvocationHandler(Object targetObject){
        this.targetObject = targetObject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 前置织入
        beforePay();

        // 被代理类的方法执行
        Object result = method.invoke(targetObject, args);

        // 后置织入
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
