package me.unturned.outpost.events.listeners;

import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class BossDamageListener implements Listener {

    /*
    * This prevents the boss from being attacked if it
    * has only been spawned for 10 seconds
    * */

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Zombie zombie)) return;
        if (!(zombie.hasMetadata("boss"))) return;

        if (event.getEntity().getTicksLived() < 200) {
            event.setCancelled(true);
        }
    }
}
