package me.unturned.outpost.tasks;

import me.unturned.outpost.Outpost;
import me.unturned.outpost.objects.Config;
import me.unturned.outpost.objects.region.SpawnRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public class LoadRegionTask extends BukkitRunnable {
    private final static Location location = new Location(Bukkit.getWorld("null"), 0, 0, 0);

    @Override
    public void run() {
        final Outpost outpost = Outpost.getInstance();
        final Config config = new Config(outpost, "config.yml");

        if (!(config.exists())) {
            config.create();
            outpost.setRegion(new SpawnRegion(location, location));
            return;
        }

        final Location loc1 = (Location) config.get("outpost.location1");
        final Location loc2 = (Location) config.get("outpost.location2");

        if (loc1 == null || loc2 == null) {
            outpost.setRegion(new SpawnRegion(location, location));
            return;
        }

        outpost.setRegion(new SpawnRegion(loc1, loc2));
    }
}
