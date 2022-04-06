### 设置final变量的原理
理解了volatile原理，再对比final的实现就比较简单了。
```java
public class TestFinal {
    final int a = 20;
}
```
字节码
```text
0: aload_0
1: invokespecial #1 // Method java/lang/Object."<init>":()V 
4: aload_0
5: bipush        20
7: putfield     #2 // Field a:I  
    <-- 写屏障
10: return
#2
```
发现 final 变量的赋值也会通过 putfield 指令来完成，同样在这条指令之后也会加入写屏障，
保证在其它线程读到它的值时不会出现为0的情况。
