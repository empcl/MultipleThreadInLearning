## System.out.println()保证变量的可见行
### 场景复现
```java
public class Demo1 {
    static boolean run = true;
    public static void main(String[] args) throws InterruptedException {

        final Thread t1 = new Thread(() -> {
            while (run) {

            }
        });

        t1.start();

        TimeUnit.SECONDS.sleep(1);

        run = false;
    }
}
```
在上述代码中，我们知道由于普通变量修改在线程间不可见性，会导致程序永远都停不了。除了对变量 `run` 进行volatile修饰或者加锁等方式外，
我们可以在 `while(true) {}` 循环体内加入`System.out.println();`，也可以实现变量的可见行。

### 原因分析
* 获得同步锁
* 清空工作内存
* 从主内存拷贝对象副本到工作内存
* 执行代码(计算或者输出等)
* 刷新主内存数据
* 释放同步锁