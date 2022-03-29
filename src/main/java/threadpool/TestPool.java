package threadpool;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j(topic = "c.TestPool")
public class TestPool {
    public static void main(String[] args) {
        RejectPolicy<Runnable> rejectPolicy;
        // 1 死等
//        rejectPolicy = BlockQueue::put;

        // 2 带超时等待
//        rejectPolicy = (queue, task) -> queue.offer(task, 500, TimeUnit.MILLISECONDS);

        // 3 让调用者放弃任务执行
//        rejectPolicy = (queue, task) -> log.info("abort:{}", task);

        // 4 让调用者抛出异常，剩余任务将不再执行
//        rejectPolicy = (queue, task) -> {throw new RuntimeException("throw task: {}" + task);};

        // 5 让调用者自己执行任务
        rejectPolicy = (queue, task) -> task.run();

        final ThreadPool threadPool = new ThreadPool(1, 500, TimeUnit.MILLISECONDS, 1, rejectPolicy);
        for (int i = 0; i < 4; i++) {
            final int t = i;
            threadPool.execute(() -> {
                try {
                    TimeUnit.SECONDS.sleep(1L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("current thread id : {}", t);
            });
        }
    }
}

@Slf4j(topic = "c.ThreadPool")
class ThreadPool {
    private BlockQueue<Runnable> taskQueue;

    private final HashSet<Worker> workers = new HashSet<>();

    /**
     * 获取任务的超时时间
     */
    private long timeout;
    private TimeUnit unit;

    /**
     * 核心线程数
     */
    private int coreSize;

    private RejectPolicy<Runnable> rejectPolicy;

    public ThreadPool(int coreSize, long timeout, TimeUnit unit, int queueCapacity, RejectPolicy<Runnable> rejectPolicy) {
        this.timeout = timeout;
        this.unit = unit;
        this.coreSize = coreSize;
        this.taskQueue = new BlockQueue<>(queueCapacity);
        this.rejectPolicy = rejectPolicy;
    }

    public void execute(Runnable task) {
        // 当任务数没有超过coreSize时，直接交给worker对象执行
        // 如果任务数超过coreSize，加入任务队列暂存
        synchronized (workers) {
            if (workers.size() < coreSize) {
                final Worker worker = new Worker(task);
                log.info("add worker:{}, task:{}", worker, task);
                workers.add(worker);
                worker.start();
            } else {
                taskQueue.tryPut(rejectPolicy, task);
            }
        }
    }

    class Worker extends Thread {
        private Runnable task;

        public Worker(Runnable task) {
            this.task = task;
        }

        @Override
        public void run() {
            // 执行任务
            // 1）当task不为空时，执行任务
            // 2）当task执行完毕，再接着从任务队列获取任务并执行
//            while (task != null || (task = taskQueue.take()) != null) {
            // 使用拒绝策略的话，要使用poll取task
            while (task != null || (task = taskQueue.poll(timeout, unit)) != null) {
                try {
                    log.info("current execute...{}", task);
                    task.run();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    task = null;
                }
            }

            synchronized (workers) {
                log.info("remove worker:{}", this);
                workers.remove(this);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Worker worker = (Worker) o;
            return Objects.equals(task, worker.task);
        }

        @Override
        public int hashCode() {
            return Objects.hash(task);
        }
    }
}

/**
 * 1）死等
 * 2）带超时时间等待
 * 3）放弃任务执行
 * 4）抛出异常
 * 5）调用者自己执行任务
 */
interface RejectPolicy<T> {
    void reject(BlockQueue<T> queue, T task);
}

@Slf4j(topic = "c.BlockQueue")
class BlockQueue<T> {
    private int capacity;

    private ArrayDeque<T> queue = new ArrayDeque<>(capacity);

    private final ReentrantLock lock = new ReentrantLock();

    final Condition fullWaitConn = lock.newCondition();
    final Condition emptyWaitConn = lock.newCondition();

    public BlockQueue(int capacity) {
        this.capacity = capacity;
    }

    public T poll(long timeout, TimeUnit unit) {
        lock.lock();
        try {
            long millis = unit.toMillis(timeout);
            while (queue.isEmpty()) {
                try {
                    if (millis <= 0) {
                        return null;
                    }
                    millis = emptyWaitConn.awaitNanos(millis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            final T t = queue.removeFirst();
            fullWaitConn.signalAll();

            return t;
        } finally {
            lock.unlock();
        }
    }

    public T take() {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                try {
                    emptyWaitConn.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            final T task = queue.removeFirst();
            fullWaitConn.signalAll();

            return task;
        } finally {
            lock.unlock();
        }
    }

    public void put(T element) {
        lock.lock();
        try {
            while (queue.size() == capacity) {
                try {
                    log.warn("waiting to add task queue....{}", element);
                    fullWaitConn.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            log.debug("add taskQueue:{}", element);
            queue.addLast(element);
            emptyWaitConn.signalAll();
        } finally {
            lock.unlock();
        }
    }

    // 带超时时间阻塞添加
    public boolean offer(T task, long timeout, TimeUnit unit) {
        lock.lock();
        try {
            long nanos = unit.toNanos(timeout);
            while (queue.size() == capacity) {
                log.warn("waiting to add task queue....{}", task);
                if (nanos < 0) {
                    return false;
                }
                try {
                    nanos = fullWaitConn.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug("add taskQueue:{}", task);
            queue.addLast(task);
            emptyWaitConn.signalAll();
            return true;
        } finally {
            lock.unlock();
        }
    }

    public int size() {
        lock.lock();
        try {
            return queue.size();
        } finally {
            lock.unlock();
        }
    }

    public void tryPut(RejectPolicy<T> rejectPolicy, T task) {
        lock.lock();
        try {
            // 判断队列是否满
            if (queue.size() == capacity) {
                rejectPolicy.reject(this, task);
            } else { // 有空闲
                log.info("add task queue:{}", task);
                queue.addLast(task);
                emptyWaitConn.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }
}

