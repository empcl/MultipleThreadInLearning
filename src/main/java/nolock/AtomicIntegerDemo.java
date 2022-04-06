package nolock;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerDemo {
    public static void main(String[] args) {
        final AtomicInteger value = new AtomicInteger(0);

        // 获取并自增（value = 0, 结果 value = 1, 返回 0），类似于 value++
        System.out.println(value.getAndIncrement());

        // 自增并获取（value = 1, 结果 value = 2, 返回 2），类似于 ++value
        System.out.println(value.incrementAndGet());

        // 获取并自减（value = 2, 结果 value = 1, 返回 2），类似于 value--
        System.out.println(value.getAndDecrement());

        // 自减并获取（value = 1, 结果 value = 0, 返回 0），类似于 --value
        System.out.println(value.decrementAndGet());

        // 获取并加值（value = 0, 结果 value = 5, 返回 0）
        System.out.println(value.getAndAdd(5));

        // 加值并获取（value = 5, 结果 value = 0, 返回 0）
        System.out.println(value.addAndGet(-5));

        // 获取并更新（value = 0, p 为 value 的当前值, 结果 value = -2, 返回 0）
        // 其中函数中的操作能保证原子，但函数需要无副作用
        System.out.println(value.getAndUpdate(p -> p - 2));

        // 更新并获取（value = -2, p 为 value 的当前值, 结果 value = 0, 返回 0）
        // 其中函数中的操作能保证原子，但函数需要无副作用
        System.out.println(value.updateAndGet(p -> p + 2));

        // 获取并计算（value = 0, p 为 i 的当前值, x 为 参数1, 结果 value = 10, 返回 0）
        // 其中函数中的操作能保证原子，但函数需要无副作用
        // getAndUpdate 如果在 lambda 中引用了外部的局部变量，要保证该局部变量是 final 的
        // getAndAccumulate 可以通过 参数1 来引用外部的局部变量，但因为其不在 lambda 中因此不必是 final
        System.out.println(value.getAndAccumulate(10, (p, x) -> p + x));

        // 计算并获取（value = 10, p 为 value 的当前值, x 为参数1, 结果 value = 0, 返回 0）
        // 其中函数中的操作能保证原子，但函数需要无副作用
        System.out.println(value.accumulateAndGet(-10, (p, x) -> p + x));
    }
}
