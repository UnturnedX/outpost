package me.unturned.outpost.tasks;

import me.unturned.outpost.Outpost;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class LoadBoostsTask extends BukkitRunnable {
    private final Outpost outpost;

    public LoadBoostsTask(Outpost outpost) {
        this.outpost = outpost;
    }

    @Override
    public void run() {
        final FileConfiguration config = outpost.getConfig();
        boolean refreshBoosts = config.get("boosts", null) == null || config.getLongList("boosts").isEmpty();

        for (Long boost : config.getLongList("boosts")) {
            final Instant instant = Instant.ofEpochMilli(boost);

            if (Date.from(instant).getDay() != Date.from(Instant.now()).getDay()) {
                refreshBoosts = true;
                break;
            }
            outpost.getScheduledBoosts().add(instant);
        }

        if (refreshBoosts) {
            LocalDateTime startOfDay = LocalDateTime.now()
                    .withHour(0)
                    .withSecond(0)
                    .withMinute(0)
                    .withNano(0);

            for (int i = 0; i < 4; i++) {
                final LocalDateTime plus = startOfDay = startOfDay.plusSeconds(ThreadLocalRandom.current().nextInt(3600, 22600));

                outpost.getScheduledBoosts().add(Instant.ofEpochSecond(plus.toEpochSecond(ZoneOffset.UTC)));
            }
        }
    }
}
