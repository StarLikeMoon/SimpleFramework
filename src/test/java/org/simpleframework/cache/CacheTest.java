package org.simpleframework.cache;

import org.simpleframework.cache.computable.ExpensiveFunction;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 缓存压测
 */
public class CacheTest {

    public static FrameworkCache<String, Integer> cache = new FrameworkCache<>(new ExpensiveFunction());
    // 利用countDownLatch让线程统一触发请求来进行压测
    public static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(10000);

        long start = System.currentTimeMillis();

        for (int i = 0; i < 10000; i++) {
            service.submit(new Runnable() {
                @Override
                public void run() {
                    Integer res = null;
                    try {
                        System.out.println(Thread.currentThread().getName() + "开始等待");
                        countDownLatch.await();
                        System.out.println(Thread.currentThread().getName() + "被放行");
                        res = cache.compute("666");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(res);
                }
            });
        }

        // 统一放行，开始压测
        TimeUnit.SECONDS.sleep(5);
        countDownLatch.countDown();
        service.shutdown();
        while (!service.isTerminated()){}

        System.out.println("压测完毕，总耗时： " + (System.currentTimeMillis() - start) + "ms");
    }

}
