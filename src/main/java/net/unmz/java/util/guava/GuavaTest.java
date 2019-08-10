package net.unmz.java.util.guava;

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

public class GuavaTest {

    public static void main(String[] args) {


    }

    /***
     * 这种“如果有缓存则返回；否则运算、缓存、然后返回”的缓存模式是有很大弊端的。
     *
     * 当高并发条件下同时进行get操作，而此时缓存值已过期时，会导致大量线程都调用生成缓存值的方法，比如从数据库读取。
     * 这时候就容易造成数据库雪崩。这也就是我们常说的“缓存穿透”。
     *
     * 而Guava cache则对此种情况有一定控制。当大量线程用相同的key获取缓存值时，只会有一个线程进入load方法，
     * 而其他线程则等待，直到缓存值被生成。这样也就避免了缓存穿透的危险。
     */
    public static void test1() {
//        LoadingCache<String, Object> caches = CacheBuilder.newBuilder()
//                .maximumSize(100)  //缓存的容量大小
//                .expireAfterWrite(10, TimeUnit.MINUTES) //expireAfterWrite缓存的过期时间
//                .build(new CacheLoader<String, Object>() { //load()方法，当获取的缓存值不存在或已过期时，则会调用此load方法，进行缓存值的计
//                    @Override
//                    public Object load(String key) throws Exception {
//                        return generateValueByKey(key);
//                    }
//                });
//
//        try {
//            System.out.println(caches.get("key-zorro"));
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 如上的使用方法，虽然不会有缓存穿透的情况，但是每当某个缓存值过期时，老是会导致大量的请求线程被阻塞。
     *
     * 而Guava则提供了另一种缓存策略，缓存值定时刷新：更新线程调用load方法更新该缓存，其他请求线程返回该缓存的旧值。
     *
     * 这样对于某个key的缓存来说，只会有一个线程被阻塞，用来生成缓存值，而其他的线程都返回旧的缓存值，不会被阻塞。
     *
     * 这里就需要用到Guava cache的refreshAfterWrite()方法。
     *
     * ---------------------
     * 原文：https://blog.csdn.net/u012859681/article/details/75220605
     */
    public static void test2() {
//        LoadingCache<String, Object> caches = CacheBuilder.newBuilder()
//                .maximumSize(100)
//                .refreshAfterWrite(10, TimeUnit.MINUTES)//refreshAfterWrite
//                .build(new CacheLoader<String, Object>() {
//                    @Override
//                    public Object load(String key) throws Exception {
//                        return generateValueByKey(key);
//                    }
//                });
//
//        try {
//            System.out.println(caches.get("key-zorro"));
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 如2中的使用方法，解决了 ‘同一个key’ 的缓存过期时会让多个线程阻塞的问题，只会让用来执行刷新缓存操作的一个用户线程会被阻塞。
     *
     * 由此可以想到另一个问题，当‘缓存的key很多’时，
     * 高并发条件下大量线程同时获取不同key对应的缓存，此时依然会造成大量线程阻塞，并且给数据库带来很大压力。
     *
     * 这个问题的解决办法就是将刷新缓存值的任务交给后台线程，
     * ——所有的用户请求线程均返回旧的缓存值，这样就不会有用户线程被阻塞了。
     *
     */
    public static void test3() {
//        ListeningExecutorService backgroundRefreshPools = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(20));
//        LoadingCache<String, Object> caches = CacheBuilder.newBuilder()
//                .maximumSize(100)
//                .refreshAfterWrite(10, TimeUnit.MINUTES)
//                .build(new CacheLoader<String, Object>() {
//                    @Override
//                    public Object load(String key) throws Exception {
//                        return generateValueByKey(key);
//                    }
//
//                    @Override
//                    public ListenableFuture<Object> reload(String key, Object oldValue) throws Exception {
//                        return backgroundRefreshPools.submit(new Callable<Object>() {
//
//                            @Override
//                            public Object call() throws Exception {
//                                return generateValueByKey(key);
//                            }
//                        });
//                    }
//                });
//        try {
//            System.out.println(caches.get("key-zorro"));
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }

        /**
         * 在上面的代码中，我们新建了一个线程池，用来执行缓存刷新任务。并且重写了CacheLoader的reload方法，在该方法中建立缓存刷新的任务并提交到线程池。
         *
         * 注意此时缓存的刷新依然需要靠用户线程来驱动，只不过和2不同之处在于该用户线程触发刷新操作之后，会立马返回旧的缓存值。
         */
    }


}
