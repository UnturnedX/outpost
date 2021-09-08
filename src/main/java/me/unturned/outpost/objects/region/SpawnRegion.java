package me.unturned.outpost.objects.region;

import me.unturned.outpost.Outpost;
import me.unturned.outpost.objects.Config;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;

public class SpawnRegion {
    private Location loc1;
    private Location loc2;

    private World world;
//
    private int minX;
    private int minY;
    private int minZ;
//
    private int maxX;
    private int maxY;
    private int maxZ;

    public SpawnRegion(Location loc1, Location loc2) {
        this.loc1 = loc1;
        this.loc2 = loc2;

        this.world = loc1.getWorld();
        this.minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        this.maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
        this.minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        this.maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
        this.minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        this.maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
    }

    public World getWorld() {
        return world;
    }

    public Location getLocation1() {
        return loc1;
    }

    public Location getLocation2() {
        return loc2;
    }

    public ArrayList<Block> getBlocks() {
        final ArrayList<Block> blocks = new ArrayList<>();

        if (world == null) {
            return blocks;
        }

        for (int x = this.minX; x <= this.maxX; ++x) {
            for (int y = this.minY; y <= this.maxY; ++y) {
                for (int z = this.minZ; z <= this.maxZ; ++z) {
                    blocks.add(world.getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }


    public void setLocation1(Location location) {
        this.world = location.getWorld();
        this.loc1 = location;

        new Config(Outpost.getInstance(), "config.yml").set("outpost.location1", location).save();

        // Refresh block positions
        this.minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        this.maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
        this.minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        this.maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
        this.minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        this.maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
    }

    public void setLocation2(Location location) {
        this.world = location.getWorld();
        this.loc2 = location;

        new Config(Outpost.getInstance(), "config.yml").set("outpost.location2", location).save();

        // Refresh block positions
        this.minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        this.maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
        this.minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        this.maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
        this.minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        this.maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
    }
}
