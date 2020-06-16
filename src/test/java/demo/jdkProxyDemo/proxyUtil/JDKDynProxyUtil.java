package demo.jdkProxyDemo.proxyUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * 用来创建代理类的工具类
 */
public class JDKDynProxyUtil {

    /**
     * 获取目标对象的动态代理对象
     * @param targetObject
     * @param handler
     * @param <T>
     * @return
     */
    public static <T>T newProxyInstance(T targetObject, InvocationHandler handler){
        // 获取Proxy类的newProxyInstance必须的参数
        ClassLoader classLoader = targetObject.getClass().getClassLoader();
        Class<?>[] interfaces = targetObject.getClass().getInterfaces();
        return (T)Proxy.newProxyInstance(classLoader, interfaces, handler);
    }

}
