package thread;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.ThreadDemo4")
public class ThreadDemo4 {
    public static void main(String[] args) throws InterruptedException {
        final Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        log.info("t1 state:{}", t1.getState());
        t1.start();
        log.info("t1 state:{}", t1.getState());
        Thread.sleep(500L);
        log.info("t1 state:{}", t1.getState());
    }
}
