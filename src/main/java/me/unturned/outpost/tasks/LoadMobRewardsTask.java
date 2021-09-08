package me.unturned.outpost.tasks;

import me.unturned.outpost.Outpost;
import me.unturned.outpost.objects.Config;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Locale;

public class LoadMobRewardsTask extends BukkitRunnable {
    private final Outpost outpost;

    public LoadMobRewardsTask(Outpost outpost) {
        this.outpost = outpost;
    }

    @Override
    public void run() {
        final Config config = new Config(outpost, "config.yml");
        if (!(config.exists())) {
            config.create();
        }

        config.set("rewards.ZOMBIE", 25)
              .set("rewards.SKELETON", 40)
              .set("rewards.BLAZE", 100)
              .set("rewards.WITHER_SKELETON", 100)
              .save();

        config.getConfigurationSection("rewards")
                .getKeys(false)
                .forEach(entity -> outpost.getRewards().put(EntityType.valueOf(entity.toUpperCase(Locale.ROOT)), config.getInt("rewards." + entity)));
    }
}
