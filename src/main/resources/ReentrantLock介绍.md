## ReentrantLock
### ReentrantLock 与 synchronized 对比
相对于 `synchronized` ，它具备如下特点：
* 可中断
* 可以设置超时时间
* 可以设置为公平锁
* 支持多个条件变量

与 synchronized一样，都支持 `可重入`

### ReentrantLock 基本语法
```text
ReentrantLock lock = new ReentrantLock();
// 获取锁
lock.lock();
try {
    // 临界区
} finally {
    // 释放锁
    lock.unlock(); 
}
```

### ReentrantLock 特性介绍
#### 可重入
可重入是指同一个线程如果首次获得了这把锁，那么因为他是这把锁的拥有者，因此有权利在此获取这把锁。
如果是不可重入锁，那么第二次获得锁时，自己也会被锁挡住。

参考代码：`ReentrantLockDemo1.java`

#### 可打断
参考代码：`ReentrantLockDemo2.java`

#### 锁超时
参考代码：`ReentrantLockDemo3.java`
> 使用tryLock() 可以解决 哲学家就餐问题
> 
> 参考代码：`ReentrantLockDemo4.java`

#### 公平锁
ReentrantLock 默认是`不公平`的

公平锁一般没有必要，会降低并发度
 
参考代码：`ReentrantLockDemo5.java`

#### 条件变量
* synchronized 中也有条件变量，就是我们讲原理时那个 waitSet 休息室，当条件不满足时进入 waitSet 等待
* ReentrantLock 的条件变量比 synchronized 强大之处在于，它是支持多个`条件变量`的，这就好比：
  * synchronized 是那些不满足条件的线程都在一间休息室等消息
  * ReentrantLock 支持多间休息室，有专门等烟的休息室、专门等早餐的休息室、唤醒时也是按休息室来唤醒
* 使用要点：
  * await 前需要获得锁
  * await 执行后，会释放锁，进入 conditionObject 等待
  * await 的线程被唤醒（或打断、或超时）取重新竞争 lock 锁
  * 竞争 lock 锁成功后，从 await 后继续执行

参考代码：`ReentrantLockDemo6.java`

### 同步模式之顺序控制
#### 固定顺序运行
题目要求：

必须先 2 后 1 打印输出
##### wait notify 版
参考代码：`reentrantlock.sequentialexecution.Demo1.java`

#### park unpark 版
参考代码：`reentrantlock.sequentialexecution.Demo2.java`

#### 交替输出
题目要求：

线程 1 输出 a 5 次，线程 2 输出 b 5 次，线程 3 输出 c 5 次。
现在要求输出 abcabcabcabcabc 怎么实现

##### wait notify版
参考代码：`reentrantlock.sequentialexecution.Demo3.java`

##### park unpark版
参考代码：`reentrantlock.sequentialexecution.Demo4.java`

##### Lock 条件变量版
参考代码：`reentrantlock.sequentialexecution.Demo5.java`