package org.simpleframework.aop;

import lombok.Getter;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.simpleframework.aop.aspect.AspectInfo;
import org.simpleframework.util.ValidationUtil;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 切面队列，用来执行横切逻辑，织入
 */
public class AspectListExecutor implements MethodInterceptor {

    // 被代理的类
    private Class<?> targetClass;
    // 切面实现类的封装信息，包含切面对象和执行顺序信息
    @Getter
    private List<AspectInfo> sortedAspectInfoList;

    public AspectListExecutor(Class<?> targetClass, List<AspectInfo> aspectInfoList) {
        this.targetClass = targetClass;
        // 对aspectInfoList中的横切对象进行排序处理
        this.sortedAspectInfoList = sortAspectInfoList(aspectInfoList);
    }

    /**
     * 按照order的值进行升序排序，确保order值小的aspect先织入
     *
     * @param aspectInfoList
     * @return
     */
    private List<AspectInfo> sortAspectInfoList(List<AspectInfo> aspectInfoList) {

        Collections.sort(aspectInfoList, new Comparator<AspectInfo>() {
            @Override
            public int compare(AspectInfo o1, AspectInfo o2) {
                return o1.getOrderIndex() - o2.getOrderIndex();
            }
        });

        return aspectInfoList;
    }

    /**
     * 代理逻辑
     *
     * @param proxy       动态代理对象的引用
     * @param method      被代理的目标方法
     * @param args        被代理的目标方法对应的参数列表
     * @param methodProxy 被代理方法的代理方法对象实例
     * @throws Throwable
     */
    @Override
    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Object returnValue = null;
        if (ValidationUtil.isEmpty(sortedAspectInfoList)) {
            return returnValue;
        }
        // 1.按照order的顺序升序执行完所有Aspect的before方法
        invokeBeforeAdvices(method, args);
        try {
            // 2.执行被代理类的方法
            returnValue = methodProxy.invokeSuper(proxy, args);
            // 3.如果正常执行，被代理方法正常返回，则按照order的顺序降序执行完所有Aspect的afterReturning方法
            returnValue = invokeAfterReturningAdvices(method, args, returnValue);
        } catch (Exception e) {
            // 4.如果被代理的方法抛出了异常，按照rder的顺序降序执行完所有Aspect的afterThrowing方法
            invokeAfterThrowingAdvices(method, args, e);
        }
        return returnValue;
    }

    /**
     * 如果被代理的方法抛出了异常，按照rder的顺序降序执行完所有Aspect的afterThrowing方法
     *
     * @param method 被代理的目标方法
     * @param args   被代理的目标方法对应的参数列表
     * @param e      异常信息
     */
    private void invokeAfterThrowingAdvices(Method method, Object[] args, Exception e) throws Throwable {

        for (int i = sortedAspectInfoList.size() - 1; i >= 0; i--) {
            sortedAspectInfoList.get(i).getAspectObject().afterThrowing(targetClass, method, args, e);
        }

    }

    /**
     * 如果正常执行，被代理方法正常返回，则按照order的顺序降序执行完所有Aspect的afterReturning方法
     *
     * @param method      被代理的目标方法
     * @param args        被代理的目标方法对应的参数列表
     * @param returnValue 被代理的目标方法执行后产生的返回值
     */
    private Object invokeAfterReturningAdvices(Method method, Object[] args, Object returnValue) throws Throwable {
        Object returnValueAfterInvoke = null;
        // 由于是后置方法，所以按照降序来执行
        for (int i = sortedAspectInfoList.size() - 1; i >= 0; i--) {
            returnValueAfterInvoke = sortedAspectInfoList.get(i).getAspectObject().afterReturning(targetClass, method, args, returnValue);
        }

        return returnValueAfterInvoke;
    }

    /**
     * 按照order的顺序升序执行完所有Aspect的before方法
     *
     * @param method 被代理的目标方法
     * @param args   被代理的目标方法对应的参数列表
     */
    private void invokeBeforeAdvices(Method method, Object[] args) throws Throwable {

        for (AspectInfo aspectInfo : sortedAspectInfoList) {
            aspectInfo.getAspectObject().before(targetClass, method, args);
        }

    }
}
