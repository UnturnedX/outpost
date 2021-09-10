package me.unturned.outpost.events;

import me.unturned.outpost.Outpost;
import me.unturned.outpost.objects.region.SpawnRegion;
import me.unturned.outpost.utils.DurationUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Instant;
import java.util.List;

public class BossEvent implements Event {
    private final Outpost outpost = Outpost.getInstance();

    @Override
    public void start() {
        final SpawnRegion region = outpost.getRegion();
        final World world = region.getWorld();

        if (world == null) {
            return;
        }

        final Location midpoint = region.getLocation1().toVector().midpoint(region.getLocation2().toVector()).toLocation(world);
        final Creature boss = (Creature) world.spawnEntity(midpoint, EntityType.ZOMBIE);

        boss.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 10, false, false));
        boss.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 3));
        boss.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 1));
        boss.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(400);
        boss.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(1.0);

        boss.setMetadata("boss", new FixedMetadataValue(outpost, true));

        final EntityEquipment equipment = boss.getEquipment();
        equipment.setHelmet(armour(Material.DIAMOND_HELMET));
        equipment.setChestplate(armour(Material.DIAMOND_CHESTPLATE));
        equipment.setLeggings(armour(Material.DIAMOND_LEGGINGS));
        equipment.setBoots(armour(Material.DIAMOND_BOOTS));

        final ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 3);

        equipment.setItemInMainHand(sword);

        // Handle the boss's target
        new BukkitRunnable() {
            Instant discoveredTarget = Instant.now();
            Player target = null;

            @Override
            public void run() {
                if (boss.isDead()) {
                    cancel();
                    return;
                }

                if (target != null  && !(target.isDead())) {
                    if (DurationUtil.getSecondsTillEnd(discoveredTarget) < 15) {
                        return;
                    }
                }

                final List<Entity> nearbyDiamondPlayers = boss.getNearbyEntities(10, 0, 10);
                nearbyDiamondPlayers.removeIf(nearbyPlayer -> nearbyPlayer instanceof Player player && player.getInventory().getHelmet() != null && player.getInventory().getHelmet().getType().equals(Material.DIAMOND_HELMET));

                if (nearbyDiamondPlayers.isEmpty()) {
                    Player closestPlayer = null;
                    double closestDistance = 100.0D;

                    for (Player player : world.getPlayers()) {
                        if (closestPlayer == null) {
                            closestPlayer = player;
                        }
                        final double distance = closestPlayer.getLocation().distance(boss.getLocation());
                        if (distance < closestDistance) {
                            closestDistance = distance;
                            closestPlayer = player;
                        }
                    }

                    target = closestPlayer;
                }
                else {
                    target = (Player) nearbyDiamondPlayers.get(0);
                    discoveredTarget = discoveredTarget.plusSeconds(15);
                }

                boss.setTarget(target);
            }
        }.runTaskTimer(outpost, 200, 10);
    }

    @Override
    public void end() {

    }

    private ItemStack armour(Material material) {
        final ItemStack item = new ItemStack(material);
        item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);

        return item;
    }
}
