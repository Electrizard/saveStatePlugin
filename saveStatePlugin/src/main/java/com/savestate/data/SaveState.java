package com.savestate.data;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaveState {
    private String name;
    private String worldName;
    private long timestamp;
    private Location centerLocation;
    private Map<String, PlayerData> playerData;
    private Map<String, SavedBlockData> blockData;
    private List<EntityData> entityData;
    private long worldTime;
    private boolean isStorm;
    private boolean isThundering;
    private int weatherDuration;
    private int thunderDuration;
    
    public SaveState(String name, String worldName, Location centerLocation) {
        this.name = name;
        this.worldName = worldName;
        this.centerLocation = centerLocation;
        this.timestamp = System.currentTimeMillis();
        this.playerData = new HashMap<>();
        this.blockData = new HashMap<>();
    }
    
    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getWorldName() { return worldName; }
    public void setWorldName(String worldName) { this.worldName = worldName; }
    
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    
    public Location getCenterLocation() { return centerLocation; }
    public void setCenterLocation(Location centerLocation) { this.centerLocation = centerLocation; }
    
    public Map<String, PlayerData> getPlayerData() { return playerData; }
    public void setPlayerData(Map<String, PlayerData> playerData) { this.playerData = playerData; }
    
    public Map<String, SavedBlockData> getBlockData() { return blockData; }
    public void setBlockData(Map<String, SavedBlockData> blockData) { this.blockData = blockData; }
    
    public List<EntityData> getEntityData() { return entityData; }
    public void setEntityData(List<EntityData> entityData) { this.entityData = entityData; }
    
    public long getWorldTime() { return worldTime; }
    public void setWorldTime(long worldTime) { this.worldTime = worldTime; }
    
    public boolean isStorm() { return isStorm; }
    public void setStorm(boolean storm) { isStorm = storm; }
    
    public boolean isThundering() { return isThundering; }
    public void setThundering(boolean thundering) { isThundering = thundering; }
    
    public int getWeatherDuration() { return weatherDuration; }
    public void setWeatherDuration(int weatherDuration) { this.weatherDuration = weatherDuration; }
    
    public int getThunderDuration() { return thunderDuration; }
    public void setThunderDuration(int thunderDuration) { this.thunderDuration = thunderDuration; }
}