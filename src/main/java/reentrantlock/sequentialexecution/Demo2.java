package reentrantlock.sequentialexecution;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * park unpark
 * 必须先 2 后 1 打印输出
 *
 * park 和 unpark 方法比较灵活，他俩谁先调用，谁后调用无所谓。
 * 并且是以线程为单位进行『暂停』和『恢复』，
 * 不需要『同步对象』和『运行标记』
 */

@Slf4j(topic = "c.Demo2")
public class Demo2 {
    public static void main(String[] args) throws InterruptedException {
        final Thread t1 = new Thread(() -> {
            LockSupport.park();
            log.debug("1");
        }, "t1");

        t1.start();

        TimeUnit.SECONDS.sleep(10);

        new Thread(() -> {
            log.debug("2");
            LockSupport.unpark(t1);
        }).start();
    }
}
