package org.simpleframework.cache;

import org.simpleframework.cache.computable.Computable;

import java.util.Map;
import java.util.concurrent.*;

/**
 * 用装饰器模式给计算器自动添加缓存功能
 *
 * 利用ConcurrentHashMap来保证线程安全
 *
 * 利用future，避免重复计算
 *
 * 防止计算出错，多次尝试
 *
 * 加入过期功能，到期自动失效，并实现随机性，防止高并发下缓存雪崩
 */
public class FrameworkCache<A, V> implements Computable<A, V> {

    private final Map<A, Future<V>> cache = new ConcurrentHashMap<>();

    // 计算类，用装饰器来装饰
    private final  Computable<A, V> c;

    public FrameworkCache(Computable<A, V> c) {
        this.c = c;
    }

    @Override
    public V compute(A arg) throws InterruptedException {
        // 有些计算类可能计算出错，此时多次计算尝试
        while (true) {
            Future<V>  f = cache.get(arg);
            if (f == null) { // 如果没有缓存
                // 避免重复计算，使用任务
                Callable<V> callable = new Callable<V>() {
                    @Override
                    public V call() throws Exception {
                        return c.compute(arg);
                    }
                };
                FutureTask<V> ft = new FutureTask<>(callable);
                f = cache.putIfAbsent(arg, ft);
                // 如果返回空说明，之前缓存中没有
                if (f == null) {
                    f = ft;
                    ft.run();
                }
            }
            try {
                return f.get();
            } catch (CancellationException e) {
                System.out.println("被取消了");
                // 防止污染缓存
                cache.remove(arg);
                throw e;
            }catch (InterruptedException e) {
                // 防止污染缓存
                cache.remove(arg);
                throw e;
            } catch (ExecutionException e) {
                System.out.println("计算错误，需要重试");
                // 防止污染缓存
                cache.remove(arg);
            }
        }
    }

    // 延迟执行的线程池，用来执行缓存失效功能
    public final static ScheduledExecutorService excutor = Executors.newScheduledThreadPool(5);

    /**
     * 支持缓存过期的方法，传入超时时间
     */
    public  V compute(A arg, long expire) throws InterruptedException {
        if (expire > 0) {
            excutor.schedule(new Runnable() {
                @Override
                public void run() {
                    expire(arg);
                }
            }, expire, TimeUnit.SECONDS);
        }
        return compute(arg);
    }

    // 随机失效时间的缓存， 1- 10s随机缓存
    public V computeRandomExpire(A key) throws InterruptedException {
        long randomExpire = (long) Math.random() * 10;
        return compute(key, randomExpire);
    }

    // 清除过期缓存
    private synchronized void expire(A key) {
        Future<V> future = cache.get(key);
        if (future != null) {
            // 如果任务还没计算完，直接取消
            if (!future.isDone()) {
                System.out.println("取消Future任务");
                future.cancel(true);
            }
            System.out.println("缓存过期时间到，清除缓存");
            cache.remove(key);
        }

    }
}
