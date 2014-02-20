/*
 * Open source License
 *
 * Project name: TheWanted
 * Main class: com.wanted.TheWanted

 * © Copyright - Sotiris Doudis
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
 * This class, is the main class of this project
 * Project name: TheWanted
 * 
 * This project is a plugin for the bukkit minecraft servers
 * More info here: http://dev.bukkit.org/bukkit-plugins/thewanted/
 * 
 * This plugin add a table with all players in the MySQL database of the server.
 * In this table players have some statics, about kills and deaths,
 * also players get a rank and a Wanted price for the player who can kill them.
 * When a player killing and other he getting more rank depends on the killed
 * player rank!
 * Ur gold is to get to the top wanted players.
 * 
 * The project is open source
 * 
 * @author Sotiris Doudis
 */
public class TheWanted  extends JavaPlugin {

    /**
     * log, a variable for the Logger of the server
     */
    public static Logger log = Logger.getLogger("Minecraft");

    /**
     * pluginManager, variable for the plugin manager of the server
     */
    public static PluginManager pluginManager;

    /**
     *
     */
    public static PluginDescriptionFile pdfFile;

    /**
     * server, variable for the game server
     */
    public static Server server;

    /**
     * database, create a DataBase object, to control database, 
     * at package com.database
     */
    public static DataBase database;

    /**
     * commands, create a Commands object, to control the plugin commands, 
     * at package com.Commands
     */
    public static Commands commands;

    /**
     * listeners, create a Listeners object, to control the plugin listeners, 
     * at package com.Listeners
     */
    public static Listeners listeners;

    /**
     * conf, create a Configuration object, to control the plugin config.yml file, 
     * at package com.wanted
     */
    public static Configuration conf;
    
    /**
     * When plugin becomes disable just write a message and the logger
     */
    @Override
    public void onDisable() {
        log.info("TheWanted plugin has been disabled");
    }

    /**
     * When plugin becomes enable, initialize the variables and the objects of other classes.
     * and write a message to the logger
     */
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
    
    /**
     *
     * @return a instance of this class, so Commands class can use to register the commands
     */
    public static TheWanted getInstance() {
	return (TheWanted) Bukkit.getServer().getPluginManager().getPlugin("TheWanted");
    }
}
