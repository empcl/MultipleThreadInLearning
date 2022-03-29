package thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 死锁 test1()
 * 活锁 test2()
 *      活锁出现在两个线程互相改变对方的结束条件，最后谁也无法结束
 * 饥饿 test3()
 *      一个线程由于优先级太低，始终得不到CPU调度执行，也不能够结束
 */
@Slf4j(topic = "c.ThreadDemo19")
public class ThreadDemo19 {
    static volatile int count = 10;

    public static void main(String[] args) {
        test2();
    }

    public static void test1() {
        final Object obj1 = new Object();
        final Object obj2 = new Object();

        new Thread(() -> {
            log.info("start t1...");
            synchronized (obj1) {
                log.debug("lock obj1");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (obj2) {
                    log.debug("lock obj2");
                }
            }
        }, "t1").start();

        new Thread(() -> {
            log.info("start t2...");
            synchronized (obj2) {
                log.info("lock obj2");
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (obj1) {
                    log.debug("lock obj1");
                }
            }
        }, "t2").start();
    }

    public static void test2() {
        new Thread(() -> {
            // 期望减到0退出循环
              while (count > 0) {
                  try {
                      TimeUnit.SECONDS.sleep(2);
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  }
                  count--;
                  log.debug("count : {}", count);
              }
        }
        , "t1").start();

        new Thread(() -> {
            // 期望超过20退出循环
            while (count < 20) {
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count++;
                log.debug("-------- count : {}", count);
            }
        }, "t2").start();
    }
}
