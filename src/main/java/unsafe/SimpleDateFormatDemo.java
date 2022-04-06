package unsafe;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;

@Slf4j(topic = "c.SimpleDataFormatDemo")
public class SimpleDateFormatDemo {
    public static void main(String[] args) {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                try {
                    log.debug("{}", sdf.parse("1951-05-21"));
                } catch (Exception e) {
                    log.error("{}", e);
                }
            }).start();
        }

    }
}
