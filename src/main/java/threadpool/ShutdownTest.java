package threadpool;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.ShutdownTest")
public class ShutdownTest {
    public static void main(String[] args) throws Exception {
        final ExecutorService executorService = Executors.newFixedThreadPool(2);

        Callable<String> callable1 = () -> {
            TimeUnit.SECONDS.sleep(1);
            log.info("{} print", Thread.currentThread().getName());
            return "1";
        };

        Callable<String> callable2 = () -> {
            TimeUnit.SECONDS.sleep(5);
            log.info("{} print", Thread.currentThread().getName());
            return "2";
        };

        executorService.invokeAll(Arrays.asList(callable1, callable2));


        /**
         * 线程池状态变为 SHUTDOWN
         *  不会接收新任务
         *  但已提交任务会执行完
         *  此方法不会阻塞调用线程的执行
         */
        executorService.shutdown();

        Callable<String> callable3 = () -> {
            log.info("{} print", Thread.currentThread().getName());
            return "3";
        };

        executorService.submit(callable3);
    }
}
