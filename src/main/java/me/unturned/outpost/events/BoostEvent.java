package me.unturned.outpost.events;

import me.unturned.outpost.Outpost;
import me.unturned.outpost.objects.Chance;
import me.unturned.outpost.objects.Mob;
import me.unturned.outpost.objects.region.SpawnRegion;
import me.unturned.outpost.tasks.MobSpawnTask;
import me.unturned.outpost.utils.DurationUtil;
import me.unturned.outpost.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;

public class BoostEvent implements Event {
    private final Outpost outpost = Outpost.getInstance();

    private final static Chance[] mobTypes = new Chance[] {
            new Chance(EntityType.ZOMBIE, 70),
            new Chance(EntityType.SKELETON, 15),
            new Chance(EntityType.BLAZE, 10),
            new Chance(EntityType.WITHER_SKELETON, 5)
    };

    @Override
    public void start() {

        // Remove all current alive mobs
        for (Entity entity : outpost.getSpawnedMobs().keySet()) {
            entity.remove();
        }

        final Instant end = Instant.now().plus(15, ChronoUnit.MINUTES);
        Bukkit.broadcastMessage(Util.colour("&aA boost is happening at the Mob Outpost!"));

        new BukkitRunnable() {

            @Override
            public void run() {

                // Begin mini boss
                if (DurationUtil.isOver(end)) {
                    end();
                    cancel();
                    return;
                }

                final SpawnRegion outpostRegion = outpost.getRegion();
                final ArrayList<Entity> toRemove = new ArrayList<>();

                outpost.getSpawnedMobs().forEach((key, mob) -> {
                    if (key.isDead()) {
                        toRemove.add(key);
                    }
                    else if (Duration.between(mob.getLastAttacked(), Instant.now()).getSeconds() > 20) { // If the mob hasn't been attacked in 20 seconds
                        key.remove();
                        toRemove.add(key);
                    }
                });

                toRemove.forEach(entity -> outpost.getSpawnedMobs().remove(entity));

                if (outpost.getSpawnedMobs().size() > 43) return;

                // Find location to spawn
                final ArrayList<Block> blocks = outpostRegion.getBlocks();

                if (blocks.isEmpty()) {
                    return;
                }

                outpost.getServer().getScheduler().runTask(outpost, () -> {
                    Block block = blocks.get(new Random().nextInt(blocks.size()));
                    block = block.getLocation().getWorld().getHighestBlockAt(block.getLocation());

                    final EntityType type = (EntityType) mobTypes[Util.getRandomItem(mobTypes)].object();
                    final Entity entity = block.getWorld().spawnEntity(block.getLocation().add(0, 1, 0), type);

                    outpost.getSpawnedMobs().put(entity, new Mob(entity, Instant.now()));
                });
            }
        }.runTaskTimerAsynchronously(outpost, 0, 10);
    }

    @Override
    public void end() {
        new MobSpawnTask(outpost).runTaskTimerAsynchronously(outpost, 20, 20);
        new BossEvent().start();
    }
}
