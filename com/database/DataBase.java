/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.database;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import com.wanted.Configuration;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.bukkit.entity.Player;

/**
 *
 * @author sovadi
 */
public class DataBase {
    private String driver;
    private String dbUrl;
    private String host;
    private int port;
    private String username;
    private String password;
    
    private String database;
    private String tableName;
    private String columnId;
    private String columnName;
    private String columnRank;
    private String columnWanted;
    private String columnKills;
    private String columnDeaths;
    
    private Connection con;
    
    public DataBase(Configuration conf) throws ClassNotFoundException, SQLException {
        this.driver = "com.mysql.jdbc.Driver";  
        this.dbUrl = "jdbc:mysql://localhost/";
        this.host = conf.getMySQLHost;
        this.port = conf.getMySQLPort;
        this.username = conf.getMySQLUsername;
        this.password = conf.getMySQLPassword;
       
        
        this.database = conf.getMySQLDatabase;
        this.tableName = conf.getMySQLTablename;
        this.columnId = "id";
        this.columnName = "name";
        this.columnRank = "rank";
        this.columnWanted = "wanted";
        this.columnKills = "kills";
        this.columnDeaths = "deaths";
        
        this.con = null;
        
        this.setup();
    }
    
    private void openConnection() throws ClassNotFoundException, SQLException {
        try{
           //STEP 2: Register JDBC driver
           Class.forName("com.mysql.jdbc.Driver");

           //STEP 3: Open a connection
           System.out.println("Connecting to database...");
           this.con = DriverManager.getConnection(this.dbUrl + this.database, this.username, this.password);

        } catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        } catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }
    }
    
    private void closeConnection() {
        try{
             if(this.con!=null)
                this.con.close();
        }catch(SQLException se) {
             se.printStackTrace();
        }
        System.out.println("Goodbye!");
    }


    private void setup() throws SQLException {
        Statement stmt = null;    
        try{
                this.openConnection();
                stmt = con.createStatement();

                String sql = "CREATE TABLE IF NOT EXISTS " + this.tableName + " " +
                             "(" + this.columnId + " INTEGER AUTO_INCREMENT NOT NULL, " +
                             " " + this.columnName + " VARCHAR(255) NOT NULL, " + 
                             " " + this.columnRank + " INTEGER NOT NULL, " + 
                             " " + this.columnWanted + " INTEGER NOT NULL, " +
                             " " + this.columnKills + " INTEGER NOT NULL, " +
                             " " + this.columnDeaths + " INTEGER NOT NULL, " +
                             " PRIMARY KEY ( " + this.columnId + " ))"; 

                stmt.executeUpdate(sql);
                System.out.println("Created table in given database...");
        }catch(SQLException se){
                //Handle errors for JDBC
                se.printStackTrace();
        }catch(Exception e){
                //Handle errors for Class.forName
                e.printStackTrace();
        }finally{
                //finally block used to close resources
                try{
                   if(stmt!=null)
                      stmt.close();
                }catch(SQLException se){
                }// do nothing

                this.closeConnection();
        }
    }
    
    public void addNewPlayer(Player player) {
        System.out.println("Player: " + player.getName() + " joined the game 1st time");
        PreparedStatement pst = null;
        try{
                this.openConnection();
                
                pst = con.prepareStatement("INSERT INTO " + tableName + "(" + columnName + "," + this.columnRank + "," + this.columnWanted + "," + this.columnKills  + "," + this.columnDeaths + ") VALUES (?,?,?,?,?);");
                pst.setString(1, player.getName());
                pst.setInt(2, 1);
                pst.setInt(3, 10);
                pst.setInt(4, 0);
                pst.setInt(5, 0);
                pst.executeUpdate();
                
                System.out.println("Added player " + player.getName() + " in table");
        }catch(SQLException se){
                se.printStackTrace();
        }catch(Exception e){
                e.printStackTrace();
        }finally{
                try{
                   if(pst!=null)
                      pst.close();
                }catch(SQLException se){
                }
                this.closeConnection();
        }
    }
    public String getPlayerData(Player player) {
        PreparedStatement pst = null;
        String data = "none";
        try {
            this.openConnection();
            
            String sql = "SELECT rank, wanted, kills, deaths FROM " + this.tableName + " WHERE name = ?;";
            
            pst = con.prepareStatement(sql);
            pst.setString(1, player.getName());
            
            ResultSet rs = pst.executeQuery();
            //STEP 5: Extract data from result set
            while(rs.next()){
               //Retrieve by column name
               int rank = rs.getInt("rank");
               int wanted = rs.getInt("wanted");
               int kills = rs.getInt("kills");
               int deaths = rs.getInt("deaths");
               
               data = rank + "," + wanted + "," + kills + "," + deaths;
            }
            rs.close();
        }catch(SQLException se){
                se.printStackTrace();
        }catch(Exception e){
                e.printStackTrace();
        }finally{
                try{
                   if(pst!=null)
                      pst.close();
                }catch(SQLException se){
                }
                this.closeConnection();
                return data;
        }
    }
    public int getPlayerRank(Player player) {
        String data = this.getPlayerData(player);
        return Integer.parseInt(data.split(",")[0]);
    }
    public int getPlayerWanted(Player player) {
        String data = this.getPlayerData(player);
        return Integer.parseInt(data.split(",")[1]);
    }
    public int getPlayerKills(Player player) {
        String data = this.getPlayerData(player);
        return Integer.parseInt(data.split(",")[2]);
    }
    public int getPlayerDeaths(Player player) {
        String data = this.getPlayerData(player);
        return Integer.parseInt(data.split(",")[3]);
    }
    public String[] getTopPlayers(Player player, int number) {
        Statement stmt = null;
        String[] players = new String[number];
        int i = 0;
        try{
            this.openConnection();
            stmt = con.createStatement();
            
            String sql = "SELECT name, wanted FROM " + this.tableName + " ORDER BY wanted DESC LIMIT " + number;
            ResultSet rs = stmt.executeQuery(sql);
            //STEP 5: Extract data from result set
            i = 0; 
            while(rs.next()){
               //Retrieve by column name
               String name = rs.getString("name");
               int wanted  = rs.getInt("wanted");
               
               players[i] = name + ", " + wanted;
               i++;
            }
            rs.close();
            System.out.println("Player " + player.getName() + " saw top players");
        }catch(SQLException se){
                se.printStackTrace();
        }catch(Exception e){
                e.printStackTrace();
        }finally{
                try{
                   if(stmt!=null)
                      stmt.close();
                }catch(SQLException se){
                }
                this.closeConnection();
                return players;
        }
    }
    public void updateRank(Player player, int newRank) {
        PreparedStatement pst = null;
        try{
            this.openConnection();
            String sql = "UPDATE " + this.tableName + " SET rank = " + newRank + " WHERE name = ?;";
            pst = con.prepareStatement(sql);
            pst.setString(1, player.getName());
            
            pst.executeUpdate();
        }catch(SQLException se){
                se.printStackTrace();
        }catch(Exception e){
                e.printStackTrace();
        }finally{
                try{
                   if(pst!=null)
                      pst.close();
                }catch(SQLException se){
                }
                this.closeConnection();
        }
        
    }
    public void updateWanted(Player player, int newWanted) {
        PreparedStatement pst = null;
        try{
            this.openConnection();
            String sql = "UPDATE " + this.tableName + " SET wanted = " + newWanted + " WHERE name = ?;";
            pst = con.prepareStatement(sql);
            pst.setString(1, player.getName());
            
            pst.executeUpdate();
        }catch(SQLException se){
                se.printStackTrace();
        }catch(Exception e){
                e.printStackTrace();
        }finally{
                try{
                   if(pst!=null)
                      pst.close();
                }catch(SQLException se){
                }
                this.closeConnection();
        }
    }
    public void updateKills(Player player, int newKills) {
        PreparedStatement pst = null;
        try{
            this.openConnection();
            String sql = "UPDATE " + this.tableName + " SET kills = " + newKills + " WHERE name = ?;";
            pst = con.prepareStatement(sql);
            pst.setString(1, player.getName());
            
            pst.executeUpdate();
        }catch(SQLException se){
                se.printStackTrace();
        }catch(Exception e){
                e.printStackTrace();
        }finally{
                try{
                   if(pst!=null)
                      pst.close();
                }catch(SQLException se){
                }
                this.closeConnection();
        }
    }
    public void updateDeaths(Player player, int newDeaths) {
        PreparedStatement pst = null;
        try{
            this.openConnection();
            String sql = "UPDATE " + this.tableName + " SET deaths = " + newDeaths + " WHERE name = ?;";
            pst = con.prepareStatement(sql);
            pst.setString(1, player.getName());
            
            pst.executeUpdate();
        }catch(SQLException se){
                se.printStackTrace();
        }catch(Exception e){
                e.printStackTrace();
        }finally{
                try{
                   if(pst!=null)
                      pst.close();
                }catch(SQLException se){
                }
                this.closeConnection();
        }
    }

    public boolean playerNotExists(Player player) {
        boolean exists = true;
        PreparedStatement pst = null;
       try {
            this.openConnection();
            
            String sql = "SELECT id FROM " + this.tableName + " WHERE name = ?;";
            
            pst = con.prepareStatement(sql);
            pst.setString(1, player.getName());
            
            ResultSet rs = pst.executeQuery();
            //STEP 5: Extract data from result set
            if (rs.next()){
               exists = false;
            }
            else exists = true;
            rs.close();
        }catch(SQLException se){
                se.printStackTrace();
        }catch(Exception e){
                e.printStackTrace();
        }finally{
                try{
                   if(pst!=null)
                      pst.close();
                }catch(SQLException se){
                }
                this.closeConnection();
        }
        
        return exists;
    }
}
