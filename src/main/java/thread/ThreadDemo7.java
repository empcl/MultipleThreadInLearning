package thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.ThreadDemo7")
public class ThreadDemo7 {
    static int v = 0;
    static int v1 = 0;
    static int v2 = 0;

    public static void main(String[] args) throws InterruptedException {
        test3();
    }

    private static void test1() throws InterruptedException {
        log.debug("开始～");
        Runnable r = () -> {
            log.debug("开始");
            try {
                TimeUnit.MICROSECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("结束");
            v = 10;
        };
        final Thread t = new Thread(r);
        t.start();
        t.join();
        log.debug("结果：{}", v);
        log.debug("结束");
    }

    private static void test2() throws InterruptedException {
        final Thread t1 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            v1 = 10;
        }, "t1");

        final Thread t2 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            v2 = 20;
        }, "t2");

        final long start = System.currentTimeMillis();
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        log.debug("v1:{}, v2:{}, cost:{}", v1, v2, System.currentTimeMillis() - start);
    }

    private static void test3() throws InterruptedException {
        final Thread t = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            v1 = 10;
        }, "t");

        final long start = System.currentTimeMillis();
        t.start();
//        t.join(1500);
        t.join(3000);
        log.debug("v1: {}, cost: {}", v1, System.currentTimeMillis() - start);
    }
}
