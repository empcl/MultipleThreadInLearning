package thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.ThreadDemo12")
public class ThreadDemo12 {
    public static void main(String[] args) throws InterruptedException {
        final Thread t1 = new Thread(() -> {
            log.debug("running...");
        }, "t1");

        final Thread t2 = new Thread(() -> {
            while (true) {
            }
        }, "t2");
        t2.start();

        final Thread t3 = new Thread(() -> {
            log.debug("running...");
        }, "t3");
        t3.start();

        final Thread t4 = new Thread(() -> {
            synchronized (ThreadDemo12.class) {
                try {
                    TimeUnit.SECONDS.sleep(10000000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t4");

        t4.start();

        final Thread t5 = new Thread(() -> {
            try {
                t2.join(); // waiting
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t5");

        t5.start();

        final Thread t6 = new Thread(() -> {
            synchronized (ThreadDemo12.class) {
                try {
                    log.info("......");
                    TimeUnit.SECONDS.sleep(100000000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t6.start();

        TimeUnit.SECONDS.sleep(1);

        log.debug("t1 state: {}", t1.getState());
        log.debug("t2 state: {}", t2.getState());
        log.debug("t3 state: {}", t3.getState());
        log.debug("t4 state: {}", t4.getState());
        log.debug("t5 state: {}", t5.getState());
        log.debug("t6 state: {}", t6.getState());
    }
}
