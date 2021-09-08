//package me.unturned.outpost.objects.region;
//
//import org.bukkit.Location;
//import org.bukkit.World;
//import org.bukkit.block.Block;
//
//import java.util.ArrayList;
//
//public class Region {
//    private World world;
//
//    private int minX;
//    private int minY;
//    private int minZ;
//
//    private int maxX;
//    private int maxY;
//    private int maxZ;
//
//    public Region(Location loc1, Location loc2) {
//        this.world = loc1.getWorld();
//
//        this.minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
//        this.maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
//        this.minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
//        this.maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
//        this.minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
//        this.maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
//    }
//
//    public void update(Location loc1, Location loc2) {
//        this.world = loc1.getWorld();
//
//        this.minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
//        this.maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
//        this.minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
//        this.maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
//        this.minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
//        this.maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
//    }
//
//    public ArrayList<Block> getBlocks() {
//        final ArrayList<Block> blocks = new ArrayList<>();
//
//        if (world == null) {
//            return blocks;
//        }
//
//        for (int x = this.minX; x <= this.maxX; ++x) {
//            for (int y = this.minY; y <= this.maxY; ++y) {
//                for (int z = this.minZ; z <= this.maxZ; ++z) {
//                    blocks.add(world.getBlockAt(x, y, z));
//                }
//            }
//        }
//        return blocks;
//    }
//
//    public World getWorld() {
//        return world;
//    }
//
//    public void setWorld(World world) {
//        this.world = world;
//    }
//}
