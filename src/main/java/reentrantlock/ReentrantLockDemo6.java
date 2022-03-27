package reentrantlock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j(topic = "c.ReentrantLockDemo6")
public class ReentrantLockDemo6 {
    final static ReentrantLock lock = new ReentrantLock();
    final static Condition waitCigaretteCondition = lock.newCondition();
    final static Condition waitBreakfastCondition = lock.newCondition();

    static volatile boolean hasCigarette = false;
    static volatile boolean hasBreakfast = false;

    public static void main(String[] args) {
        new Thread(() -> {
            lock.lock();
            try {
                while (!hasCigarette) {
                    try {
                        waitCigaretteCondition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } finally {
                lock.unlock();
            }
            log.info("eat cigarette...");
        }, "n1").start();

        new Thread(() -> {
            lock.lock();
            try {
                while (!hasBreakfast) {
                    try {
                        waitBreakfastCondition.await();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } finally {
                lock.unlock();
            }
            log.info("eat breakfast___");
        }, "n2").start();


        try {
            TimeUnit.SECONDS.sleep(1);
            sendBreakfast();
            TimeUnit.SECONDS.sleep(1);
            sendCigarette();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void sendCigarette() {
        lock.lock();
        try {
            log.info("send cigarette...");
            hasCigarette = true;
            waitCigaretteCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    private static void sendBreakfast() {
        lock.lock();
        try {
            log.info("send break fast___");
            hasBreakfast = true;
            waitBreakfastCondition.signal();
        } finally {
            lock.unlock();
        }
    }
}
