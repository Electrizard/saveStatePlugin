package com.savestate.commands;

import com.savestate.managers.SaveStateManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LoadSaveCommand implements CommandExecutor {
    private final SaveStateManager saveStateManager;
    
    public LoadSaveCommand(SaveStateManager saveStateManager) {
        this.saveStateManager = saveStateManager;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be executed by players!");
            return true;
        }
        
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /loadsave <name>");
            return true;
        }
        
        Player player = (Player) sender;
        String saveStateName = args[0];
        
        // Check if save state exists
        if (saveStateManager.getSaveState(saveStateName) == null) {
            player.sendMessage(ChatColor.RED + "Save state '" + saveStateName + "' does not exist!");
            player.sendMessage(ChatColor.GRAY + "Use /listsavestates to see available save states.");
            return true;
        }
        
        player.sendMessage(ChatColor.YELLOW + "Loading save state '" + saveStateName + "'...");
        player.sendMessage(ChatColor.YELLOW + "This may take a moment...");
        
        // Run the load operation on main thread but warn other players
        for (Player onlinePlayer : player.getWorld().getPlayers()) {
            if (!onlinePlayer.equals(player)) {
                onlinePlayer.sendMessage(ChatColor.YELLOW + player.getName() + " is loading save state '" + saveStateName + "'");
            }
        }
        
        boolean success = saveStateManager.loadSaveState(saveStateName, player);
        
        if (success) {
            for (Player onlinePlayer : player.getWorld().getPlayers()) {
                onlinePlayer.sendMessage(ChatColor.GREEN + "Save state '" + saveStateName + "' loaded successfully!");
            }
        } else {
            player.sendMessage(ChatColor.RED + "Failed to load save state! The world may not exist or the save state may be corrupted.");
        }
        
        return true;
    }
}