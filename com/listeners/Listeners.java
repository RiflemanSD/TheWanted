/*
 * Open source License
 *
 * Project name: TheWanted
 * Main class: com.wanted.TheWanted

 * © Copyright - Sotiris Doudis 
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
 * This class creating the listeners for the plugin,
 * a listener is a event handler for the game events
 * In this plugin we have 2 listeners,
 * 1 in a player login at server
 * 2 when a player die event
 * 
 * @author Sotiris Doudis 
 */
public class Listeners  implements Listener {
    private DataBase database;
    private CalculateWantedChange calcWanted;
    
    /**
     * Constractor for the class, here we geting the main database variable for
     * the main class so we can get or change database datas
     * Also here we register the listeners to the server, so he can listen them
     * 
     * @param database
     */
    public Listeners(DataBase database) {
        this.database = database;
        this.calcWanted = new CalculateWantedChange(database);
        
        Bukkit.getServer().getPluginManager().registerEvents(this, TheWanted.getInstance());
    }
    
    /**
     * on player login event, when a player login we added him in the database
     * 
     * @param event - PlayerLoginEvent
     */
    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
	Player player = event.getPlayer();
        
        if (database.playerNotExists(player)) database.addNewPlayer(player);
    }
	
    /**
     * When a player die, server call this method, so we can get the killer and
     * the victim and change his statics
     * 
     * @param death - PlayerDeathEvent
     */
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
