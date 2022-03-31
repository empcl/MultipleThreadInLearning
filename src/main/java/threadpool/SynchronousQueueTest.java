package threadpool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.SynchronousQueueTest")
public class SynchronousQueueTest {
    public static void main(String[] args) throws InterruptedException {
        final SynchronousQueue<Integer> queue = new SynchronousQueue<>();

        new Thread(() -> {
            try {
                log.debug("start putting {}", 1);
                log.debug("putting... {}", 1);
                queue.put(1);
                log.debug("{} putted...", 1);

                log.debug("putting {}", 2);
                queue.put(2);
                log.debug("{} putted...", 2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "t1").start();

        TimeUnit.SECONDS.sleep(1);

        new Thread(() -> {
            try {
                log.debug("taking {}", 1);
                TimeUnit.SECONDS.sleep(1);
                log.debug("{} started taking", Thread.currentThread().getName());
                queue.take();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }, "t2").start();

        TimeUnit.SECONDS.sleep(1);

        new Thread(() -> {
            try {
                log.debug("taking {}", 2);
                TimeUnit.SECONDS.sleep(2);
                log.debug("{} started taking", Thread.currentThread().getName());
                queue.take();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }, "t3").start();
    }
}
