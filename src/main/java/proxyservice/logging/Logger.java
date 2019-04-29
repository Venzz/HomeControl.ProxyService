package proxyservice.logging;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Logger {
    private LocalDateTime lastDataLoggingDate = LocalDateTime.now();
    private int accumulatedDataSinceLastLogging = 0;

    public Logger() {
    }

    public void log(String title, String message) {
        System.out.println(title + " => " + message);
    }

    public void log(String title, byte[] data) {
        accumulatedDataSinceLastLogging += data.length;
        if (ChronoUnit.SECONDS.between(lastDataLoggingDate, LocalDateTime.now()) > 1) {
            System.out.println(title + " => " + accumulatedDataSinceLastLogging + " bytes.");
            lastDataLoggingDate = LocalDateTime.now();
            accumulatedDataSinceLastLogging = 0;
        }
    }
}
