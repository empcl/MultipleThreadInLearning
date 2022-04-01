package threadpool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class CalAddNumTest1 {
    public static void main(String[] args) {
        final ForkJoinPool pool = new ForkJoinPool(1);
        pool.invoke(new AddTask2(1, 5));
    }
}

/**
 * 二分法
 */
@Slf4j(topic = "c.AddTask2")
class AddTask2 extends RecursiveTask<Integer> {

    private int begin;
    private int end;

    public AddTask2(int begin, int end) {
        this.begin = begin;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        if (begin == end) {
            log.debug("join () {}", begin);
            return begin;
        }

        if (end - begin == 1) {
            log.debug("join () {} - {}", begin, end);
            return begin + end;
        }

        int mid = (end + begin) / 2;
        final AddTask2 t1 = new AddTask2(begin, mid);
        t1.fork();

        final AddTask2 t2 = new AddTask2(mid + 1, end);
        t2.fork();

        log.debug("fork {} - {}", t1, t2);

        int result = t1.join() + t2.join();

        log.debug("join {} + {} = {}", t1, t2, result);
        return result;
    }

    @Override
    public String toString() {
        return "AddTask2{" +
                "begin=" + begin +
                ", end=" + end +
                '}';
    }
}

@Slf4j(topic = "c.AddTask1")
class AddTask1 extends RecursiveTask<Integer> {

    private int n;

    public AddTask1(int n) {
        this.n = n;
    }

    @Override
    protected Integer compute() {
        if (n == 1) {
            log.debug("join() {}", n);
            return n;
        }

        final AddTask1 addTask1 = new AddTask1(n - 1);
        addTask1.fork();
        log.debug("fork() {} + {}", n, addTask1);

        final int result = n + addTask1.join();

        log.debug("result: {}", result);

        return result;
    }

    @Override
    public String toString() {
        return "AddTask1{" +
                "n=" + n +
                '}';
    }
}
