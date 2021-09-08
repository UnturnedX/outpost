package me.unturned.outpost.utils;

import java.time.Duration;
import java.time.Instant;

public class DurationUtil {

    public static long getSecondsTillEnd(Instant instant) {
        return Duration.between(Instant.now(), instant).getSeconds();
    }

    public static boolean isOver(Instant instant) {
        return instant.isBefore(Instant.now());
    }
}
