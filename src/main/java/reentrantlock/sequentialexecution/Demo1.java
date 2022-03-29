package reentrantlock.sequentialexecution;

import lombok.extern.slf4j.Slf4j;

/**
 * wait notify
 * 必须先 2 后 1 打印输出
 *
 * 缺点：
 * 1）首先，需要保证先 wait 再 notify，否则 wait 线程永远得不到唤醒。因此使用了『运行标记』来判断该不该 wait
 * 2）第二，如果有些干扰线程错误地 notify 了 wait 线程，条件不满足时还要重新等待，使用了 while 循环来解决此问题
 * 3）最后，唤醒对象上的 wait 线程需要使用 notifyAll，因为『同步对象』上的等待线程可能不止一个
 */
@Slf4j(topic = "c.Demo1")
public class Demo1 {
    final static Object obj = new Object();
    // t2运行标记，代表t2是否运行过
    static boolean t2Runned = false;

    public static void main(String[] args) {
        new Thread(() -> {
            synchronized (obj) {
                while (!t2Runned) {
                    try {
                        obj.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                log.info("1");
            }
        }, "t1").start();

        new Thread(() -> {
            synchronized (obj) {
                log.info("2");
                t2Runned = true;
                obj.notifyAll();
            }
        }, "t2").start();
    }
}
