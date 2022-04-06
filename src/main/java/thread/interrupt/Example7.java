package thread.interrupt;

import java.util.concurrent.TimeUnit;

public class Example7 {
    static int x;
    public static void main(String[] args) {
        Thread t2 = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println(x);
                    break;
                }
            }
        }, "t2");
        t2.start();

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            x = 10;
            t2.interrupt();
        }, "t1").start();

        while (!t2.isInterrupted()) {
            Thread.yield();
        }

        System.out.println(x);
    }
}
