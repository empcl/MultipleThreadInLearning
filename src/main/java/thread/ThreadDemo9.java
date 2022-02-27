package thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.ThreadDemo9")
public class ThreadDemo9 {
    public static void main(String[] args) throws InterruptedException {
        log.debug("开始运行...");
        final Thread daemon = new Thread(() -> {
            log.debug("开始运行...");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("运行结束...");
        }, "daemon");

        daemon.setDaemon(true);
        daemon.start();

        TimeUnit.SECONDS.sleep(1);
        log.debug("运行结束...");
    }
}
