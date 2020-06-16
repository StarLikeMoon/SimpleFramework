package demo.jdkProxyDemo;

import demo.jdkProxyDemo.cglib.AlipayMethodInterceptor;
import demo.jdkProxyDemo.cglib.CglibUtil;
import demo.jdkProxyDemo.impl.CommonPayment;
import demo.jdkProxyDemo.impl.ToBPaymentImpl;
import demo.jdkProxyDemo.impl.ToCPaymentImpl;
import demo.jdkProxyDemo.proxyUtil.JDKDynProxyUtil;

import java.lang.reflect.InvocationHandler;

public class ProxyDemo {

//    /**
//     * 通过动态代理来实现横切逻辑
//     * @param args
//     */
//    public static void main(String[] args) {
//
//        ToCPayment toCPayment = new ToCPaymentImpl();
//        InvocationHandler toCInvocationHandler = new AlipayInvocationHandler(toCPayment);
//        ToCPayment toCPaymentProxy = JDKDynProxyUtil.newProxyInstance(toCPayment, toCInvocationHandler);
//        toCPaymentProxy.pay();
//
//        ToBPayment toBPayment = new ToBPaymentImpl();
//
//        InvocationHandler toBInvocationHandler = new AlipayInvocationHandler(toBPayment);
//        ToBPayment toBPaymentProxy = JDKDynProxyUtil.newProxyInstance(toBPayment, toBInvocationHandler);
//        toBPaymentProxy.pay();
//
//    }

    /**
     * 利用Cglib来实现横切逻辑
     * @param args
     */
    public static void main(String[] args) {
        CommonPayment commonPayment = new CommonPayment();
        AlipayMethodInterceptor alipayMethodInterceptor =new AlipayMethodInterceptor();
        CommonPayment commonPaymentProxy = CglibUtil.createProxy(commonPayment, alipayMethodInterceptor);
        commonPaymentProxy.pay();
    }

}
