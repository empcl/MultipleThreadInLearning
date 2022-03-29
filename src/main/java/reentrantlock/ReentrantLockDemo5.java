package reentrantlock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j(topic = "c.ReentrantLockDemo5")
public class ReentrantLockDemo5 {
    public static void main(String[] args) throws InterruptedException {
        final ReentrantLock lock = new ReentrantLock(false);

        lock.lock();

        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                lock.lock();
                try {
                    System.out.println(Thread.currentThread().getName() + " running...");
                } finally {
                    lock.unlock();
                }
            }, "t_" + i).start();
        }

        TimeUnit.SECONDS.sleep(1);

        new Thread(() -> {
            log.info(Thread.currentThread().getName() + " starting...");
            lock.lock();

            try {
                log.info("{} running___", Thread.currentThread().getName());
            } finally {
                lock.unlock();
            }
        }, "enforce").start();

        lock.unlock();
    }
}
