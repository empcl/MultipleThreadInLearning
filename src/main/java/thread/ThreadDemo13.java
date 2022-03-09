package thread;

import lombok.extern.slf4j.Slf4j;
import thread.util.Downloader;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.ThreadDemo13")
public class ThreadDemo13 {
    public static void main(String[] args) throws InterruptedException {
        // 线程1 等待线程2的下载结果
        GuardedObject go = new GuardedObject();
        final Thread t1 = new Thread(() -> {
            log.info("等待结果");
            final Object result = go.get(100);
            log.info("结果是：{}", result);
        }, "t1");

        final Thread t2 = new Thread(() -> {
            log.info("执行下载");
            try {
                final List<String> download = Downloader.download();
                int result = download.size();
                go.complete(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "t2");

        t1.start();
//        TimeUnit.SECONDS.sleep(10);
        t2.start();
    }
}

class GuardedObject {
    private Object response;

    public Object get(long timeout) {
        synchronized (this) {
            // 开始时间
            long begin = System.currentTimeMillis();
            // 经历的时间
            long passedTime = 0;
            while (response == null) {
                if (passedTime >= timeout) {
                    break;
                }
                try {
                    this.wait(timeout - passedTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                passedTime = System.currentTimeMillis() - begin;
            }
            return response;
        }
    }

    public void complete(Object response) {
        synchronized (this) {
            this.response = response;
            this.notifyAll();
            ;
        }
    }
}