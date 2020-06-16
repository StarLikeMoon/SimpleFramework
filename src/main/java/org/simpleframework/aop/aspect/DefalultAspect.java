package org.simpleframework.aop.aspect;

import java.lang.reflect.Method;

/**
 * 横切框架抽象类，定义了支持几种横切逻辑
 * 这里没有使用@before等注解来实现，而是使用抽象类钩子方法的方式
 * 实现简单成本低，主要学习设计思路
 */
public abstract class DefalultAspect {

    /**
     * 事前拦截
     *
     * @param targetClass 被代理的目标类
     * @param method      被代理的目标方法
     * @param args        被代理的目标方法对应的参数列表
     * @throws Throwable 异常签名
     */
    public void before(Class<?> targetClass, Method method, Object[] args) throws Throwable {

    }

    /**
     * 事后拦截
     *
     * @param targetClass 被代理的目标类
     * @param method      被代理的目标方法
     * @param args        被代理的目标方法对应的参数列表
     * @param returnValue 被代理的目标方法执行后的返回值
     * @throws Throwable 异常签名
     */
    public Object afterReturning(Class<?> targetClass, Method method,
                                 Object[] args, Object returnValue) throws Throwable {
        return returnValue;
    }

    /**
     * 异常拦截
     *
     * @param targetClass 被代理的目标类
     * @param method      被代理的目标方法
     * @param args        被代理的目标方法对应的参数列表
     * @param e           被代理的目标方法抛出的异常
     * @throws Throwable
     */
    public void afterThrowing(Class<?> targetClass, Method method,
                              Object[] args, Throwable e) throws Throwable {

    }

}
