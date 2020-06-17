package org.simpleframework.aop.aspectJ;

import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.ShadowMatch;

import java.lang.reflect.Method;

/**
 * 解析Aspect表达式并且定位被织入的目标
 */
public class PointcutLocator {

    // Pointcut解析器，需要装配上相关语法树才能识别Aspect注解中的pointcut表达式
    // 直接给它赋值上Aspectj的所有表达式，以便支持对众多表达式的解析
    private PointcutParser pointcutParser = PointcutParser.getPointcutParserSupportingSpecifiedPrimitivesAndUsingContextClassloaderForResolution(
            PointcutParser.getAllSupportedPointcutPrimitives()
    );

    // 表达式解析器
    private PointcutExpression pointcutExpression;

    public PointcutLocator(String expression) {
        this.pointcutExpression = pointcutParser.parsePointcutExpression(expression);
    }

    /**
     * 根据传入的Class对象，判断其是否为Aspect横切类的目标代理类，即匹不匹配pointcut表达式（初筛）
     *
     * @param targetClass
     * @return
     */
    public boolean roughMatches(Class<?> targetClass) {
        // couldMatchJoinPointsInType只能校验within
        // 不能校验（execution, call, get, set等），面对无法校验的表达式，会直接返回true
        return pointcutExpression.couldMatchJoinPointsInType(targetClass);
    }

    /**
     * 判断传入的Method对象是否是Aspect的目标代理方法，即是否匹配pointcut表达式（精筛）
     *
     * @param method
     * @return
     */
    public boolean accurateMatches(Method method) {
        ShadowMatch shadowMatch = pointcutExpression.matchesMethodExecution(method);
        if (shadowMatch.alwaysMatches()) {
            // 完全匹配的情况
            return true;
        } else {
            return false;
        }
    }
}
