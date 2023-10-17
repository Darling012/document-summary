# spring 线程池
##### 问题




## spring 线程池
Spring提供了多种线程池：  
  
- `SimpleAsyncTaskExecutor`：不是真的线程池，这个类不重用线程，每次调用都会创建一个新的线程。  
- `SyncTaskExecutor`：这个类没有实现异步调用，只是一个同步操作。只适用于不需要多线程的地  
- `ConcurrentTaskExecutor`：Executor的适配类，不推荐使用。如果ThreadPoolTaskExecutor不满足要求时，才用考虑使用这个类  
- `ThreadPoolTaskScheduler`：可以使用cron表达式  
- `ThreadPoolTaskExecutor` ：最常使用，推荐。其实质是对java.util.concurrent.ThreadPoolExecutor的包装  
  
阿里规范不允许直接建线程，要自建线程池，项目中额外线程需求并不高，所以提供一个线程池满足异步需求。spring 提供的ThreadPoolTaskExecutor，可直接注入使用， 默认线程数量为cpu核心数。 


[看完这 10 张图再说你会用ThreadPoolExecutor](https://mp.weixin.qq.com/s/Ci_wnpJQ1Ee1Ua2Ci6OItg)