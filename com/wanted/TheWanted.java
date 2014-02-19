/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wanted;

import com.commands.Commands;
import com.database.DataBase;
import com.listeners.Listeners;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author sovadi
 */
public class TheWanted  extends JavaPlugin {

    public static Logger log = Logger.getLogger("Minecraft");
    public static PluginManager pluginManager;
    public static PluginDescriptionFile pdfFile;
    public static Server server;
    public static DataBase database;
    public static Commands commands;
    public static Listeners listeners;
    public static Configuration conf;
    
    @Override
    public void onDisable() {
        log.info("TheWanted plugin has been disabled");
    }
    @Override
    public void onEnable() {
        pluginManager = this.getServer().getPluginManager();
        pdfFile = this.getDescription();
        server = this.getServer();
        try {
            conf = new Configuration(this);
            database = new DataBase(conf);
            commands = new Commands(database);
            listeners = new Listeners(database);
        } catch (ClassNotFoundException ex) {
            log.info("ClassNotFoundException");
        } catch (SQLException ex) {
            log.info("SQLException");
        }
        
        log.info("TheWanted plugin has been enabled");
    }
    
    public static TheWanted getInstance() {
	return (TheWanted) Bukkit.getServer().getPluginManager().getPlugin("TheWanted");
    }
}
