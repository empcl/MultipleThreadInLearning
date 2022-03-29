package thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 测试 waiting状态到blocked状态
 */
@Slf4j(topic = "c.ThreadDemo17")
public class ThreadDemo17 {
    final static Object obj = new Object();
    public static void main(String[] args) throws InterruptedException {
        final Thread t1 = new Thread(() -> {
            synchronized (obj) {
                log.debug("execute...");
                try {
                    obj.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("t1 code...");
            }
        }, "t1");

        final Thread t2 = new Thread(() -> {
            synchronized (obj) {
                log.debug("execute...");
                try {
                    obj.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("t2 code...");
            }
        }, "t2");

        t1.start();
        t2.start();

        TimeUnit.SECONDS.sleep(1);

        log.info("notify all waiting thread...");
        synchronized (obj) {
//            obj.notifyAll();
            obj.notify();
            System.out.println(">");
        }
    }
}
