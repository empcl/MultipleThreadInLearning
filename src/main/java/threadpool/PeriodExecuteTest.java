package threadpool;

import lombok.extern.slf4j.Slf4j;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 每周四18:00:00 定时执行任务
 */
@Slf4j(topic = "c.PeriodExecuteTest")
public class PeriodExecuteTest {
    public static void main(String[] args) {
        final LocalDateTime now = LocalDateTime.now();

        LocalDateTime thursday = now.with(DayOfWeek.THURSDAY)
                .withHour(18).withMinute(0).withSecond(0).withNano(0);

        if(now.compareTo(thursday) > 0) {
            thursday = thursday.plusWeeks(1);
        }

        long initialDelay = Duration.between(now, thursday).toMillis();
        long period = 7 * 24 * 3600 * 1000;

        final ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(2);

        scheduledThreadPool.scheduleAtFixedRate(() -> {
            log.info("now time: {}", new Date());
        }, initialDelay, period, TimeUnit.MILLISECONDS);

    }
}
