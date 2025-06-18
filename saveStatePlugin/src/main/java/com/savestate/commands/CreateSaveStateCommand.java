package com.savestate.commands;

import com.savestate.managers.SaveStateManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateSaveStateCommand implements CommandExecutor {
    private final SaveStateManager saveStateManager;

    public CreateSaveStateCommand(SaveStateManager saveStateManager) {
        this.saveStateManager = saveStateManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be executed by players!");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /createsavestate <name>");
            return true;
        }

        Player player = (Player) sender;
        String saveStateName = args[0];

        // Check if save state name is valid
        if (saveStateName.isEmpty() || saveStateName.contains(" ") || saveStateName.contains(".") || saveStateName.contains("/")) {
            player.sendMessage(ChatColor.RED + "Invalid save state name! Please use alphanumeric characters and underscores only.");
            return true;
        }

        // Check if we've reached the limit (optional limit of 10 save states)
        if (saveStateManager.getSaveStateCount() >= 10) {
            player.sendMessage(ChatColor.RED + "Maximum number of save states reached (10). Please delete some before creating new ones.");
            return true;
        }

        player.sendMessage(ChatColor.YELLOW + "Creating save state '" + saveStateName + "'...");
        player.sendMessage(ChatColor.YELLOW + "This may take a moment as we save blocks in a 200x200 area...");

        // Run the save state creation on the main thread to avoid async issues
        // We'll use a delayed task to prevent blocking the command execution
        saveStateManager.getPlugin().getServer().getScheduler().runTask(
                saveStateManager.getPlugin(),
                () -> {
                    boolean success = saveStateManager.createSaveState(saveStateName, player);

                    if (success) {
                        player.sendMessage(ChatColor.GREEN + "Save state '" + saveStateName + "' created successfully!");
                        player.sendMessage(ChatColor.GRAY + "Saved area: 200x200 blocks around your position");
                        player.sendMessage(ChatColor.GRAY + "Players saved: " + player.getWorld().getPlayers().size());
                    } else {
                        player.sendMessage(ChatColor.RED + "Failed to create save state! A save state with that name already exists.");
                    }
                }
        );

        return true;
    }
}