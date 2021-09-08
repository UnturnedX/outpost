package me.unturned.outpost.objects;

import org.bukkit.entity.Entity;

import java.time.Instant;

public class Mob {

    private Entity entity;
    private Instant lastAttacked;

    public Mob(Entity entity, Instant lastAttacked) {
        this.entity = entity;
        this.lastAttacked = lastAttacked;
    }

    public Instant getLastAttacked() {
        return lastAttacked;
    }

    public void setLastAttacked(Instant lastAttacked) {
        this.lastAttacked = lastAttacked;
    }

    public Entity getEntity() {
        return entity;
    }
}
