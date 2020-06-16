package org.simpleframework.core;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.simpleframework.aop.annotation.Aspect;
import org.simpleframework.core.annotation.Component;
import org.simpleframework.core.annotation.Controller;
import org.simpleframework.core.annotation.Repository;
import org.simpleframework.core.annotation.Service;
import org.simpleframework.util.ClassUtil;
import org.simpleframework.util.ValidationUtil;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 利用枚举饿汉单例模式来防止反射破解单例
 */
@Slf4j
// 利用lombok创建一个私有的构造函数
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanContainer {
    // 存储bean实例的载体
    private final Map<Class<?>, Object> beanMap = new ConcurrentHashMap<>();

    // 加载bean的注解列表，被注解列表标记的bean才会被添加进容器
    private static final List<Class<? extends Annotation>> BEAN_ANNOTATION
            = Arrays.asList(Component.class, Controller.class, Service.class, Repository.class, Aspect.class);

    // 定义私有的枚举类型成员变量，用来实现单例，防止被反射破解
    private enum ContainerHolder {
        HOLDER;
        private BeanContainer instance;

        ContainerHolder() {
            instance = new BeanContainer();
        }
    }

    /**
     * 获取bean容器实例
     *
     * @return
     */
    public static BeanContainer getInstance() {
        return ContainerHolder.HOLDER.instance;
    }

    // 用来判断容器是否已经被加载过
    private boolean loaded = false;

    /**
     * 获取容器加载状态，是否被加载过
     *
     * @return
     */
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * 扫描加载所有Bean，保证线程安全，所以加synchronized
     *
     * @param packName
     */
    public synchronized void loadBeans(String packName) {
        // 判断容器是否已经被加载
        if (isLoaded()) {
            log.warn("BeanContainer has been loaded!");
            return;
        }
        Set<Class<?>> classSet = ClassUtil.extractPackageClass(packName);
        // 判空，看是否获取到对象
        if (ValidationUtil.isEmpty(classSet)) {
            log.warn("extract nothing for packageName " + packName);
            return;
        }
        // 根据BEAN_ANNOTATION注解列表过滤需要加载的类
        for (Class<?> clazz : classSet) {
            for (Class<? extends Annotation> annotation : BEAN_ANNOTATION) {
                // 如果类上面标记了指定的注解就加载进来
                if (clazz.isAnnotationPresent(annotation)) {
                    // 将目标类本身作为键，将目标类的实例作为值，放到beanMap中
                    beanMap.put(clazz, ClassUtil.newInstace(clazz, true));
                }
            }
        }
        loaded = true;
    }

    /**
     * 获取bean实例的数量
     *
     * @return
     */
    public int size() {
        return beanMap.size();
    }

    /**
     * 添加一个class对象及其实例
     *
     * @param clazz class对象
     * @param bean  Bean实例
     * @return 原有的bean实例，没有就返回null
     */
    public Object addBean(Class<?> clazz, Object bean) {
        return beanMap.put(clazz, bean);
    }

    /**
     * 移除一个IOC容器管理的对象
     *
     * @param clazz class对象
     * @return 删除的bean实例，没有就返回null
     */
    public Object removeBean(Class<?> clazz) {
        return beanMap.remove(clazz);
    }

    /**
     * 根据Class对象获取Bean实例
     *
     * @param clazz
     * @return
     */
    public Object getBean(Class<?> clazz) {
        return beanMap.get(clazz);
    }

    /**
     * 获取IOC管理的所有Class对象集合
     *
     * @return
     */
    public Set<Class<?>> getClasses() {
        return beanMap.keySet();
    }

    /**
     * 获取所有的bean集合
     *
     * @return
     */
    public Set<Object> getBeans() {
        return new HashSet<>(beanMap.values());
    }

    /**
     * 根据注解筛选出Bean的Class集合
     *
     * @param annotation
     * @return
     */
    public Set<Class<?>> getClassesByAnnotation(Class<? extends Annotation> annotation) {
        // 1.获取beanMap的所有class对象
        Set<Class<?>> keySet = getClasses();
        if (ValidationUtil.isEmpty(keySet)) {
            log.warn("nothing in beanMap");
            return null;
        }
        // 2.通过注解筛选出备注接标记的class对象，并添加到set中
        Set<Class<?>> classSet = new HashSet<>();
        for (Class<?> clazz : keySet) {
            // 如果有对象的注解是传入的注解条件，就加入set
            if (clazz.isAnnotationPresent(annotation)) {
                classSet.add(clazz);
            }
        }
        return classSet.size() > 0 ? classSet : null;
    }

    /**
     * 通过传入接口或者父类获取实现类或者子类的Class集合，不包括其本身
     *
     * @param interfaceOrClass
     * @return
     */
    public Set<Class<?>> getClassesBySuper(Class<?> interfaceOrClass) {
        // 1.获取beanMap的所有class对象
        Set<Class<?>> keySet = getClasses();
        if (ValidationUtil.isEmpty(keySet)) {
            log.warn("nothing in beanMap");
            return null;
        }
        // 2.通过传入的接口或者父类筛选class对象，并添加到set中
        Set<Class<?>> classSet = new HashSet<>();
        for (Class<?> clazz : keySet) {
            // 判断keySet中的元素是否为传入的接口或者父类的实现或者子类
            if (interfaceOrClass.isAssignableFrom(clazz) && !clazz.equals(interfaceOrClass)) {
                classSet.add(clazz);
            }
        }
        return classSet.size() > 0 ? classSet : null;
    }

}
