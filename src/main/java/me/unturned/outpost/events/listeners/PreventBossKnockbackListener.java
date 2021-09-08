package me.unturned.outpost.events.listeners;

import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.BlockVector;

public class PreventBossKnockbackListener implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Zombie zombie)) return;
        if (!(zombie.hasMetadata("boss"))) return;

        zombie.setVelocity(new BlockVector(0, 0, 0));
    }
}
