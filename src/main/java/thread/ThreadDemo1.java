package thread;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.ThreadDemo1")
public class ThreadDemo1 {
    public static void main(String[] args) {
        Thread t = new Thread(() -> log.debug("Thread Running..."));
        t.setName("td 1");
        t.start();

        log.debug("Main Running...");
    }
}
