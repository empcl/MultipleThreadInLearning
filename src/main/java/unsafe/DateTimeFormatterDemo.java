package unsafe;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j(topic = "c.DateTimeFormatterDemo")
public class DateTimeFormatterDemo {
    public static void main(String[] args) {
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                final LocalDate date = dtf.parse("2018-01-01", LocalDate::from);
                log.info("{}",date);
            }).start();
        }
    }
}
