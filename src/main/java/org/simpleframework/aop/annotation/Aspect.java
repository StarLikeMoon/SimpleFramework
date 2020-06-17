package org.simpleframework.aop.annotation;

import java.lang.annotation.*;

/**
 * 切面类注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {

    // AspectJ支持的横切表达式
    String pointcut();

    // 不使用AspectJ时支持的joinpoint
    Class<? extends Annotation> value() default Aspect.class;

}
