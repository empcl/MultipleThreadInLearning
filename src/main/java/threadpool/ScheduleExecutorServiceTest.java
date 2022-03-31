package threadpool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.ScheduleExecutorServiceTest")
public class ScheduleExecutorServiceTest {
    public static void main(String[] args) {
        final ScheduleExecutorServiceTest serviceTest = new ScheduleExecutorServiceTest();
        final ScheduledExecutorService service = Executors.newScheduledThreadPool(2);

        log.debug("starting...");

        serviceTest.fun3(service);
    }

    private void fun3(ScheduledExecutorService service) {
        // scheduleWithFixedDelay 的间隔是 **上一个任务结束 <-> 延时 <-> 下一个任务开始 **
        service.scheduleWithFixedDelay(() -> {
            log.debug("task -- ");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    private void fun2(ScheduledExecutorService service) {
        // 调度的实际间隔 = max (任务的执行时间, period)
        service.scheduleAtFixedRate(() -> {
            log.info("task --");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    private void fun1(ScheduledExecutorService service) {
        // 仅调度一次
        // 添加两个任务，希望它们都在 1s 后执行
        // corePoolSize > tasks, 任务并行调度
        // corePoolSize < tasks, 部分任务需要等待之前的任务调度完，继续调度
        service.schedule(() -> {
            log.info("task 1");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 1, TimeUnit.SECONDS);

        service.schedule(() -> {
            log.info("task 2");
        }, 1, TimeUnit.SECONDS);
    }
}
