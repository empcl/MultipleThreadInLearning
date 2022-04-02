package volatiletest;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.MonitorServiceTest")
public class MonitorServiceTest {
    public static void main(String[] args) {
        final MonitorService service = new MonitorService();

        for (int i = 0; i < 5; i++) {
            final int a = i;
            new Thread(() -> {
                service.start();
            }, "t_" + a).start();
        }
    }
}

@Slf4j(topic = "c.MonitorService")
class MonitorService {
    private volatile boolean isStarted = false;

    public void start() {
        log.info("starting...");
        if (isStarted) {
            log.info("started!!!");
            return;
        }

        isStarted = true;
    }
}