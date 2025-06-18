package com.savestate.data;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class EntityData {
    private EntityType entityType;
    private Location location;
    private String nbtData;
    private double health;
    private float yaw;
    private float pitch;
    
    public EntityData() {}
    
    public EntityData(EntityType entityType, Location location) {
        this.entityType = entityType;
        this.location = location;
    }
    
    // Getters and setters
    public EntityType getEntityType() { return entityType; }
    public void setEntityType(EntityType entityType) { this.entityType = entityType; }
    
    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }
    
    public String getNbtData() { return nbtData; }
    public void setNbtData(String nbtData) { this.nbtData = nbtData; }
    
    public double getHealth() { return health; }
    public void setHealth(double health) { this.health = health; }
    
    public float getYaw() { return yaw; }
    public void setYaw(float yaw) { this.yaw = yaw; }
    
    public float getPitch() { return pitch; }
    public void setPitch(float pitch) { this.pitch = pitch; }
}