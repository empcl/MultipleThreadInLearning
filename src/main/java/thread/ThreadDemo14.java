package thread;

import lombok.extern.slf4j.Slf4j;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class ThreadDemo14 {
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            new People().start();
        }
        TimeUnit.SECONDS.sleep(1);
        for (Integer id : MailBoxes.getIds()) {
            new Postman(id, "content - " + id).start();
        }
    }
}

@Slf4j(topic = "c.People")
class People extends Thread {
    @Override
    public void run() {
        final GuardedObjectv1 guardedObject = MailBoxes.createGuardedObject();
        log.debug("start get message, id: {}", guardedObject.getId());
        final Object mail = guardedObject.get(5000L);
        log.debug("get message id:{}, content:{}", guardedObject.getId(), mail);
    }
}

@Slf4j(topic = "c.Postman")
class Postman extends Thread{
    private int id;
    private String mail;

    public Postman(int id, String mail) {
        this.id = id;
        this.mail = mail;
    }

    @Override
    public void run() {
        final GuardedObjectv1 go = MailBoxes.getGuardedObjectv1(id);
        log.debug("send message id:{}, contentba:{}", id, mail);
        go.complete(mail);
    }
}

class  MailBoxes {
    private static Map<Integer, GuardedObjectv1> boxes = new Hashtable<>();
    private static int id = 1;
    // 产生唯一的id
    public static synchronized int generateId() {
        return id++;
    }

    public static GuardedObjectv1 createGuardedObject() {
        final GuardedObjectv1 go = new GuardedObjectv1(generateId());
        boxes.put(go.getId(), go);
        return go;
    }

    public static GuardedObjectv1 getGuardedObjectv1(int id) {
        return boxes.remove(id);
    }

    public static Set<Integer> getIds() {
        return boxes.keySet();
    }
}

class GuardedObjectv1 {
    private int id;
    private Object response;

    public GuardedObjectv1(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Object get(long timeout) {
        synchronized (this) {
            // 开始时间
            long begin = System.currentTimeMillis();
            // 经历的时间
            long passedTime = 0;
            while (response == null) {
                if (passedTime >= timeout) {
                    break;
                }
                try {
                    this.wait(timeout - passedTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                passedTime = System.currentTimeMillis() - begin;
            }
            return response;
        }
    }

    public void complete(Object response) {
        synchronized (this) {
            this.response = response;
            this.notifyAll();
        }
    }

}