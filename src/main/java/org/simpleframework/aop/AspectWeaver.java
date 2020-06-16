package org.simpleframework.aop;

import org.simpleframework.aop.annotation.Aspect;
import org.simpleframework.aop.annotation.Order;
import org.simpleframework.aop.aspect.AspectInfo;
import org.simpleframework.aop.aspect.DefalultAspect;
import org.simpleframework.core.BeanContainer;
import org.simpleframework.util.ValidationUtil;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 将横切逻辑织入到被代理的对象中
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
        // 2.将切面类按照不同的织入目标进行切分
        Map<Class<? extends Annotation>, List<AspectInfo>> categorizedMap = new ConcurrentHashMap<>();
        if (ValidationUtil.isEmpty(aspectSet)) {
            return;
        } else {
            for (Class<?> aspectClass : aspectSet) {
                // 对横切类进行校验，通过校验才分类
                if (verifyAspect(aspectClass)) {
                    categorizeAspect(categorizedMap, aspectClass);
                } else {
                    throw new RuntimeException("@Aspect and @Order have not been added to the Aspect class, " +
                            "or Aspect class does not extend from DefaultAspect, " +
                            "or the value in Aspect Tag equals @Aspect");
                }
            }
        }
        // 3.按照不同的织入目标分别去按序织入Aspect的逻辑
        if (ValidationUtil.isEmpty(categorizedMap)) {
            return;
        } else {
            for (Class<? extends Annotation> category : categorizedMap.keySet()) {
                // 遍历categorizedMap中的Key，即需要织入的注解类型，然后一个个按照对应的aspectInfoList进行织入
                weaveByCategory(category, categorizedMap.get(category));
            }
        }
    }

    /**
     * 对目标类进行织入
     *
     * @param category    目标织入注解类型
     * @param aspectInfos 该类型下对应的横切类列表
     */
    private void weaveByCategory(Class<? extends Annotation> category, List<AspectInfo> aspectInfos) {

        // 1.利用注解获取被代理类的集合
        Set<Class<?>> classSet = beanContainer.getClassesByAnnotation(category);
        if (ValidationUtil.isEmpty(classSet)) {
            return;
        } else {
            // 2.遍历被代理类，分别为每个被代理类生成动态代理
            for (Class<?> targetClass : classSet) {
                AspectListExecutor aspectListExecutor = new AspectListExecutor(targetClass, aspectInfos);
                Object proxyBean = ProxyCreator.createProxy(targetClass, aspectListExecutor);
                // 3.将动态代理对象添加到容器中，取代被代理类实例
                beanContainer.addBean(targetClass, proxyBean);
            }
        }

    }

    /**
     * 对切面类按照不同的织入目标进行切分，存入map中
     *
     * @param categorizedMap 用来切分不同织入目标
     * @param aspectClass    切面类
     */
    private void categorizeAspect(Map<Class<? extends Annotation>,
            List<AspectInfo>> categorizedMap, Class<?> aspectClass) {
        // 1.获取aspectClass中的order和Aspect标签中的属性
        Order orderTag = aspectClass.getAnnotation(Order.class);
        Aspect aspectTag = aspectClass.getAnnotation(Aspect.class);
        // 2.从容器中获取切面类的实例
        DefalultAspect aspect = (DefalultAspect) beanContainer.getBean(aspectClass);
        // 3.创建AspectInfo封装类
        AspectInfo aspectInfo = new AspectInfo(orderTag.value(), aspect);
        // 4.填充Map
        if (!categorizedMap.containsKey(aspectTag.value())) {
            // 如果织入的joinpoint第一次出现，则以该joinpoint为key，以新创建的List<AspectInfo>>为value存入map
            List<AspectInfo> aspectInfoList = new ArrayList<>();
            aspectInfoList.add(aspectInfo);
            categorizedMap.put(aspectTag.value(), aspectInfoList);
        } else {
            // 如果织入的joinpoint不是第一次出现，则往joinpoint对应的valueList中添加新的Aspect
            List<AspectInfo> aspectInfoList = categorizedMap.get(aspectTag.value());
            aspectInfoList.add(aspectInfo);
        }

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
                DefalultAspect.class.isAssignableFrom(aspectClass) &&
                aspectClass.getAnnotation(Aspect.class).value() != Aspect.class;

    }
}
