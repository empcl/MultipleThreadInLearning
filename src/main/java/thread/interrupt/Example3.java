package thread.interrupt;

public class Example3 extends Thread{
    public static void main(String[] args) throws InterruptedException {
        final Example3 thread = new Example3();
        System.out.println("Starting thread...");
        thread.start();
        Thread.sleep(3000);
        System.out.println("Asking thread to stop...");
        thread.interrupt();
        Thread.sleep(3000);
        System.out.println("Stopping application...");
        System.out.println("状态：" + Thread.currentThread().isInterrupted());
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            System.out.println("Thread is running...");
            final long start = System.currentTimeMillis();
            // 使用while循环模拟 sleep
            while ((System.currentTimeMillis() - start) < 1000) {

            }
        }
        System.out.println("Thread exiting under request...");
    }
}
