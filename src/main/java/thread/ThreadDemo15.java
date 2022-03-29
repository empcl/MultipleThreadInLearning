package thread;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 生产者消费者
 * wait/notifyAll
 */
@Slf4j(topic = "c.ThreadDemo15")
public class ThreadDemo15 {
    public static void main(String[] args) {
        final MessageQueue messageQueue = new MessageQueue(2);
        for (int i = 0; i < 3; i++) {
            int id = i;
            new Thread(() -> {
                messageQueue.put(new Message(id, "content - " + id));
            }, "producer_" + id).start();
        }

        for (int i = 0; i < 3; i++) {
            int id = i;
            new Thread(() -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                final Message message = messageQueue.take();
                log.info("message : {}", message);
            }, "consumer_" + id).start();
        }
    }
}

@Slf4j(topic = "c.MessageQueue")
class MessageQueue {
    private LinkedList<Message> messages = new LinkedList<>();
    private int capacity = 0;

    public MessageQueue(int capacity) {
        this.capacity = capacity;
    }

    public Message take() {
        synchronized (messages) {
            while (messages.isEmpty()) {
                try {
                    messages.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug("take data...");
            final Message message = messages.removeFirst();
            log.debug("consumed data : {}", message);
            messages.notifyAll();
            return message;
        }
    }

    public void put(Message message) {
        synchronized (messages) {
            while (messages.size() >= capacity) {
                try {
                    log.info("waiting...");
                    messages.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            messages.add(message);
            log.info("put data successfully, data : {}", message);
            messages.notifyAll();
        }
    }
}

@Data
@AllArgsConstructor
class Message {
    private int id;
    private String msg;
}
