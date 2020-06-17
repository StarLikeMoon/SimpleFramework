package org.simpleframework.aop;

import com.sun.corba.se.spi.ior.ObjectKey;
import org.simpleframework.aop.annotation.Aspect;
import org.simpleframework.aop.annotation.Order;
import org.simpleframework.aop.aspect.AspectInfo;
import org.simpleframework.aop.aspect.DefalultAspect;
import org.simpleframework.aop.aspectJ.PointcutLocator;
import org.simpleframework.core.BeanContainer;
import org.simpleframework.util.ValidationUtil;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 将横切逻辑织入到被代理的对象中，AOP2.0版本，利用AspectJ
 */
public class AspectWeaver {

    private BeanContainer beanContainer;

    public AspectWeaver() {
        // 获取容器，容器是绝对单例的
        beanContainer = BeanContainer.getInstance();
    }

    public void doAop() {
        // 1.获取所有的切面类
        Set<Class<?>> aspectSet = beanContainer.getClassesByAnnotation(Aspect.class);
        if (ValidationUtil.isEmpty(aspectSet)) {
            return;
        }
        // 2.拼装AspectInfoList
        List<AspectInfo> aspectInfoList = packAspectInfoList(aspectSet);
        // 3.遍历容器里的类
        Set<Class<?>> classSet = beanContainer.getClasses();
        for (Class<?> targetClass : classSet) {
            // 排除bean是AspectClass
            if (targetClass.isAnnotationPresent(Aspect.class)) {
                continue;
            }
            // 4.初筛符合条件的Aspect
            List<AspectInfo> roughMatchedAspectList = collectRoughMatchedAspectListForSpecificClass(aspectInfoList, targetClass);
            if (ValidationUtil.isEmpty(roughMatchedAspectList)) {
                continue;
            }
            // 5.尝试进行Aspect的织入
            wrapIfNecessary(roughMatchedAspectList, targetClass);
        }

    }

    /**
     * 尝试进行Aspect的织入
     * @param roughMatchedAspectList
     * @param targetClass
     */
    private void wrapIfNecessary(List<AspectInfo> roughMatchedAspectList, Class<?> targetClass) {
        // 创建动态代理对象
        AspectListExecutor executor = new AspectListExecutor(targetClass, roughMatchedAspectList);
        Object proxyBean = ProxyCreator.createProxy(targetClass, executor);
        // 替换被代理的对象
        beanContainer.addBean(targetClass, proxyBean);
    }

    /**
     * 初筛符合条件的Aspect
     * @param aspectInfoList
     * @param targetClass
     * @return
     */
    private List<AspectInfo> collectRoughMatchedAspectListForSpecificClass(List<AspectInfo> aspectInfoList, Class<?> targetClass) {
        List<AspectInfo> roughMatchedAspectList = new ArrayList<>();
        for (AspectInfo aspectInfo : aspectInfoList) {
            if (aspectInfo.getPointcutLocator().roughMatches(targetClass)) {
                roughMatchedAspectList.add(aspectInfo);
            }
        }
        return roughMatchedAspectList;
    }

    /**
     * 根据获取到的所有的切面类拼装AspectInfoList
     *
     * @param aspectSet
     * @return
     */
    private List<AspectInfo> packAspectInfoList(Set<Class<?>> aspectSet) {
        List<AspectInfo> aspectInfoList = new ArrayList<>();
        for (Class<?> aspectClass : aspectSet) {
            if (verifyAspect(aspectClass)) {
                // 1.获取aspectClass中的order和Aspect标签中的属性
                Order orderTag = aspectClass.getAnnotation(Order.class);
                Aspect aspectTag = aspectClass.getAnnotation(Aspect.class);
                // 2.创建PointcutLocator
                PointcutLocator pointcutLocator = new PointcutLocator(aspectTag.pointcut());
                // 3.从容器中获取切面类的实例
                DefalultAspect aspect = (DefalultAspect) beanContainer.getBean(aspectClass);
                // 4.创建AspectInfo封装类
                AspectInfo aspectInfo = new AspectInfo(orderTag.value(), aspect, pointcutLocator);
                aspectInfoList.add(aspectInfo);
            } else {
                throw new RuntimeException("@Aspect and @Order have not been added to the Aspect class, " +
                        "or Aspect class does not extend from DefaultAspect");
            }
        }
        return aspectInfoList;
    }

    /**
     * 用来验证Aspect类是否满足自定义切面类的要求
     * 首先一定要遵守给Aspect类添加@Aspect标签和@Order标签的规范
     * 同时，一定要继承自DefaultAspect.class
     * 此外，@Aspect的属性不能是它本身
     *
     * @param aspectClass
     * @return
     */
    private boolean verifyAspect(Class<?> aspectClass) {

        return aspectClass.isAnnotationPresent(Aspect.class) &&
                aspectClass.isAnnotationPresent(Order.class) &&
                DefalultAspect.class.isAssignableFrom(aspectClass);
    }

}
