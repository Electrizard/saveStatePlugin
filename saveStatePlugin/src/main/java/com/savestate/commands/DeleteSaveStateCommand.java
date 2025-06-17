package com.savestate.commands;

import com.savestate.managers.SaveStateManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DeleteSaveStateCommand implements CommandExecutor {
    private final SaveStateManager saveStateManager;
    
    public DeleteSaveStateCommand(SaveStateManager saveStateManager) {
        this.saveStateManager = saveStateManager;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /deletesavestate <name>");
            return true;
        }
        
        String saveStateName = args[0];
        
        // Check if save state exists
        if (saveStateManager.getSaveState(saveStateName) == null) {
            sender.sendMessage(ChatColor.RED + "Save state '" + saveStateName + "' does not exist!");
            sender.sendMessage(ChatColor.GRAY + "Use /listsavestates to see available save states.");
            return true;
        }
        
        boolean success = saveStateManager.deleteSaveState(saveStateName);
        
        if (success) {
            sender.sendMessage(ChatColor.GREEN + "Save state '" + saveStateName + "' deleted successfully!");
        } else {
            sender.sendMessage(ChatColor.RED + "Failed to delete save state '" + saveStateName + "'!");
        }
        
        return true;
    }
}