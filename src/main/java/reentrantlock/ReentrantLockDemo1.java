package reentrantlock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 可重入
 */
@Slf4j(topic = "c.ReentrantLockDemo1")
public class ReentrantLockDemo1 {
    static ReentrantLock lock = new ReentrantLock();
    public static void main(String[] args) {
        m1();
    }

    private static void m1() {
        lock.lock();
        try {
            log.debug("execute m1...");
            m2();
        } finally {
            lock.unlock();
        }
    }

    private static void m2() {
        lock.lock();
        try {
            log.debug("execute m2...");
        } finally {
            lock.unlock();
        }
    }
}
