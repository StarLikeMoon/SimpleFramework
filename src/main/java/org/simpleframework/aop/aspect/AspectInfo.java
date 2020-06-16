package org.simpleframework.aop.aspect;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用来封装Aspect的执行顺序和实现类等信息
 */
@AllArgsConstructor // 创建带参的构造函数
@Getter
public class AspectInfo {
    // order优先级信息
    private int orderIndex;

    // Aspect实现类
    private DefalultAspect aspectObject;


}
