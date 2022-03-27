package reentrantlock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 锁超时
 */
@Slf4j(topic = "c.ReentrantLockDemo3")
public class ReentrantLockDemo3 {
    private static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        final Thread t1 = new Thread(() -> {
            log.debug("start...");
            // tryLock() 如果没有获得锁，立马返回false，否则true
            // tryLock(timeout, unit) 等待超时
            try {
                if (!lock.tryLock(3L, TimeUnit.SECONDS)) {
                    log.debug("failed to get lock immediately,return.");
                    return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                log.debug("get lock.");
            } finally {
                lock.unlock();
            }
        }, "t1");

        lock.lock();
        log.debug("main get lock");
        t1.start();

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
