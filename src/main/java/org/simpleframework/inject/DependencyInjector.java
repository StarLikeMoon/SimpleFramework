package org.simpleframework.inject;

import lombok.extern.slf4j.Slf4j;
import org.simpleframework.core.BeanContainer;
import org.simpleframework.inject.annotation.Autowired;
import org.simpleframework.util.ClassUtil;
import org.simpleframework.util.ValidationUtil;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * 用来依赖注入的类
 */
@Slf4j
public class DependencyInjector {
    // bean容器
    private BeanContainer beanContainer;

    /**
     * DependencyInjector生成的时候直接初始化beanContainer
     */
    public DependencyInjector() {
        beanContainer = BeanContainer.getInstance();
    }

    /**
     * 执行IOC，依赖注入
     */
    public void doIoc() {
        // 判空
        if (ValidationUtil.isEmpty(beanContainer.getClasses())) {
            log.warn("empty classes in BeanContainer");
            return;
        }
        // 1.遍历Bean容器中所有的Class对象
        for (Class<?> clazz : beanContainer.getClasses()) {
            // 2.遍历Class对象的所有成员变量，利用反射
            Field[] fields = clazz.getDeclaredFields();
            if (ValidationUtil.isEmpty(fields)) {
                continue;
            }
            for (Field field : fields) {
                // 3.找出被Autowired标记的成员变量
                if (field.isAnnotationPresent(Autowired.class)) {
                    // 获取Autowired实例
                    Autowired autowired = field.getAnnotation(Autowired.class);
                    String autowiredValue = autowired.value();
                    // 4.获取这些成员变量的类型
                    Class<?> fieldClass = field.getType();
                    // 5.获取这些成员变量的类型或者指定的名称（如果有的话）在容器里对应的实例
                    Object fieldValue = getFieldInstance(fieldClass, autowiredValue);
                    if (fieldValue == null) {
                        throw new RuntimeException("unable to inject relevant type, target fieldClass is: " + fieldClass.getName() + autowiredValue);
                    } else {
                        // 6.通过反射将对应的成员变量实例注入到成员变量所在类的实例中
                        Object targetBean = beanContainer.getBean(clazz);
                        ClassUtil.setField(field, targetBean, fieldValue, true);
                    }
                }
            }
        }

    }

    /**
     * 根据成员变量的类型在容器里找到对应的实例
     *
     * @param fieldClass
     * @return
     */
    private Object getFieldInstance(Class<?> fieldClass, String autowiredValue) {
        Object fieldValue = beanContainer.getBean(fieldClass);
        if (fieldValue != null) {
            return fieldValue;
        } else {
            // 如果为空则可能成员变量是个接口，这里查找其实现类
            Class<?> implementedClass = getImplementClass(fieldClass, autowiredValue);
            if (implementedClass != null) {
                // 不为空说明该接口的实现类在容器中存在实例
                return beanContainer.getBean(implementedClass);
            } else {
                return null;
            }
        }

    }

    /**
     * 获取接口的实现类
     *
     * @param fieldClass
     * @return
     */
    private Class<?> getImplementClass(Class<?> fieldClass, String autowiredValue) {
        Set<Class<?>> classSet = beanContainer.getClassesBySuper(fieldClass);
        if (!ValidationUtil.isEmpty(classSet)) {
            if (ValidationUtil.isEmpty(autowiredValue)) {
                // 用户没有传入要注入的具体实现类名称，此时需要处理两种情况
                if (classSet.size() == 1) {
                    // 只有唯一实现类
                    return classSet.iterator().next();
                } else {
                    // 如果有多个实现类，且用户没有指定，就抛异常
                    throw new RuntimeException("multiple implemented classes for "
                            + fieldClass.getName() + ", please set @Autowired's value to pick one");
                }
            } else {
                // 用户传了autowiredValue，遍历集合中看是否有指定名字的那个实现类
                for (Class<?> clazz : classSet) {
                    if (autowiredValue.equals(clazz.getSimpleName())) {
                        return clazz;
                    }
                }
            }
        }
        return null;
    }
}
