package thread;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic="c.ThreadDemo2")
public class ThreadDemo2 {
    public static void main(String[] args) {
        Runnable r = () -> log.debug("Thread Running...");

        final Thread thread = new Thread(r, "td 2");
        thread.start();
    }
}
