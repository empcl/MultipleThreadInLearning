package threadpool;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

@Slf4j(topic = "c.SubmitTaskTest")
public class SubmitTaskTest {
    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
        final ExecutorService executorService = Executors.newFixedThreadPool(1);
        // 执行任务
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                log.debug("{} running...", Thread.currentThread().getName());
            }
        });

        // 提交任务task，用返回值Future获得任务执行结果
        final Future<String> result = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "plan";
            }
        });

        log.debug("result : {} ", result.get());

        Collection<Callable<String>> tasks = Arrays.asList(
                () -> {
                    TimeUnit.SECONDS.sleep(1);
                    return "1";
                },
                () -> {
                    TimeUnit.SECONDS.sleep(1);
                    return "2";
                }
        );

        // 提交 tasks 中所有任务，哪个任务先成功执行完毕，返回此任务执行结果，其它任务取消
        final String result1 = executorService.invokeAny(tasks);

        // 提交 tasks 中所有任务，哪个任务先成功执行完毕，返回此任务执行结果，其它任务取消，带超时时间
        final String result2 = executorService.invokeAny(tasks, 5000, TimeUnit.MILLISECONDS);
        System.out.println(result1);
        System.out.println(result2);

        // 提交tasks中所有任务
        final List<Future<String>> futures1 = executorService.invokeAll(tasks);
        final List<Future<String>> futures2 = executorService.invokeAll(tasks, 500, TimeUnit.MILLISECONDS);
        for (Future<String> future : futures1) {
            System.out.println(future.get());
        }
        System.out.println("-------------------");
        for (Future<String> future : futures2) {
            System.out.println(future.get());
        }
    }
}
