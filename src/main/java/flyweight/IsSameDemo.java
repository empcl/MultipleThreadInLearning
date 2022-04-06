package flyweight;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j(topic = "c.IsSameDemo")
public class IsSameDemo {
    public static void main(String[] args) {
        fun2();
    }

    public static void fun2() {
        final String v1 = new String("a");
        final String v2 = new String("a");

        final String v3 = String.valueOf("a");
        final String v4 = String.valueOf("a");

        final String v5 = "a";
        final String v6 = "a";

        System.out.println(v1 == v2);
        System.out.println(v3 == v4);
        System.out.println(v1 == v3);

        System.out.println(v5 == v6);
        System.out.println(v3 == v5);
    }

    public static void fun1() {
        final Long v1 = new Long(5L);
        final Long v2 = new Long(5L);

        final Long v3 = Long.valueOf(5L);
        final Long v4 = Long.valueOf(5L);

        System.out.println(v1 == v2);
        System.out.println(v3 == v4);
        System.out.println(v1 == v3);
    }
}
