package thread;

import lombok.extern.slf4j.Slf4j;

import static java.lang.Thread.sleep;

@Slf4j(topic = "c.ThreadDemo15")
public class ThreadDemo11 {
    public static void main(String[] args) {
        Runnable r1 = () -> {
            try {
                log.debug("洗水壶～");
                sleep(1000);
                log.debug("烧开水～");
                sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        final Thread t1 = new Thread(r1, "T1");

        Runnable r2 = () -> {
            log.debug("洗茶壶～");
            try {
                sleep(1000);
                log.debug("洗茶杯～");
                sleep(2000);
                log.debug("拿茶叶～");
                sleep(1000);
                t1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            log.info("泡茶");
        };

        final Thread t2 = new Thread(r2, "T2");

        t1.start();
        t2.start();
    }
}
