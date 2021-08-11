package com.atguigu.gulimall.search.thread;

import java.util.concurrent.*;

/**
 * @Auther :朱树广
 * @Date :2021/4/2
 * @Description :com.atguigu.gulimall.search.thread
 * @Version :1.0
 */
public class ThreadTest {
    public static ExecutorService executor = new ThreadPoolExecutor(5
            , 10
            , 10
            , TimeUnit.SECONDS
            , new LinkedBlockingQueue<>(100));

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("main...start");
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("当前线程：" + Thread.currentThread().getId());
            int i = 10 / 0;
            System.out.println("运行结果：" + i);
            return i;
        }, executor).whenComplete((res,exception)->{
            System.out.println("结果："+res+" 异常："+exception);
        }).exceptionally((throwable)->{
            return 10;
        });
        Integer i = future.get();
        System.out.println("main...end：" + i);
    }
}
