/*
 * Open source License
 *
 * Project name: TheWanted
 * Main class: com.wanted.TheWanted

 * © Copyright - Sotiris Doudis 
*/

package com.wanted;

import com.database.DataBase;
import org.bukkit.entity.Player;

/**
 * This class calculating the changes at the players wanted and rank, when a 
 * player killing another player
 * 
 * @author Sotiris Doudis 
 */
public class CalculateWantedChange {
    private DataBase database;
    private double dif;
    
    /**
     * Constractor of the class
     * We geting the databse so we can get and change the player stats
     * 
     * @param database
     */
    public CalculateWantedChange(DataBase database) {
        this.database = database;
        dif = 10.0;
    }
    
    /**
     * In this method we get the killer and the victim, and we calculating the
     * diffrenect between them, so we can change his data
     * 
     * @param killer - player who kill victim
     * @param victim - player who killed by killer
     */
    public void changeWantedFor(Player killer, Player victim) {
        int kRank = this.database.getPlayerRank(killer);
        int kWanted = this.database.getPlayerWanted(killer);
        
        int vRank = this.database.getPlayerRank(victim);
        int vWanted = this.database.getPlayerWanted(victim);
        
        double diffrent = this.calcDiffrent(kRank, vRank);
        
        this.database.updateRank(killer, (int)(kRank + diffrent));
        if ( vRank > 10 ) this.database.updateRank(victim, (int)(vRank - diffrent/2));
        
        this.database.updateWanted(killer, (int)(kWanted + 10*diffrent));
        if ( vRank > 10 ) this.database.updateWanted(victim, (int)(vWanted - (10*diffrent)/2));
    }
    
    private double calcDiffrent(int r1, int r2) {
        this.dif = calcDif(r1);
        
        if (r1 > r2) {
            return (r2/this.dif)/2.0;
        }
        else if (r1 < r2) {
            return (r2/this.dif)*2.0;
        }
        else {
            return r2/this.dif;
        }
    }
    
    private double calcDif(int r1) {
        if (r1 > 0 && r1 < 10) {
            return 1.0;
        }
        else if (r1 < 50) {
            return 2.0;
        }
        else if (r1 < 100) {
            return 5.0;
        }
        else if (r1 < 500) {
            return 7.0;
        }
        else if (r1 < 1000) {
            return 10.0;
        }
        else if (r1 < 10000) {
            return 15.0;
        }
        else {
            return 30.0;
        }
    }
}
