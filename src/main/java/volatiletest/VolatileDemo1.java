package volatiletest;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.VolatileDemo1")
public class VolatileDemo1 {
    public static void main(String[] args) throws InterruptedException {
        final TPTVolatile tptVolatile = new TPTVolatile();
        tptVolatile.start();

        TimeUnit.SECONDS.sleep(5);

        tptVolatile.stop();
    }
}

@Slf4j(topic = "c.TPTVolatile")
class TPTVolatile {
    private Thread thread;
    private volatile boolean stop = false;

    public void start() {
        thread = new Thread(() -> {
            while (true) {
                final Thread thread = Thread.currentThread();
                if (stop) {
                    log.info("处理后事～");
                    break;
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                    log.debug("保存结果～");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "monitorThread");

        thread.start();
    }

    public void stop() {
        stop = true;
        thread.interrupt();
    }
}
