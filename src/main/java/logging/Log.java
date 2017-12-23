package logging;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Log {
    private String title;
    private LocalDateTime lastDataLoggingDate = LocalDateTime.now();
    private int accumulatedDataSinceLastLogging = 0;

    public Log(String title) {
        this.title = title;
    }

    public void logData(byte[] data) {
        accumulatedDataSinceLastLogging += data.length;
        if (ChronoUnit.SECONDS.between(LocalDateTime.now(), lastDataLoggingDate) > 1) {
            System.out.println(title + " => " + accumulatedDataSinceLastLogging + " bytes.");
            accumulatedDataSinceLastLogging = 0;
        }
    }
}