package thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

@Slf4j(topic="c.ThreadDemo3")
public class ThreadDemo3 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final FutureTask<Integer> task = new FutureTask<>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                log.debug("running...");
                Thread.sleep(1000);
                return 1;
            }
        });

        new Thread(task, "t1").start();

        final Integer result = task.get();
        log.debug("result: {}" ,result);
    }
}
