package com.savestate.commands;

import com.savestate.data.SaveState;
import com.savestate.managers.SaveStateManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

public class ListSaveStatesCommand implements CommandExecutor {
    private final SaveStateManager saveStateManager;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public ListSaveStatesCommand(SaveStateManager saveStateManager) {
        this.saveStateManager = saveStateManager;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Set<String> saveStateNames = saveStateManager.getSaveStateNames();
        
        if (saveStateNames.isEmpty()) {
            sender.sendMessage(ChatColor.YELLOW + "No save states found.");
            sender.sendMessage(ChatColor.GRAY + "Use /createsavestate <name> to create one.");
            return true;
        }
        
        sender.sendMessage(ChatColor.GREEN + "=== Available Save States ===");
        sender.sendMessage(ChatColor.GRAY + "Found " + saveStateNames.size() + " save state(s):");
        
        for (String name : saveStateNames) {
            SaveState saveState = saveStateManager.getSaveState(name);
            if (saveState != null) {
                String timestamp = dateFormat.format(new Date(saveState.getTimestamp()));
                int playerCount = saveState.getPlayerData().size();
                int blockCount = saveState.getBlockData().size();
                int entityCount = saveState.getEntityData() != null ? saveState.getEntityData().size() : 0;
                
                sender.sendMessage(ChatColor.AQUA + "• " + name + ChatColor.GRAY + " (Created: " + timestamp + ")");
                sender.sendMessage(ChatColor.GRAY + "  World: " + saveState.getWorldName() + 
                                 " | Players: " + playerCount + 
                                 " | Blocks: " + blockCount + 
                                 " | Entities: " + entityCount);
            } else {
                sender.sendMessage(ChatColor.AQUA + "• " + name + ChatColor.RED + " (Error loading details)");
            }
        }
        
        sender.sendMessage(ChatColor.GREEN + "===========================");
        sender.sendMessage(ChatColor.GRAY + "Use /loadsave <name> to load a save state");
        sender.sendMessage(ChatColor.GRAY + "Use /deletesavestate <name> to delete a save state");
        
        return true;
    }
}