package me.unturned.outpost.listeners;

import me.unturned.outpost.Outpost;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.time.Instant;

public record PlayerMobDamageListener(Outpost outpost) implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        if (!(outpost.getSpawnedMobs().containsKey(event.getEntity()))) return;

        final Entity entity = event.getEntity();
        outpost.getSpawnedMobs().get(entity).setLastAttacked(Instant.now());
    }
}
