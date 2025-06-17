package com.savestate;

import com.savestate.commands.CreateSaveStateCommand;
import com.savestate.commands.LoadSaveCommand;
import com.savestate.commands.ListSaveStatesCommand;
import com.savestate.commands.DeleteSaveStateCommand;
import com.savestate.managers.SaveStateManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SaveStatePlugin extends JavaPlugin {
    
    private SaveStateManager saveStateManager;
    
    @Override
    public void onEnable() {
        // Initialize the save state manager
        saveStateManager = new SaveStateManager(this);
        
        // Register commands
        getCommand("createsavestate").setExecutor(new CreateSaveStateCommand(saveStateManager));
        getCommand("loadsave").setExecutor(new LoadSaveCommand(saveStateManager));
        getCommand("listsavestates").setExecutor(new ListSaveStatesCommand(saveStateManager));
        getCommand("deletesavestate").setExecutor(new DeleteSaveStateCommand(saveStateManager));
        
        getLogger().info("SaveStatePlugin has been enabled!");
    }
    
    @Override
    public void onDisable() {
        getLogger().info("SaveStatePlugin has been disabled!");
    }
    
    public SaveStateManager getSaveStateManager() {
        return saveStateManager;
    }
}