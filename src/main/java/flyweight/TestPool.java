package flyweight;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class TestPool {
    public static void main(String[] args) {
        final Pool pool = new Pool(2);
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                final MockConnection connection = pool.peek();

                try {
                    TimeUnit.SECONDS.sleep(new Random().nextInt(5));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                pool.free(connection);
            }).start();
        }
    }
}

@Slf4j(topic = "c.Pool")
class Pool {
    private final int size;
    private final MockConnection[] connections;
    // 连接状态数组 0 空闲 1 繁忙
    private AtomicIntegerArray states;

    public Pool(int size) {
        this.size = size;
        this.connections = new MockConnection[size];
        this.states = new AtomicIntegerArray(size);
        for (int i = 0; i < size; i++) {
            connections[i] = new MockConnection();
        }
    }

    public MockConnection peek() {
        while(true){
            for (int i = 0; i < size; i++) {
                if (states.get(i) == 0) {
                    if (states.compareAndSet(i, 0, 1)) {
                        log.debug("peek {}", connections[i]);
                        return connections[i];
                    }
                }
            }

            synchronized(this) {
                log.debug("wait...");
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void free(MockConnection connection) {
        for (int i = 0; i < size; i++) {
            if (connection == connections[i]) {
                states.set(i, 0);
                synchronized(this) {
                    log.debug("free {}", connection);
                    this.notifyAll();
                }
                break;
            }
        }
    }

}

class MockConnection {}