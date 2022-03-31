package threadpool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j(topic = "c.ExceptionCaptureTest")
public class ExceptionCaptureTest {
    public static void main(String[] args) {
        final ExecutorService service = Executors.newFixedThreadPool(1);

        final Future<Boolean> submit = service.submit(() -> {
            log.info("start...");
            int i = 1 / 0;
            return true;
        });

        try {
            log.info("result : {}", submit.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void fun2(final ExecutorService service) {

    }

    public static void fun1(final ExecutorService service) {
        service.submit(() -> {
            log.info("start...");
            try {
                int i = 1 / 0;
            } catch (Exception e) {
                log.error("Exception {}", e.getMessage());
            }
            return true;
        });
    }
}
