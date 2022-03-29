package reentrantlock.sequentialexecution;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j(topic = "c.Demo5")
public class Demo5 {
    public static void main(String[] args) {
        final AwaitSignalCondition asc = new AwaitSignalCondition(5);
        final Condition c1 = asc.newCondition();
        final Condition c2 = asc.newCondition();
        final Condition c3 = asc.newCondition();

        new Thread(() -> {
            asc.print("a", c1, c2);
        }).start();

        new Thread(() -> {
            asc.print("b", c2, c3);
        }).start();

        new Thread(() -> {
            asc.print("c", c3, c1);
        }).start();


        asc.start(c1);
    }
}

@Slf4j(topic = "c.AwaitSignalCondition")
class AwaitSignalCondition extends ReentrantLock {
    private int loopNumber;

    public AwaitSignalCondition(int loopNumber) {
        this.loopNumber = loopNumber;
    }

    public void start(Condition first) {
        try {
            this.lock();
            log.info("start...");
            first.signal();
        } finally {
            this.unlock();
        }
    }

    public void print(String str, Condition current, Condition next) {
        for (int i = 0; i < loopNumber; i++) {
            this.lock();
            try {
                try {
                    current.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info(str);
                next.signal();
            } finally {
                this.unlock();
            }
        }
    }
}
