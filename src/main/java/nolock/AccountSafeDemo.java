package nolock;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class AccountSafeDemo {
    public static void main(String[] args) {
        final long start = System.currentTimeMillis();
        AccountSafe as = new AccountSafe(10000);
        ArrayList<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            final Thread t1 = new Thread(() -> {
                as.withdraw(10);
            });
            threads.add(t1);
        }

        threads.stream().forEach(Thread::start);
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(as.getBalance() + ", cost time : " + (System.currentTimeMillis() - start));
    }
}

class AccountSafe {
    private AtomicInteger balance;

    public AccountSafe(Integer balance) {
        this.balance = new AtomicInteger(balance);
    }

    public AtomicInteger getBalance() {
        return balance;
    }

    public void withdraw(Integer amount) {
//        while (true) {
//            final int prev = balance.get();
//            int next = prev - amount;
//            if (balance.compareAndSet(prev, next)) {
//                break;
//            }
//        }
        balance.addAndGet(-1 * amount);
    }
}
