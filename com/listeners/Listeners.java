/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.listeners;

import com.database.DataBase;
import com.wanted.CalculateWantedChange;
import com.wanted.TheWanted;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 *
 * @author sovadi
 */
public class Listeners  implements Listener {
    private DataBase database;
    private CalculateWantedChange calcWanted;
    
    public Listeners(DataBase database) {
        this.database = database;
        this.calcWanted = new CalculateWantedChange(database);
        
        Bukkit.getServer().getPluginManager().registerEvents(this, TheWanted.getInstance());
    }
    
    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
	Player player = event.getPlayer();
        
        if (database.playerNotExists(player)) database.addNewPlayer(player);
    }
	
    @EventHandler(priority = EventPriority.NORMAL)
    public void onKill(PlayerDeathEvent death) {
                if(death.getEntity().getKiller() instanceof Player) {
                    Player killer = death.getEntity().getKiller();
                    Player victim = death.getEntity().getPlayer();
                    System.out.println(victim.getName());
                    
                    if(killer.hasPermission("wanted.use")) {
                        this.database.updateKills(killer, this.database.getPlayerKills(killer)+1);
                        this.database.updateDeaths(victim, this.database.getPlayerDeaths(victim)+1);
                        
                        this.calcWanted.changeWantedFor(killer, victim);
                    }
                }
    }
}
