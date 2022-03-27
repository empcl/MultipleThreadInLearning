package reentrantlock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 可打断
 */
@Slf4j(topic = "c.ReentrantLockDemo2")
public class ReentrantLockDemo2 {
    static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        final Thread t1 = new Thread(() -> {
            log.debug("start...");
            try {
                // 这里使用lockInterruptibly 表示当前等待锁的过程中可以被打断，如果是lock方法的话，调用interrupt()方法则没有效果哦
                lock.lockInterruptibly();
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.debug("be interrupted when waiting...");
                return;
            }

            try {
                log.debug("t1 get lock...");
            } finally {
                lock.unlock();
            }
        }, "t1");

        lock.lock();
        log.debug("main get lock...");
        t1.start();
        try {
            TimeUnit.SECONDS.sleep(1);
            t1.interrupt();
            log.debug("execute interrupt...");
        } finally {
            lock.unlock();
        }
    }
}
