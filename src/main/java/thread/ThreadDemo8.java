package thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

@Slf4j(topic = "c.ThreadDemo8")
public class ThreadDemo8 {
    public static void main(String[] args) throws InterruptedException {
        test3();
    }

    private static void test1() throws InterruptedException {
        final Thread t1 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t1");

        t1.start();

        TimeUnit.SECONDS.sleep(3);
        t1.interrupt();
        // 这里需要sleep一下，才能获得t1的中断标志位
        TimeUnit.MICROSECONDS.sleep(1);
        log.debug("打断状态: {}", t1.isInterrupted());
    }

    private static void test2() throws InterruptedException {
        final Thread t2 = new Thread(() -> {
            while (true) {
                final Thread current = Thread.currentThread();
                final boolean interrupted = current.isInterrupted();
                if (interrupted) {
                    log.debug("打断状态: {}", interrupted);
                    break;
                }
            }
        }, "t2");

        t2.start();

        TimeUnit.SECONDS.sleep(1);
        t2.interrupt();
    }

    // TODO 为什么可以继续向下执行，Thread.interrupted(); 清除状态后，第二次LockSupport.park不是阻塞了么？
    private static void test3() throws InterruptedException {
        final Thread t3 = new Thread(() -> {
            log.debug("park...");
            LockSupport.park();
            log.debug("unPark...");
            log.debug("打断状态：{}", Thread.currentThread().isInterrupted());

            Thread.interrupted();
            log.debug("清除打断状态:{}", Thread.currentThread().isInterrupted());
            LockSupport.park();
            log.debug("unPark~~~");
        }, "t3");

        t3.start();
        TimeUnit.SECONDS.sleep(3);
        t3.interrupt();
    }
}
