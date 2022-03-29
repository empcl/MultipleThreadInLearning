package thread;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

/**
 * 买票练习
 */

@Slf4j(topic = "c.ExerciseSell")
public class ExerciseSell {
    public static void main(String[] args) throws InterruptedException {
        final TicketWindow tw = new TicketWindow(2000);
        final ArrayList<Thread> threadArrayList = new ArrayList<>();
        final Vector<Integer> salesVector = new Vector<>();
        for (int i = 0; i < 2000; i++) {
            final Thread t = new Thread(() -> {
                synchronized (tw) {
                    final int sell = tw.sell(new Random().nextInt(10) + 1);
                    salesVector.add(sell);
                }
            }, "t_" + i);

            threadArrayList.add(t);
            t.start();
        }

        for (Thread t : threadArrayList) {
            t.join();
        }

        final Integer sales = salesVector.stream().reduce(Integer::sum).get();
        log.debug("sales:{}, total : {}",sales, sales + tw.getCount());


    }
}

@Slf4j(topic = "c.TicketWindow")
class TicketWindow {
    private int count;

    public TicketWindow(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public int sell(int sales) {
        if (count >= sales) {
            this.count = this.count - sales;
            return sales;
        }
        return 0;
    }
}
