package thread;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

@Slf4j(topic = "c.ThreadUnsafe")
public class ThreadUnsafe {
    ArrayList<String> list = new ArrayList<>();
    static int LOOP_NUMBER = 200;
    static int THREAD_NUMBER = 2;

    public static void main(String[] args) {
        final ThreadUnsafe tusf = new ThreadUnsafe();
        for (int i = 0; i < THREAD_NUMBER; i++) {
            new Thread(() -> {
                tusf.method1(LOOP_NUMBER);
            }, "Thread_" + i).start();
        }
    }

    private void method1(int loopNumber) {
        for (int i = 0; i < loopNumber; i++) {
            method2();
            method3();
        }
    }

    private void method2() {
        list.add("1");
    }

    private void method3() {
        list.remove(0);
    }
}
