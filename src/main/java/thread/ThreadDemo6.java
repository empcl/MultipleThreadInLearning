package thread;

/**
 * 1、调用 yield 会让当前线程从Running状态进入Runnable就绪状态，然后调度执行其他线程
 * 2、具体的实现依赖于操作系统的任务调度器
 * 3、调用sleep 会让当前线程从Running状态 进入Timed Waiting状态（阻塞）
 * 4、线程优先级
 * 1）线程优先级会提示调度器优先调度该线程，但它仅仅是一个提示，调度器可以忽略它
 * 2）如果CPU比较忙，那么优先级高的线程会获得更多的时间片，但CPU闲时，优先级几乎没有作用
 */
public class ThreadDemo6 {
    public static void main(String[] args) {
        Runnable task1 = () -> {
            int count = 0;
            for (; ; ) {
                System.out.println("-------> 1 " + count++);
            }
        };

        Runnable task2 = () -> {
            int count = 0;
            for (; ; ) {
//                Thread.yield();
                System.out.println("          -------> 2 " + count++);
            }
        };

        final Thread t1 = new Thread(task1, "t1");
        final Thread t2 = new Thread(task2, "t2");
        t1.setPriority(Thread.MIN_PRIORITY);
        t2.setPriority(Thread.MAX_PRIORITY);

        t1.start();
        t2.start();
    }
}
