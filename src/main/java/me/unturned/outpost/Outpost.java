package me.unturned.outpost;

import me.unturned.outpost.commands.OutpostCommand;
import me.unturned.outpost.events.listeners.BossDamageListener;
import me.unturned.outpost.listeners.PlayerMobDamageListener;
import me.unturned.outpost.listeners.PlayerMobKillListener;
import me.unturned.outpost.objects.Mob;
import me.unturned.outpost.objects.region.SpawnRegion;
import me.unturned.outpost.tasks.LoadBoostsTask;
import me.unturned.outpost.tasks.LoadMobRewardsTask;
import me.unturned.outpost.tasks.LoadRegionTask;
import me.unturned.outpost.tasks.MobSpawnTask;
import me.unturned.outpost.utils.DurationUtil;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
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

        // Commands
        getCommand("outpost").setExecutor(new OutpostCommand(this));

        // Listeners
        getServer().getPluginManager().registerEvents(new PlayerMobDamageListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerMobKillListener(this), this);
        getServer().getPluginManager().registerEvents(new BossDamageListener(), this);

        // Tasks
        new LoadMobRewardsTask(this).runTask(this);
        new LoadBoostsTask(this).runTaskAsynchronously(this);
        new LoadRegionTask().runTaskAsynchronously(this);

        new MobSpawnTask(this).runTaskTimerAsynchronously(this, 40, 20);
    }

    @Override
    public void onDisable() {
        getConfig().set("boosts", scheduledBoosts.stream().map(Instant::toEpochMilli).collect(Collectors.toCollection(ArrayList::new)));
        saveConfig();
    }

    public boolean isBoostTime() {
        if (scheduledBoosts.isEmpty()) {
            new LoadBoostsTask(this).runTaskAsynchronously(this);
            return false;
        }

        final Instant instant = scheduledBoosts.get(0);

        if (DurationUtil.isOver(instant)) {
            scheduledBoosts.remove(instant);
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

    public ArrayList<Instant> getScheduledBoosts() {
        return scheduledBoosts;
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
