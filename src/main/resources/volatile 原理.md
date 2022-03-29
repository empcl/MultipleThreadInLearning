### volatile 原理
volatile的底层实现原理是 **内存屏障**，`Memory Barrier`
* 对volatile变量的写指令后会加入写屏障
* 对volatile变量的读指令前会加入读屏障

#### 如何保证可见行
写屏障（sfence）保证在该屏障之前的，对共享变量的改动，都同步到主存当中
```text
public void actor2(I_Result r) {
 num = 2;
 ready = true; // ready 是 volatile 赋值带写屏障
 // 写屏障
}
```
而读屏障（lfence）保证在该屏障之后，对共享变量的读取，加载的是主存中最新数据
```text
public void actor1(I_Result r) {
 // 读屏障
 // ready 是 volatile 读取值带读屏障
 if(ready) {
   r.r1 = num + num;
 }
}
```
#### 如果保证有序性
写屏障会确保指令重排序时，不会将写屏障之前的代码排在写屏障之后
```text
public void actor2(I_Result r) {
 num = 2;
 ready = true; // ready 是 volatile 赋值带写屏障
 // 写屏障
}
```
读屏障会确保指令重排序时，不会将读屏障之后的代码排在读屏障之前
```text
public void actor1(I_Result r) {
 // 读屏障
 // ready 是 volatile 读取值带读屏障
 if(ready) {
 r.r1 = num + num;
 }
}
```

#### 说明
虽然volatile能够保证可见性、有序性，但是不能解决指令交错的问题。
* 写屏障仅仅是保证之后的读能够读到最新的结果，但不能保证读跑到它前面去（多行程的情况下）
* 而有序性的保证也只是保证了本线程内相关代码不被重排序

### 扩展
#### DCL（double checked locking）问题
#### DCL 解决