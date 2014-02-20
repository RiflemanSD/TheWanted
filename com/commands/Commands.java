/*
 * Open source License
 *
 * Project name: TheWanted
 * Main class: com.wanted.TheWanted
 *
 * © Copyright - Sotiris Doudis
 */

package com.commands;

import com.database.DataBase;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * This class, creating the commands of the plugin.
 * A command in a minecraft server, is a way so players can contact with the
 * server. For example a command can teleport a player to another location or 
 * give him items etc.
 * 
 * In this plugin we have create 2 commands
 * Command: /wanted, this command showing the players data
 * Command: /topwanted, this command showing the top players to the player who
 * executed it
 * 
 * @author Sotiris doudis
 */
public class Commands implements CommandExecutor {
    private DataBase database;
    
    /**
     * Constractor of class
     * Here we telling to the server to set the executor so when a player execute
     * /wanted or /topwanted command, the server we call our methods
     * We need to get the database of the plugin so we can get and show in a 
     * player the database stats
     * 
     * @param database
     */
    public Commands(DataBase database) {
        this.database = database;
        
        Bukkit.getPluginCommand("wanted").setExecutor(this);
	Bukkit.getPluginCommand("topwanted").setExecutor(this);
    }
    
    /**
     * When a command execute will call this method.
     * This method getting the player who execute the command, the command and 
     * the arguments of the command.
     * 
     * In this method we checking what commands was executed and we running our
     * methods, if /wanted command was execute we running the wanted() method and
     * show at the player the data
     * 
     * @param sender - who execute the command
     * @param command - the command was executed
     * @param commandLabel
     * @param args - arguments of the command
     * @return true if the command could executed right
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        String commandName = command.getName().toLowerCase();
        if (sender instanceof Player) {
            Player player = (Player)sender;
            
            if (commandName.equals("wanted")) {
                if (args.length == 0) {
                    wanted(player);
                    return true;
                }
                else {
                    player.sendMessage("Too many parammetrs");
                    return false;
                }
            }
            else if (commandName.equals("topwanted")) {
                if (args.length == 0) {
                    topwanted(player);
                    return true;
                }
                else {
                    player.sendMessage("Too many parammetrs");
                    return false;
                }
            }
        }
        return false;
    }
    
    private void wanted(Player player) {
        player.sendMessage(this.database.getPlayerData(player));
    }

    private void topwanted(Player player) {
        player.sendMessage(this.database.getTopPlayers(player, 5));
    }
}
