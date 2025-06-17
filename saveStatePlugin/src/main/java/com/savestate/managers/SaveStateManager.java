package com.savestate.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.savestate.SaveStatePlugin;
import com.savestate.data.SaveState;
import com.savestate.data.PlayerData;
import com.savestate.data.SavedBlockData;
import com.savestate.data.EntityData;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class SaveStateManager {
    private final SaveStatePlugin plugin;
    private final File saveStatesFolder;
    private final Gson gson;
    private final Map<String, SaveState> loadedSaveStates;
    private static final int SAVE_RADIUS = 100; // 200x200 area (100 blocks in each direction)

    public SaveStateManager(SaveStatePlugin plugin) {
        this.plugin = plugin;
        this.saveStatesFolder = new File(plugin.getDataFolder(), "savestates");
        if (!saveStatesFolder.exists()) {
            saveStatesFolder.mkdirs();
        }

        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Location.class, new LocationTypeAdapter())
                .registerTypeAdapter(ItemStack.class, new ItemStackTypeAdapter())
                .create();

        this.loadedSaveStates = new HashMap<>();
        loadAllSaveStates();
    }

    public SaveStatePlugin getPlugin() {
        return plugin;
    }

    public boolean createSaveState(String name, Player creator) {
        if (loadedSaveStates.containsKey(name)) {
            return false; // Save state already exists
        }

        Location centerLocation = creator.getLocation();
        World world = centerLocation.getWorld();

        SaveState saveState = new SaveState(name, world.getName(), centerLocation);

        // Save all online players in the same world
        for (Player player : world.getPlayers()) {
            PlayerData playerData = capturePlayerData(player);
            saveState.getPlayerData().put(player.getUniqueId().toString(), playerData);
        }

        // Save blocks in radius (this is the heavy operation)
        plugin.getLogger().info("Starting block capture for save state: " + name);
        Map<String, SavedBlockData> blockData = captureBlockData(world, centerLocation);
        saveState.setBlockData(blockData);
        plugin.getLogger().info("Captured " + blockData.size() + " blocks for save state: " + name);

        // Save entities in radius
        List<EntityData> entityData = captureEntityData(world, centerLocation);
        saveState.setEntityData(entityData);
        plugin.getLogger().info("Captured " + entityData.size() + " entities for save state: " + name);

        // Save to file and memory
        saveSaveStateToFile(saveState);
        loadedSaveStates.put(name, saveState);

        return true;
    }

    public boolean loadSaveState(String name, Player executor) {
        SaveState saveState = loadedSaveStates.get(name);
        if (saveState == null) {
            return false;
        }

        World world = Bukkit.getWorld(saveState.getWorldName());
        if (world == null) {
            return false;
        }

        plugin.getLogger().info("Loading save state: " + name);

        // Restore players first
        for (Player player : world.getPlayers()) {
            PlayerData playerData = saveState.getPlayerData().get(player.getUniqueId().toString());
            if (playerData != null) {
                restorePlayerData(player, playerData);
            }
        }

        // Clear existing entities in the area first
        clearEntitiesInArea(world, saveState.getCenterLocation());

        // Restore blocks (this is the heavy operation)
        restoreBlockData(world, saveState.getBlockData());

        // Restore entities
        restoreEntityData(world, saveState.getEntityData());

        plugin.getLogger().info("Successfully loaded save state: " + name);
        return true;
    }

    private PlayerData capturePlayerData(Player player) {
        PlayerData data = new PlayerData(player.getName(), player.getUniqueId().toString());

        data.setLocation(player.getLocation());
        data.setYaw(player.getLocation().getYaw());
        data.setPitch(player.getLocation().getPitch());
        data.setInventory(player.getInventory().getContents());
        data.setEnderChest(player.getEnderChest().getContents());
        data.setArmor(player.getInventory().getArmorContents());
        data.setOffHandItem(player.getInventory().getItemInOffHand());
        data.setSelectedSlot(player.getInventory().getHeldItemSlot());
        data.setHealth(player.getHealth());
        data.setFoodLevel(player.getFoodLevel());
        data.setSaturation(player.getSaturation());
        data.setExhaustion(player.getExhaustion());
        data.setLevel(player.getLevel());
        data.setExp(player.getExp());
        data.setTotalExperience(player.getTotalExperience());
        data.setGameMode(player.getGameMode());
        data.setAllowFlight(player.getAllowFlight());
        data.setFlying(player.isFlying());
        data.setFlySpeed(player.getFlySpeed());
        data.setWalkSpeed(player.getWalkSpeed());
        data.setPotionEffects(player.getActivePotionEffects());

        return data;
    }

    private void restorePlayerData(Player player, PlayerData data) {
        player.teleport(data.getLocation());
        player.getInventory().setContents(data.getInventory());
        player.getEnderChest().setContents(data.getEnderChest());
        player.getInventory().setArmorContents(data.getArmor());
        player.getInventory().setItemInOffHand(data.getOffHandItem());
        player.getInventory().setHeldItemSlot(data.getSelectedSlot());
        player.setHealth(data.getHealth());
        player.setFoodLevel(data.getFoodLevel());
        player.setSaturation(data.getSaturation());
        player.setExhaustion(data.getExhaustion());
        player.setLevel(data.getLevel());
        player.setExp(data.getExp());
        player.setTotalExperience(data.getTotalExperience());
        player.setGameMode(data.getGameMode());
        player.setAllowFlight(data.isAllowFlight());
        player.setFlying(data.isFlying());
        player.setFlySpeed(data.getFlySpeed());
        player.setWalkSpeed(data.getWalkSpeed());

        // Clear existing potion effects and apply saved ones
        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
        if (data.getPotionEffects() != null) {
            data.getPotionEffects().forEach(player::addPotionEffect);
        }
    }

    private Map<String, SavedBlockData> captureBlockData(World world, Location center) {
        Map<String, SavedBlockData> blockData = new HashMap<>();

        int centerX = center.getBlockX();
        int centerZ = center.getBlockZ();
        int minY = world.getMinHeight();
        int maxY = world.getMaxHeight();

        int blocksProcessed = 0;
        int totalBlocks = (SAVE_RADIUS * 2 + 1) * (SAVE_RADIUS * 2 + 1) * (maxY - minY);

        for (int x = centerX - SAVE_RADIUS; x <= centerX + SAVE_RADIUS; x++) {
            for (int z = centerZ - SAVE_RADIUS; z <= centerZ + SAVE_RADIUS; z++) {
                for (int y = minY; y < maxY; y++) {
                    Block block = world.getBlockAt(x, y, z);
                    if (block.getType() != Material.AIR) {
                        SavedBlockData data = new SavedBlockData(x, y, z, block.getType(), block.getBlockData().getAsString());
                        blockData.put(data.getLocationKey(), data);
                    }

                    blocksProcessed++;
                    // Log progress every 10000 blocks to show we're not frozen
                    if (blocksProcessed % 10000 == 0) {
                        plugin.getLogger().info("Block capture progress: " + blocksProcessed + "/" + totalBlocks + " (" + (blocksProcessed * 100 / totalBlocks) + "%)");
                    }
                }
            }
        }

        return blockData;
    }

    private void restoreBlockData(World world, Map<String, SavedBlockData> blockData) {
        int blocksRestored = 0;
        int totalBlocks = blockData.size();

        for (SavedBlockData data : blockData.values()) {
            Block block = world.getBlockAt(data.getX(), data.getY(), data.getZ());
            block.setType(data.getMaterial());
            if (data.getBlockDataString() != null) {
                try {
                    org.bukkit.block.data.BlockData bukkitBlockData = Bukkit.createBlockData(data.getBlockDataString());
                    block.setBlockData(bukkitBlockData);
                } catch (Exception e) {
                    plugin.getLogger().warning("Failed to restore block data at " + data.getLocationKey() + ": " + e.getMessage());
                }
            }

            blocksRestored++;
            // Log progress every 1000 blocks
            if (blocksRestored % 1000 == 0) {
                plugin.getLogger().info("Block restore progress: " + blocksRestored + "/" + totalBlocks + " (" + (blocksRestored * 100 / totalBlocks) + "%)");
            }
        }
    }

    private List<EntityData> captureEntityData(World world, Location center) {
        List<EntityData> entityData = new ArrayList<>();

        int centerX = center.getBlockX();
        int centerZ = center.getBlockZ();

        // Get all entities in the world and filter by distance
        Collection<Entity> worldEntities = world.getEntities();
        for (Entity entity : worldEntities) {
            if (entity instanceof Player) continue; // Players are handled separately

            Location entityLoc = entity.getLocation();
            if (Math.abs(entityLoc.getBlockX() - centerX) <= SAVE_RADIUS &&
                    Math.abs(entityLoc.getBlockZ() - centerZ) <= SAVE_RADIUS) {

                EntityData data = new EntityData(entity.getType(), entityLoc);
                data.setYaw(entityLoc.getYaw());
                data.setPitch(entityLoc.getPitch());

                if (entity instanceof LivingEntity) {
                    data.setHealth(((LivingEntity) entity).getHealth());
                }

                entityData.add(data);
            }
        }

        return entityData;
    }

    private void clearEntitiesInArea(World world, Location center) {
        int centerX = center.getBlockX();
        int centerZ = center.getBlockZ();

        List<Entity> toRemove = new ArrayList<>();
        Collection<Entity> worldEntities = world.getEntities();
        for (Entity entity : worldEntities) {
            if (entity instanceof Player) continue;

            Location entityLoc = entity.getLocation();
            if (Math.abs(entityLoc.getBlockX() - centerX) <= SAVE_RADIUS &&
                    Math.abs(entityLoc.getBlockZ() - centerZ) <= SAVE_RADIUS) {
                toRemove.add(entity);
            }
        }

        toRemove.forEach(Entity::remove);
    }

    private void restoreEntityData(World world, List<EntityData> entityData) {
        if (entityData == null) return;

        for (EntityData data : entityData) {
            try {
                Entity entity = world.spawnEntity(data.getLocation(), data.getEntityType());
                if (entity instanceof LivingEntity && data.getHealth() > 0) {
                    ((LivingEntity) entity).setHealth(data.getHealth());
                }
            } catch (Exception e) {
                plugin.getLogger().warning("Failed to restore entity " + data.getEntityType() + ": " + e.getMessage());
            }
        }
    }

    private void saveSaveStateToFile(SaveState saveState) {
        File file = new File(saveStatesFolder, saveState.getName() + ".json");
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(saveState, writer);
            plugin.getLogger().info("Successfully saved save state to file: " + file.getName());
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save state " + saveState.getName() + ": " + e.getMessage());
        }
    }

    private void loadAllSaveStates() {
        File[] files = saveStatesFolder.listFiles((dir, name) -> name.endsWith(".json"));
        if (files == null) return;

        for (File file : files) {
            try (FileReader reader = new FileReader(file)) {
                SaveState saveState = gson.fromJson(reader, SaveState.class);
                if (saveState != null) {
                    loadedSaveStates.put(saveState.getName(), saveState);
                    plugin.getLogger().info("Loaded save state: " + saveState.getName());
                }
            } catch (IOException e) {
                plugin.getLogger().warning("Failed to load save state from " + file.getName() + ": " + e.getMessage());
            }
        }

        plugin.getLogger().info("Loaded " + loadedSaveStates.size() + " save states");
    }

    public boolean deleteSaveState(String name) {
        if (!loadedSaveStates.containsKey(name)) {
            return false;
        }

        File file = new File(saveStatesFolder, name + ".json");
        if (file.exists()) {
            file.delete();
        }

        loadedSaveStates.remove(name);
        return true;
    }

    public Set<String> getSaveStateNames() {
        return new HashSet<>(loadedSaveStates.keySet());
    }

    public SaveState getSaveState(String name) {
        return loadedSaveStates.get(name);
    }

    public int getSaveStateCount() {
        return loadedSaveStates.size();
    }
}