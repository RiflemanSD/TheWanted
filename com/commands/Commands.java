/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 *
 * @author sovadi
 */
public class Commands implements CommandExecutor {
    private DataBase database;
    
    public Commands(DataBase database) {
        this.database = database;
        
        Bukkit.getPluginCommand("wanted").setExecutor(this);
	Bukkit.getPluginCommand("topwanted").setExecutor(this);
    }
    
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
