package me.unturned.outpost.listeners;

import me.unturned.outpost.Outpost;
import me.unturned.outpost.utils.Util;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public record PlayerMobKillListener(Outpost outpost) implements Listener {

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        final LivingEntity entity = event.getEntity();

        if (!(outpost.getSpawnedMobs().containsKey(entity))) return;
        if (!(outpost.getRewards().containsKey(entity.getType()))) return;
        if (entity.getKiller() == null) return;

        final Player killer = entity.getKiller();
        final int amount = outpost.getRewards().get(entity.getType());

        outpost.getEconomy().depositPlayer(killer, amount);
        killer.sendActionBar(Util.colour("&a+ $" + amount));

        outpost.getSpawnedMobs().remove(entity);
    }
}