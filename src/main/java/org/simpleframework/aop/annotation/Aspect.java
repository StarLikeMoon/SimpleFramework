package org.simpleframework.aop.annotation;

import java.lang.annotation.*;

/**
 * 切面类注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {

    /**
     * 需要被织入横切逻辑的注解标签
     * @return
     */
    Class<? extends Annotation> value();

}
