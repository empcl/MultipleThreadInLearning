package thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.ThreadDemo10")
public class ThreadDemo10 {
    public static void main(String[] args) throws InterruptedException {
        final TwoPhaseTermination tpt = new TwoPhaseTermination();
        tpt.start();

        TimeUnit.SECONDS.sleep(5);
        tpt.stop();
    }
}

@Slf4j(topic = "c.TwoPhaseTermination")
class TwoPhaseTermination {
    private Thread monitor;

    public void start() {
        monitor = new Thread(() -> {
            while (true) {
                final Thread currentThread = Thread.currentThread();
                if (currentThread.isInterrupted()) {
                    log.debug("料理后事...");
                    break;
                }
                try {
                    TimeUnit.SECONDS.sleep(1); // 情况1（打断位：false）
                    log.debug("执行监控记录"); // 情况2 - 正常打断（打断位：true）
                } catch (InterruptedException e) {
                    e.printStackTrace();
//                    currentThread.interrupt();
                }
            }
        });

        monitor.start();
    }

    public void stop() {
        monitor.interrupt();
    }
}
