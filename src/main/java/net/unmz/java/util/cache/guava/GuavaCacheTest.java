/*******************************************************
 * Copyright (C) 2019 iQIYI.COM - All Rights Reserved
 *
 * This file is part of {cupid_3}.
 * Unauthorized copy of this file, via any medium is strictly prohibited.
 * Proprietary and Confidential.
 *
 * Date: 2019/7/24
 * Author(s): zhanglongyun<zhanglongyun@qiyi.com>
 *
 *******************************************************/
package net.unmz.java.util.cache.guava;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GuavaCacheTest {

    //https://blog.csdn.net/u012859681/article/details/75220605
    //https://cd826.iteye.com/blog/2036659
    //https://blog.csdn.net/u011726984/article/details/80223633

    /**
     * ========== 【定时过期】 ============
     *
     * 定义了缓存大小、过期时间及缓存值生成方法
     *
     * 缺点：
     *  如果用其他的缓存方式，如redis，我们知道上面这种“如果有缓存则返回；否则运算、缓存、然后返回”的缓存模式是有很大弊端的。
     *
     *  当高并发条件下同时进行get操作，而此时缓存值已过期时，会导致大量线程都调用生成缓存值的方法，比如从数据库读取。
     *   这时候就容易造成数据库雪崩。
     *
     *   ———— 这也就是我们常说的“缓存穿透”
     */
    private static void method1() {
        //当缓存数量即将到达容量上线时，则会进行缓存回收，回收最近没有使用或总体上很少使用的缓存项。
        //需要注意的是在接近这个容量上限时就会发生，所以在定义这个值的时候需要视情况适量地增大一点。
        final LoadingCache<String, Object> caches = CacheBuilder.newBuilder()
                .maximumSize(100)  //缓存的容量
                .expireAfterWrite(10, TimeUnit.SECONDS) //10s后自动失效
                .build(new CacheLoader<String, Object>() {

                    //当获取的缓存值不存在或已过期时，则会调用此load()方法，进行缓存值的计算
                    @Override
                    public Object load(String key) throws Exception {
                        Thread.sleep(500);
                        //return generateValueByKey(key);
                        return null;
                    }
                });
        try {
            System.out.println(caches.get("key-zorro"));
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * ========== 【定时刷新】 ============
     *
     * 缓存值定时刷新： 更新线程调用load方法更新该缓存，——————其他请求线程返回该缓存的旧值！！！
     * 这样对于某个key的缓存来说，只会有一个线程被阻塞，用来生成缓存值，而其他的线程都返回旧的缓存值，不会被阻塞。
     *
     * 此外需要注意一个点，这里的定时并不是真正意义上的定时。
     * Guava cache的刷新需要依靠用户请求线程，让该线程去进行load方法的调用，所以如果一直没有用户尝试获取该缓存值，则该缓存也并不会刷新。
     *
     */
    private static void method2() {
        LoadingCache<String, Object> caches = CacheBuilder.newBuilder()
                .maximumSize(100)   //缓存的容量
                .refreshAfterWrite(10, TimeUnit.SECONDS) //缓存值10s定时刷新：更新线程调用load方法更新该缓存，其他请求线程返回该缓存的旧值
                .build(new CacheLoader<String, Object>() {
                    @Override
                    public Object load(String key) throws Exception {
//                        return generateValueByKey(key);
                        return null;
                    }
                });
        try {
            System.out.println(caches.get("key-zorro"));
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    /**
     * ========== 【异步刷新】 ============
     *
     * 如2中的使用方法，解决了同一个key的缓存过期时会让多个线程阻塞的问题，只会让用来执行刷新缓存操作的一个用户线程会被阻塞。
     * 由此可以想到另一个问题，=== 当缓存的key很多时 ===，高并发条件下大量线程同时获取不同key对应的缓存，此时依然会造成大量线程阻塞，并且给数据库带来很大压力。
     *
     * 这个问题的解决办法就是将刷新缓存值的任务交给后台线程，所有的用户请求线程均返回旧的缓存值，===> 这样就不会有用户线程被阻塞了。
     *
     * 我们新建了一个线程池，用来执行缓存刷新任务。并且重写了CacheLoader的reload方法，在该方法中建立缓存刷新的任务并提交到线程池。
     *
     * ===注意此时缓存的刷新依然需要靠用户线程来驱动，只不过和2不同之处在于该用户线程触发刷新操作之后，会立马返回旧的缓存值
     *
     */
//    private static void method3() {
//        ListeningExecutorService backgroundRefreshPools = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(20));
//        LoadingCache<String, Object> caches = CacheBuilder.newBuilder()
//                .maximumSize(100)
//                .refreshAfterWrite(10, TimeUnit.MINUTES)
//                .build(new CacheLoader<String, Object>() {
//                    @Override
//                    public Object load(String key) throws Exception {
////                        return generateValueByKey(key);
//                        return null;
//                    }
//
//                    @Override
//                    public ListenableFuture<Object> reload(String key,
//                                                           Object oldValue) throws Exception {
//                        return backgroundRefreshPools.submit(new Callable<Object>() {
//                            @Override
//                            public Object call() throws Exception {
////                                return generateValueByKey(key);
//                                return null;
//                            }
//                        });
//                    }
//                });
//        try {
//            System.out.println(caches.get("key-zorro"));
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//    }

    public static void main(String[] args) {

    }

}
