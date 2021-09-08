package me.unturned.outpost;

import me.unturned.outpost.commands.OutpostCommand;
import me.unturned.outpost.listeners.PlayerMobDamageListener;
import me.unturned.outpost.listeners.PlayerMobKillListener;
import me.unturned.outpost.objects.Mob;
import me.unturned.outpost.objects.region.SpawnRegion;
import me.unturned.outpost.tasks.LoadMobRewardsTask;
import me.unturned.outpost.tasks.LoadRegionTask;
import me.unturned.outpost.tasks.MobSpawnTask;
import me.unturned.outpost.utils.DurationUtil;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Outpost extends JavaPlugin {
    private static Outpost instance;
    private Economy economy;

    private final ArrayList<Instant> scheduledBoosts = new ArrayList<>();
    private final HashMap<EntityType, Integer> rewards = new HashMap<>();
    private final HashMap<Entity, Mob> spawnedMobs = new HashMap<>();

    private SpawnRegion region;

    @Override
    public void onEnable() {
        instance = this;
        economy = getServer().getServicesManager().getRegistration(Economy.class).getProvider();

        loadBoosts();

        // Commands
        getCommand("outpost").setExecutor(new OutpostCommand(this));

        // Listeners
        getServer().getPluginManager().registerEvents(new PlayerMobDamageListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerMobKillListener(this), this);

        // Tasks
        new LoadMobRewardsTask(this).runTask(this);
        new LoadRegionTask().runTaskAsynchronously(this);

        new MobSpawnTask(this).runTaskTimerAsynchronously(this, 40, 20);
    }

    @Override
    public void onDisable() {

        // Save boost timers
        getConfig().set("boosts", scheduledBoosts.stream().map(Instant::toEpochMilli).collect(Collectors.toCollection(ArrayList::new)));
        saveConfig();
    }

    public void loadBoosts() {
        final FileConfiguration config = getConfig();
        boolean refreshBoosts = config.get("boosts", null) == null;

        for (Long boost : config.getLongList("boosts")) {
            final Instant instant = Instant.ofEpochMilli(boost);

            if (config.getLongList("boosts").isEmpty() || Date.from(instant).getDay() != Date.from(Instant.now()).getDay()) {
                refreshBoosts = true;
                break;
            }
            scheduledBoosts.add(instant);
        }

        if (refreshBoosts) {
            for (int i = 0; i < 4; i++) {
                // TODO finalize the time arrangement
                scheduledBoosts.add(Instant.now().plusSeconds(ThreadLocalRandom.current().nextInt(100, 86400)));
            }
        }
    }

    public boolean isBoostTime() {
        Instant instant = null;
        for (Instant scheduledBoost : scheduledBoosts) {
            if (instant == null) {
                instant = scheduledBoost;
            }
            else if (instant.isBefore(scheduledBoost)) {
                instant = scheduledBoost;
            }
        }
        return DurationUtil.isOver(instant);
    }

    public static Outpost getInstance() {
        return instance;
    }

    public HashMap<Entity, Mob> getSpawnedMobs() {
        return spawnedMobs;
    }

    public HashMap<EntityType, Integer> getRewards() {
        return rewards;
    }

    public Economy getEconomy() {
        return economy;
    }

    public SpawnRegion getRegion() {
        return region;
    }

    public void setRegion(SpawnRegion region) {
        this.region = region;
    }
}
