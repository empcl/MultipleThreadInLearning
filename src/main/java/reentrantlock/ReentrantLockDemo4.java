package reentrantlock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j(topic = "c.ReentrantLockDemo4")
public class ReentrantLockDemo4 {
    public static void main(String[] args) {
        final Chopstick c1 = new Chopstick("c1");
        final Chopstick c2 = new Chopstick("c2");
        final Chopstick c3 = new Chopstick("c3");
        final Chopstick c4 = new Chopstick("c4");
        final Chopstick c5 = new Chopstick("c5");

        final Philosopher v12 = new Philosopher(c1, c2, "v12");
        final Philosopher v23 = new Philosopher(c2, c3, "v23");
        final Philosopher v34 = new Philosopher(c3, c4, "v34");
        final Philosopher v45 = new Philosopher(c4, c5, "v45");
        final Philosopher v51 = new Philosopher(c5, c1, "v51");

        v12.start();
        v23.start();
        v34.start();
        v45.start();
        v51.start();
    }
}

@Slf4j(topic = "c.Philosopher")
class Philosopher extends Thread {
    private Chopstick left;
    private Chopstick right;

    public Philosopher(Chopstick left, Chopstick right, String name) {
        super(name);
        this.left = left;
        this.right = right;
    }

    @Override
    public void run() {
        while (true) {
            // 尝试获取左手筷子
            if (left.tryLock()) {
              try {
                  if (right.tryLock()) {
                      try {
                          eat();
                      } finally {
                          right.unlock();
                      }
                  }
              } finally {
                  left.unlock();
              }
            }
        }
    }

    private void eat() {
        log.debug(Thread.currentThread().getName() + " eat ...");
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Chopstick extends ReentrantLock {
    String name;

    public Chopstick(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Chopstick{" +
                "name='" + name + '\'' +
                '}';
    }
}
