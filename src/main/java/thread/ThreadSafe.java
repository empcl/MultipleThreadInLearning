package thread;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

@Slf4j(topic = "c.ThreadSafe")
public class ThreadSafe {
    static int LOOP_NUMBER = 200000;
    static int THREAD_NUMBER = 2;

    public static void main(String[] args) {
        final ThreadSafe tusf = new ThreadSafe();
        for (int i = 0; i < THREAD_NUMBER; i++) {
            new Thread(() -> {
                tusf.method1(LOOP_NUMBER);
            }, "Thread_" + i).start();
        }
    }

    private void method1(int loopNumber) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < loopNumber; i++) {
            method2(list);
            method3(list);
        }
    }

    private void method2(ArrayList<String> list) {
        list.add("1");
    }

    private void method3(ArrayList<String> list) {
        list.remove(0);
    }
}
