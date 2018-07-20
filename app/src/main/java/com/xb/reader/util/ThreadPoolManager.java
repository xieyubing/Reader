package com.xb.reader.util;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by asus on 2017/8/9.
 */

public class ThreadPoolManager {

    private static ThreadPoolManager mInstance = new ThreadPoolManager();
    public static ThreadPoolManager getInstance() {
        return mInstance;
    }

    private int corePoolSize;
    private int maximumPoolSize;
    private long keepAliveTime = 1;
    private TimeUnit unit = TimeUnit.HOURS;
    private ThreadPoolExecutor executor;

    private ThreadPoolManager(){
        corePoolSize = Runtime.getRuntime().availableProcessors()*2+1;
        maximumPoolSize = 10;
        executor = new ThreadPoolExecutor(corePoolSize,maximumPoolSize,keepAliveTime,unit,
                new LinkedBlockingQueue<Runnable>(),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
    }

    /**
     * 执行任务
     */
    public void execute(Runnable runnable){
        if(runnable==null)return;

        executor.execute(runnable);
    }
    /**
     * 从线程池中移除任务
     */
    public void remove(Runnable runnable){
        if(runnable==null)return;

        executor.remove(runnable);
    }

}
