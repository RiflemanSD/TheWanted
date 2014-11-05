/*
 * Open source License
 *
 * Project name: TheWanted
 * Main class: com.wanted.TheWanted

 * © Copyright - Sotiris Doudis 
*/

package com.wanted;

import java.io.File;
import java.io.InputStream;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * This class is class so we can create/delete/add datas from config.yml
 * 
 * @author Sotiris Doudis 
 */
public class Configuration  extends YamlConfiguration {
    
    /**
     * getMySQLHost, the server host, defined in config.yml
     */
    public String getMySQLHost;

    /**
     * getMySQLPort, MySQL port, defined in config.yml
     */
    public int getMySQLPort;

    /**
     * getMySQLUsername, MySQL username, defined in config.yml
     */
    public String getMySQLUsername;

    /**
     * getMySQLPassword, MySQL password, defined in config.yml
     */
    public String getMySQLPassword;

    /**
     * getMySQLDatabase, MySQL database name, defined in config.yml
     */
    public String getMySQLDatabase;

    /**
     * getMySQLTablename, MySQL table name in database, defined in config.yml
     */
    public String getMySQLTablename;
    
    private final File file;
    private TheWanted plugin;
    
    /**
     * variable to get plugin config file, so we can create a new.
     */
    protected static YamlConfiguration configFile;
    
    /**
     * Constractor for this class,
     * here we initialize the class settings, and creating a default config file
     * @param plugin - main class of the project
     */
    public Configuration(TheWanted plugin) {
        this.file = new File(plugin.getDataFolder(),"config.yml");
        this.plugin = plugin;
        
        configFile = (YamlConfiguration) plugin.getConfig();
        
        this.setData();
        this.getData();
    }
    
    /**
     * With this method we seting the class variables from config values
     */
    public void getData() {
        configFile.options().copyDefaults(true);
        plugin.saveConfig();
        
        getMySQLHost = configFile.getString("DataSource.mySQLHost");
        getMySQLPort = configFile.getInt("DataSource.mySQLPort");
        getMySQLUsername = configFile.getString("DataSource.mySQLUsername");
        getMySQLPassword = configFile.getString("DataSource.mySQLPassword");
        getMySQLDatabase = configFile.getString("DataSource.mySQLDatabase");
        getMySQLTablename = configFile.getString("DataSource.mySQLTablename");
    }
    
    /**
     * With this method, we set default datas for config file (if not defined)
     */
    public void setData() {
        if (!configFile.contains("DataSource.mySQLHost")) 
            configFile.set("DataSource.mySQLHost", "127.0.0.1");
        if (!configFile.contains("DataSource.mySQLPort")) 
            configFile.set("DataSource.mySQLPort", 3306);
        if (!configFile.contains("DataSource.mySQLUsername")) 
            configFile.set("DataSource.mySQLUsername","user");
        if (!configFile.contains("DataSource.mySQLPassword")) 
            configFile.set("DataSource.mySQLPassword","12345");
        if (!configFile.contains("DataSource.mySQLDatabase")) 
            configFile.set("DataSource.mySQLDatabase","wanteddb");
        if (!configFile.contains("DataSource.mySQLTablename")) 
            configFile.set("DataSource.mySQLTablename","players");
    }
}
