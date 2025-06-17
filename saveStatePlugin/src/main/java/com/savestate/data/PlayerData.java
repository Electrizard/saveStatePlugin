package com.savestate.data;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.Collection;

public class PlayerData {
    private String playerName;
    private String uuid;
    private Location location;
    private float yaw;
    private float pitch;
    private ItemStack[] inventory;
    private ItemStack[] enderChest;
    private ItemStack[] armor;
    private ItemStack offHandItem;
    private int selectedSlot;
    private double health;
    private int foodLevel;
    private float saturation;
    private float exhaustion;
    private int level;
    private float exp;
    private int totalExperience;
    private GameMode gameMode;
    private boolean allowFlight;
    private boolean flying;
    private float flySpeed;
    private float walkSpeed;
    private Collection<PotionEffect> potionEffects;
    
    // Constructors
    public PlayerData() {}
    
    public PlayerData(String playerName, String uuid) {
        this.playerName = playerName;
        this.uuid = uuid;
    }
    
    // Getters and setters
    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }
    
    public String getUuid() { return uuid; }
    public void setUuid(String uuid) { this.uuid = uuid; }
    
    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }
    
    public float getYaw() { return yaw; }
    public void setYaw(float yaw) { this.yaw = yaw; }
    
    public float getPitch() { return pitch; }
    public void setPitch(float pitch) { this.pitch = pitch; }
    
    public ItemStack[] getInventory() { return inventory; }
    public void setInventory(ItemStack[] inventory) { this.inventory = inventory; }
    
    public ItemStack[] getEnderChest() { return enderChest; }
    public void setEnderChest(ItemStack[] enderChest) { this.enderChest = enderChest; }
    
    public ItemStack[] getArmor() { return armor; }
    public void setArmor(ItemStack[] armor) { this.armor = armor; }
    
    public ItemStack getOffHandItem() { return offHandItem; }
    public void setOffHandItem(ItemStack offHandItem) { this.offHandItem = offHandItem; }
    
    public int getSelectedSlot() { return selectedSlot; }
    public void setSelectedSlot(int selectedSlot) { this.selectedSlot = selectedSlot; }
    
    public double getHealth() { return health; }
    public void setHealth(double health) { this.health = health; }
    
    public int getFoodLevel() { return foodLevel; }
    public void setFoodLevel(int foodLevel) { this.foodLevel = foodLevel; }
    
    public float getSaturation() { return saturation; }
    public void setSaturation(float saturation) { this.saturation = saturation; }
    
    public float getExhaustion() { return exhaustion; }
    public void setExhaustion(float exhaustion) { this.exhaustion = exhaustion; }
    
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    
    public float getExp() { return exp; }
    public void setExp(float exp) { this.exp = exp; }
    
    public int getTotalExperience() { return totalExperience; }
    public void setTotalExperience(int totalExperience) { this.totalExperience = totalExperience; }
    
    public GameMode getGameMode() { return gameMode; }
    public void setGameMode(GameMode gameMode) { this.gameMode = gameMode; }
    
    public boolean isAllowFlight() { return allowFlight; }
    public void setAllowFlight(boolean allowFlight) { this.allowFlight = allowFlight; }
    
    public boolean isFlying() { return flying; }
    public void setFlying(boolean flying) { this.flying = flying; }
    
    public float getFlySpeed() { return flySpeed; }
    public void setFlySpeed(float flySpeed) { this.flySpeed = flySpeed; }
    
    public float getWalkSpeed() { return walkSpeed; }
    public void setWalkSpeed(float walkSpeed) { this.walkSpeed = walkSpeed; }
    
    public Collection<PotionEffect> getPotionEffects() { return potionEffects; }
    public void setPotionEffects(Collection<PotionEffect> potionEffects) { this.potionEffects = potionEffects; }
}