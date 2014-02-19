/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wanted;

import java.io.File;
import java.io.InputStream;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author sovadi
 */
public class Configuration  extends YamlConfiguration {
    
    public String getMySQLHost;
    public int getMySQLPort;
    public String getMySQLUsername;
    public String getMySQLPassword;
    public String getMySQLDatabase;
    public String getMySQLTablename;
    
    private final File file;
    private TheWanted plugin;
    
    protected static YamlConfiguration configFile;
    
    public Configuration(TheWanted plugin) {
        this.file = new File(plugin.getDataFolder(),"config.yml");
        this.plugin = plugin;
        
        configFile = (YamlConfiguration) plugin.getConfig();
        
        this.setData();
        this.getData();
    }
    
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
