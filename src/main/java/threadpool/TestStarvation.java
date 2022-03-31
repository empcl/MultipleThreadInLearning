package threadpool;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j(topic = "c.TestStarvation")
public class TestStarvation {
    private static final List<String> MENU = Arrays.asList("A", "B", "C", "D");

    private static final Random RANDOM = new Random();

    private static String cooking() {
        return MENU.get(RANDOM.nextInt(MENU.size()));
    }

    public static void main(String[] args) {
        final ExecutorService executorService = Executors.newFixedThreadPool(2);
        final ExecutorService executorService1 = Executors.newFixedThreadPool(2);

        executorService.execute(() -> {
           log.info("start");
            final Future<String> f = executorService1.submit(() -> {
                log.debug("do.");
                return cooking();
            });

            try {
                log.debug("go: {}", f.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });

        executorService.execute(() -> {
            log.info("start");
            final Future<String> f = executorService1.submit(() -> {
                log.debug("do.");
                return cooking();
            });

            try {
                log.debug("go: {}", f.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });

    }
}
