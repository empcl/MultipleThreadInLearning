package thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 1、可以使用interrupt方法打断正在睡眠的线程，这时sleep方法会抛出InterruptedException
 * 2、睡眠结束后的线程未必会立刻得到执行，需要等待CPU时间片重新调度到该线程
 */
@Slf4j(topic = "c.ThreadDemo5")
public class ThreadDemo5 {
    public static void main(String[] args) throws InterruptedException {
        final Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                log.debug("enter sleep...");
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    log.info("wake up");
                    e.printStackTrace();
                }
            }
        };

        t1.start();

        log.debug("interrupt...");
        t1.interrupt();
    }
}
