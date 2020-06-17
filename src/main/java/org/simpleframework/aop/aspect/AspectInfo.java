package org.simpleframework.aop.aspect;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.simpleframework.aop.aspectJ.PointcutLocator;

/**
 * 用来封装Aspect的执行顺序和实现类等信息
 */
@Getter
public class AspectInfo {
    // order优先级信息
    private int orderIndex;

    // Aspect实现类
    private DefalultAspect aspectObject;

    // 解析Aspect表达式并且定位被织入的目标
    private PointcutLocator pointcutLocator;

    public AspectInfo(int orderIndex, DefalultAspect aspectObject, PointcutLocator pointcutLocator) {
        this.orderIndex = orderIndex;
        this.aspectObject = aspectObject;
        this.pointcutLocator = pointcutLocator;
    }

    public AspectInfo(int orderIndex, DefalultAspect aspectObject) {
        this.orderIndex = orderIndex;
        this.aspectObject = aspectObject;
    }
}
