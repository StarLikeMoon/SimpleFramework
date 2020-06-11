package org.simpleframework.inject.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用来注入依赖的注解，目前仅支持通过成员变量注入
 * 在Spring中，如果一个接口有多个实现类，可以通过结合@Qualifier指定实例名称来装配
 * 在这里为了实现简单，主要是学习设计思路，直接在Autowired中定义一个value属性来代替Qualifier
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Autowired {
    String value() default "";
}
