package me.unturned.outpost.tasks;

import me.unturned.outpost.Outpost;
import me.unturned.outpost.events.BoostEvent;
import me.unturned.outpost.objects.Chance;
import me.unturned.outpost.objects.Mob;
import me.unturned.outpost.objects.region.SpawnRegion;
import me.unturned.outpost.utils.Util;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Random;

public class MobSpawnTask extends BukkitRunnable {
    private final static Chance[] mobTypes = new Chance[] {
            new Chance(EntityType.ZOMBIE, 50),
            new Chance(EntityType.SKELETON, 20)
    };

    private final Outpost outpost;

    public MobSpawnTask(Outpost outpost) {
        this.outpost = outpost;
    }

    @Override
    public void run() {
        final SpawnRegion outpostRegion = outpost.getRegion();

        if (outpost.isBoostTime()) {
            new BoostEvent().start();
            cancel();
            return;
        }

        if (outpostRegion == null || outpostRegion.getLocation1() == null || outpostRegion.getLocation2() == null) return;

        final ArrayList<Entity> toRemove = new ArrayList<>();

        outpost.getSpawnedMobs().forEach((key, mob) -> {
            if (key.isDead()) {
                toRemove.add(key);
            }
            else if (Duration.between(mob.getLastAttacked(), Instant.now()).getSeconds() > 20) {
                key.remove();
                toRemove.add(key);
            }
        });

        toRemove.forEach(entity -> outpost.getSpawnedMobs().remove(entity));

        if (outpost.getSpawnedMobs().size() > 23) return;

        outpost.getServer().getScheduler().runTask(outpost, () -> {

            // Find location to spawn
            final ArrayList<Block> blocks = outpostRegion.getBlocks();

            if (blocks.isEmpty()) {
                return;
            }

            Block block = blocks.get(new Random().nextInt(blocks.size()));
            block = block.getLocation().getWorld().getHighestBlockAt(block.getLocation()); // get the highest block of found location

            final EntityType type = (EntityType) mobTypes[Util.getRandomItem(mobTypes)].object();
            final LivingEntity entity = (LivingEntity) block.getWorld().spawnEntity(block.getLocation().add(0, 1, 0), type);
            entity.setCustomName(Util.colour(outpost.getConfig().getString("mob-custom-name", "").replace("%hp%", String.valueOf(entity.getHealth()))));

            outpost.getSpawnedMobs().put(entity, new Mob(entity, Instant.now()));
        });
    }
}
