package org.simpleframework.util;

import java.util.Collection;
import java.util.Map;

/**
 * 用来判断的工具类
 */
public class ValidationUtil {

    /**
     * 用来判断集合是否为空
     *
     * @param obj
     * @return
     */
    public static boolean isEmpty(Collection<?> obj) {
        return obj == null || obj.isEmpty();
    }

    /**
     * 用来判断String是否为空或者""
     *
     * @param obj
     * @return
     */
    public static boolean isEmpty(String obj) {
        return obj == null || "".equals(obj);
    }

    /**
     * 用来判断数组是否为空
     *
     * @param obj
     * @return
     */
    public static boolean isEmpty(Object[] obj) {
        return obj == null || obj.length == 0;
    }

    /**
     * 用来判断Map是否为空
     */
    public static boolean isEmpty(Map<?, ?> obj) {
        return obj == null || obj.isEmpty();
    }

}
