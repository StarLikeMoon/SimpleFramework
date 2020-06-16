package demo.jdkProxyDemo.cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

public class CglibUtil {

    public static <T>T createProxy(T targetObject, MethodInterceptor methodInterceptor){

       return (T)Enhancer.create(targetObject.getClass(), methodInterceptor);

    }

}
