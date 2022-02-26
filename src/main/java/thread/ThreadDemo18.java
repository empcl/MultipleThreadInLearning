package thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 多把锁（细粒度锁） 与 this锁
 * 场景：一间大屋子有两个功能：学习和睡觉，互不相干
 * 现在小南要学习，小女要睡觉，但如果只有一间屋子（一个对象锁）的话，那么并发会很低
 * 解决办法：准备多个房间（多个对象锁）
 *
 * 注意：使用多个对象锁的时候，要注意这多个对象锁是多个不想干的锁
 *
 * 将锁的粒度细分：
 * 好处：可以增加并发度
 * 坏处：如果一个线程需要同时获得多把锁，就容易发生死锁
 */

@Slf4j(topic = "c.ThreadDemo18")
public class ThreadDemo18 {
    public static void main(String[] args) {
        final BigRoom br = new BigRoom();
        new Thread(() -> {
            br.study();
        }, "small nan").start();

        new Thread(() -> {
            br.sleep();
        }, "small male").start();
    }
}

@Slf4j(topic = "c.BigRoom")
class BigRoom {
    private final Object studyRoom = new Object();
    private final Object sleepRoom = new Object();

    public void sleep() {
        synchronized (sleepRoom) {
            log.debug("sleeping 2 hours...");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void study() {
        synchronized (studyRoom) {
            log.debug("studying 1 hours...");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}