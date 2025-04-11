package model.strategy.player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import model.Country;
import model.GameMap;
import model.Player;
import utils.logger.LogEntryBuffer;

/**
 * Cheater Strategy class - implements cheater strategy
 *
 * @author Poorav Panchal
 * @version 1.0.0
 */
public class CheaterStrategy extends PlayerStrategy implements Serializable {

    /**
     * Logger Observable
     */
    private LogEntryBuffer d_Logger = LogEntryBuffer.getInstance();
    
    public String createCommand() {
        d_Player = GameMap.getInstance().getD_CurrentPlayer();
        d_Logger.log("Issuing Orders for the Cheater Player - " + d_Player.getD_Name());
        Player l_NeighborOwner;
        List<Country> l_Enemies = new ArrayList<>();
        // Capture neighbor countries
        for (Country l_Country : d_Player.getCapturedCountries()) {
            for (Country l_Neighbor : l_Country.getD_CountryNeighbors()) {
                if (l_Neighbor.getPlayer() != d_Player) {
                    l_NeighborOwner = l_Neighbor.getPlayer();
                    l_NeighborOwner.getCapturedCountries().remove(l_Neighbor);
                    l_Enemies.add(l_Neighbor);
                    l_Neighbor.setPlayer(d_Player);
                    d_Logger.log("Conquered the neighbor country of enemy - " + l_Neighbor.getD_CountryName());
                }
            }
        }
        d_Player.getCapturedCountries().addAll(l_Enemies);

        // Double the army of a country if it has an enemy
        for (Country l_Country : d_Player.getCapturedCountries()) {
            for (Country l_Neighbor : l_Country.getD_CountryNeighbors()) {
                if (l_Neighbor.getPlayer() != d_Player) {
                    l_Country.setD_Armies(l_Country.getD_Armies() * 2);
                    d_Logger.log("Armies doubled in Cheater Player's country" + l_Country.getD_CountryName());
                }
            }
        }
        return "pass";
    }
}
