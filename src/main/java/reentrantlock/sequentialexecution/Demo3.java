package reentrantlock.sequentialexecution;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.Demo3")
public class Demo3 {
    public static void main(String[] args) {
        final SyncWaitSignal syncWaitSignal = new SyncWaitSignal(1, 5);

        new Thread(() -> {
                syncWaitSignal.print(1, 2, "a");
        }).start();

        new Thread(() -> {
                syncWaitSignal.print(2, 3, "b");
        }).start();

        new Thread(() -> {
                syncWaitSignal.print(3, 1, "c");
        }).start();
    }
}

@Slf4j(topic = "c.SyncWaitSignal")
class SyncWaitSignal {
    private int flag;
    private int loopNumber;

    public SyncWaitSignal(int flag, int loopNumber) {
        this.flag = flag;
        this.loopNumber = loopNumber;
    }

    public void print(int flag, int nextFlag, String str){
        for (int i = 0; i < loopNumber; i++) {
            synchronized (this) {
                while (this.flag != flag) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.info("content : {}", str);
                this.flag = nextFlag;
                this.notifyAll();
            }
        }
    }
}
