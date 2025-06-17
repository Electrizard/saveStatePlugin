package com.savestate.data;

import org.bukkit.Material;

public class SavedBlockData {
    private int x, y, z;
    private Material material;
    private String blockDataString;
    private String nbtData; // For tile entities like chests, signs, etc.
    
    public SavedBlockData() {}
    
    public SavedBlockData(int x, int y, int z, Material material, String blockDataString) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.material = material;
        this.blockDataString = blockDataString;
    }
    
    // Getters and setters
    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    
    public int getZ() { return z; }
    public void setZ(int z) { this.z = z; }
    
    public Material getMaterial() { return material; }
    public void setMaterial(Material material) { this.material = material; }
    
    public String getBlockDataString() { return blockDataString; }
    public void setBlockDataString(String blockDataString) { this.blockDataString = blockDataString; }
    
    public String getNbtData() { return nbtData; }
    public void setNbtData(String nbtData) { this.nbtData = nbtData; }
    
    public String getLocationKey() {
        return x + "," + y + "," + z;
    }
}