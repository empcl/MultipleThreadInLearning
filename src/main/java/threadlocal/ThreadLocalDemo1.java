package threadlocal;

/**
 * 解决了线程隔离的需求
 * 在多线程并发的场景下，每个线程中的变量都是相互独立的
 */
public class ThreadLocalDemo1 {
    private String content;

    ThreadLocal<String> tl = new ThreadLocal<>();

    public String getContent() {
        return tl.get();
    }

    public void setContent(String content) {
        tl.set(content);
    }

    public static void main(String[] args) {
        final ThreadLocalDemo1 tld = new ThreadLocalDemo1();
        for (int i = 0; i < 5; i++) {
            final Thread thread = new Thread(() -> {
                tld.setContent(Thread.currentThread().getName() + "的数据");
                System.out.println(Thread.currentThread().getName() + " ----> " + tld.getContent());
            });

            thread.setName("线程" + i);
            thread.start();
        }
    }
}
