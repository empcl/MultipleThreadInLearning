package thread;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

@Slf4j(topic = "c.TestBiased")
public class TestBiased {
    public static void main(String[] args) {
        final Dog dog = new Dog();
        try {
            final ClassLayout classLayout = ClassLayout.parseInstance(dog);
            final String result = classLayout.toPrintable();
            System.out.println(result);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}

class Dog {

}

