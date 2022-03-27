package reentrantlock.sequentialexecution;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

@Slf4j(topic = "c.Demo4")
public class Demo4 {
    public static void main(String[] args) {
        final SyncPark syncPark = new SyncPark(5);
        Thread[] threads = new Thread[3];
        final Thread t1 = new Thread(() -> {
            syncPark.print("a");
        }, "t1");

        final Thread t2 = new Thread(() -> {
            syncPark.print("b");
        }, "t2");

        final Thread t3 = new Thread(() -> {
            syncPark.print("c");
        }, "t3");

        threads[0] = t1;
        threads[1] = t2;
        threads[2] = t3;

        syncPark.setThreads(threads);

        syncPark.start();
    }
}

@Slf4j(topic = "c.SyncPark")
class SyncPark {
    private int loopNumber;
    private Thread[] threads;

    public SyncPark(int loopNumber) {
        this.loopNumber = loopNumber;
    }

    public void setThreads(Thread[] threads) {
        this.threads = threads;
    }

    public void print(String str) {
        for (int i = 0; i < loopNumber; i++) {
            LockSupport.park();
            log.info("content: {}", str);
            LockSupport.unpark(nextThread());
        }
    }

    private Thread nextThread() {
        final Thread currentThread = Thread.currentThread();

        int index = 0;
        for (int i = 0; i < threads.length; i++) {
            if (currentThread == threads[i]) {
                index = i;
                break;
            }
        }

        if (index < threads.length - 1) {
            return threads[index + 1];
        } else {
            return threads[0];
        }
    }

    public void start() {
        for (Thread thread : threads) {
            thread.start();
        }

        LockSupport.unpark(threads[0]);
    }
}
