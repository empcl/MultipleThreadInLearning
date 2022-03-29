package thread;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;

/**
 * 转账练习
 */

@Slf4j(topic = "c.ExerciseTransfer")
public class ExerciseTransfer {
    public static void main(String[] args) throws InterruptedException {
        final Account a = new Account(1000);
        final Account b = new Account(2000);

        final Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                a.transfer(b, new Random().nextInt(100) + 1);
            }
        }, "t1");

        final Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                a.transfer(b, new Random().nextInt(100) + 1);
            }
        }, "t2");

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        log.debug("total : {}", a.getMoney() + b.getMoney());
    }
}

class Account {
    private int money;

    public Account(int money) {
        this.money = money;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public synchronized void transfer(Account target, int amount) {
        if (this.getMoney() >= amount) {
            this.setMoney(this.getMoney() - amount);
            target.setMoney(target.getMoney() + amount);
        }
    }
}
