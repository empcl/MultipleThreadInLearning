package jmm;

import java.util.concurrent.TimeUnit;

public class Demo1 {
    static volatile boolean run = true;
    public static void main(String[] args) throws InterruptedException {

        final Thread t1 = new Thread(() -> {
            while (run) {
                System.out.println("z");
            }
        });

        t1.start();

        TimeUnit.SECONDS.sleep(1);

        run = false;
    }
}
