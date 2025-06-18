package com.savestate;

import com.savestate.commands.CreateSaveStateCommand;
import com.savestate.commands.LoadSaveCommand;
import com.savestate.commands.ListSaveStatesCommand;
import com.savestate.commands.DeleteSaveStateCommand;
import com.savestate.managers.SaveStateManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class SaveStatePlugin extends JavaPlugin {
    
    private SaveStateManager saveStateManager;
    
    @Override
    public void onEnable() {
        // Initialize the save state manager (this will load existing save states from disk)
        saveStateManager = new SaveStateManager(this);
        
        // Register commands
        getCommand("createsavestate").setExecutor(new CreateSaveStateCommand(saveStateManager));
        getCommand("loadsave").setExecutor(new LoadSaveCommand(saveStateManager));
        getCommand("listsavestates").setExecutor(new ListSaveStatesCommand(saveStateManager));
        getCommand("deletesavestate").setExecutor(new DeleteSaveStateCommand(saveStateManager));
        
        // Send startup message to all online players
        Bukkit.getScheduler().runTaskLater(this, () -> {
            String message = ChatColor.BLUE + "Save State Plugin by Electrizard";
            Bukkit.broadcastMessage(message);
        }, 20L); // Delay by 1 second (20 ticks) to ensure players are fully loaded
        
        getLogger().info("SaveStatePlugin has been enabled!");
        
        // Log how many save states were loaded
        if (saveStateManager.getSaveStateCount() > 0) {
            getLogger().info("Ready to use with " + saveStateManager.getSaveStateCount() + " existing save states!");
        }
    }
    
    @Override
    public void onDisable() {
        // Save all current save states to disk before shutting down
        if (saveStateManager != null) {
            saveStateManager.saveAllSaveStatesToDisk();
        }
        
        getLogger().info("SaveStatePlugin has been disabled!");
    }
    
    public SaveStateManager getSaveStateManager() {
        return saveStateManager;
    }
}