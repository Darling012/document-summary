# spring 定时任务

springtask默认单线程串行执行，可通过配置SchedulingConfigurer实现并行任务（ThreadPoolTaskScheduler，可注入的线程池为XXXScheduler），即不同任务方法用不同线程，解决不能同时执行不同任务。加上@Async实现并行异步任务（另起线程执行）解决单个任务需要间隔三秒，但执行了五秒，上个任务影响下个任务问题。只加@Async也能实现并行。  
  
即ThreadPoolTaskScheduler解决了调用定时任务时，当存在多个任务，提供了不同线程。  
  
@Async是异步执行，无论各个任务，还是单个任务不同循环，都是另起线程，所以实现了并行。要注意极端情况线程池没线程可用。  
存在在异步线程中获取不到request情况，因为通过@async注解的方法，会被springboot丢到线程池中去执行，就等于开启了新的线程； 但是RequestContextHolder使用ThreadLocal保存request实例的，那么如果在新的线程中肯定会获取不到request的。建议在调用异步方法的时候，将request作为参数传递到异步方法中  
1. https://mp.weixin.qq.com/s/eu6cpP7rpjZvf4-nl3K7uw  
2. https://blog.51cto.com/u_15127686/2832738  
3. https://blog.csdn.net/jiao_zg/article/details/121030592  
4. https://blog.csdn.net/wzy_168/article/details/109182301 