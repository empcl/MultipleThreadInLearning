package threadpool;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.ShutdownTest")
public class ShutdownNowTest {
    public static void main(String[] args) throws Exception {
        final ExecutorService executorService = Executors.newFixedThreadPool(2);

        Callable<String> callable1 = () -> {
            log.info("{} print", Thread.currentThread().getName());
            TimeUnit.SECONDS.sleep(1);
            return "1";
        };

        Callable<String> callable2 = () -> {
            log.info("{} print", Thread.currentThread().getName());
            TimeUnit.SECONDS.sleep(5);
            return "2";
        };

        Callable<String> callable3 = () -> {
            TimeUnit.SECONDS.sleep(10);
            log.info("{} print", Thread.currentThread().getName());
            return "3";
        };

        // 阻塞
        executorService.invokeAll(Arrays.asList(callable1, callable2));

        /**
         * 线程池状态变为 STOP
         *  不会接受新任务
         *  会将队列中的任务返回
         *  并用interrupt的方式中断正在执行的任务
         */
        final List<Runnable> runnables = executorService.shutdownNow();

        for (Runnable runnable : runnables) {
            System.out.println(" -> " + runnable);
        }

        Callable<String> callable4 = () -> {
            log.info("{} print", Thread.currentThread().getName());
            return "4";
        };

        executorService.submit(callable4);
    }
}
