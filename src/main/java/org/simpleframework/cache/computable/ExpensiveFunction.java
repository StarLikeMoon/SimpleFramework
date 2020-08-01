package org.simpleframework.cache.computable;

import java.util.concurrent.TimeUnit;

/**
 * 模拟一个业务实现类，实现了Computable接口，模拟耗时计算
 */
public class ExpensiveFunction implements Computable<String, Integer> {

    @Override
    public Integer compute(String arg) throws Exception {
        TimeUnit.SECONDS.sleep(5);
        return Integer.valueOf(arg);
    }
}
