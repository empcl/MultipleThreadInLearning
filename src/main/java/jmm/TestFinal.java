package jmm;

import static jmm.TestFinal.A;
import static jmm.TestFinal.B;

public class TestFinal {
    final static int A = 10;
    final static int B = Short.MAX_VALUE;

    final int a = 20;
    final int b = Integer.MAX_VALUE;
}

class UseFinal1 {
    public void test() {
        System.out.println(A);
        System.out.println(B);
        System.out.println(new TestFinal().a);
        System.out.println(new TestFinal().b);
    }
}

class UseFinal2 {
    public void test() {
        System.out.println(A);
    }
}
