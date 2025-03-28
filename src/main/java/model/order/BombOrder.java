package model.order;

import model.Card;
import model.CardType;
import model.Country;
import model.GameMap;
import model.Player;
import utils.logger.LogEntryBuffer;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a Bomb Order - destroys half of enemy armies in a target country.

 *   @author Pruthvirajsinh Dodiya
 */
public class BombOrder extends Order implements Serializable {

    /** Game Map singleton instance */
    public GameMap d_GameMap;

    /** Logger instance */
    public LogEntryBuffer d_Logger;

    /**
     * Constructor for BombOrder
     */
    public BombOrder() {
        super();
        setType("bomb");
        d_GameMap = GameMap.getInstance();
        d_Logger = LogEntryBuffer.getInstance();
    }

    /**
     * Executes the bomb order - halves the armies in an enemy country if valid.
     *
     * @return true if successful, false otherwise
     */
    @Override
    public boolean execute() {
        Player l_Player = getOrderInfo().getPlayer();
        Country l_Target = getOrderInfo().getTargetCountry();

        d_Logger.log(getOrderInfo().getCommand());

        if (!validateCommand()) {
            return false;
        }

        int l_OriginalArmies = l_Target.getD_Armies();
        int l_NewArmies = Math.max(0, l_OriginalArmies / 2);
        l_Target.setD_Armies(l_NewArmies);

 //todo: add removeCard in Player.java

        l_Player.removeCard(new Card(CardType.BOMB));

        d_Logger.log(String.format("Bomb executed on %s: Armies reduced from %d to %d.",
                l_Target.getD_CountryName(), l_OriginalArmies, l_NewArmies));
        d_Logger.log("---------------------------------------------------------------------------------------------");
        return true;
    }

    /**
     * Validates whether the bomb order is legal.
     *
     * @return true if valid, false otherwise
     */
    @Override
    public boolean validateCommand() {
        Player l_Player = getOrderInfo().getPlayer();
        Country l_Target = getOrderInfo().getTargetCountry();

        if (l_Player == null || l_Target == null) {
            d_Logger.log("Invalid bomb order: Player or target is null.");
            return false;
        }
//todo: add checkIfCardAvailable in Player.java
        if (!l_Player.cardAvailable(CardType.BOMB)) {
            d_Logger.log("Invalid bomb order: Player does not have a Bomb card.");
            return false;
        }

        if (l_Player.isCaptured(l_Target)) {
            d_Logger.log("Invalid bomb order: Cannot bomb your own country.");
            return false;
        }

        boolean isAdjacent = false;
        for (Country l_PlayerCountry : l_Player.getCapturedCountries()) {
            if (l_PlayerCountry.getD_CountryNeighbors().contains(l_Target)) {
                isAdjacent = true;
                break;
            }
        }

        if (!isAdjacent) {
            d_Logger.log("Invalid bomb order: Target country is not adjacent to any of the player's countries.");
            return false;
        }

        if (l_Player.getNeutralPlayers().contains(l_Target.getPlayer())) {
            d_Logger.log(String.format("Truce between %s and %s. Bombing not allowed.",
                    l_Player.getD_Name(), l_Target.getPlayer().getD_Name()));
            l_Player.getNeutralPlayers().remove(l_Target.getPlayer());
            l_Target.getPlayer().getNeutralPlayers().remove(l_Player);
            return false;
        }

        return true;
    }

    /**
     * Logs the bomb order command
     */
    @Override
    public void printOrderCommand() {
        d_Logger.log(String.format("Bomb Order: Player %s targets country %s.",
                getOrderInfo().getPlayer().getD_Name(), getOrderInfo().getTargetCountry().getD_CountryName()));
        d_Logger.log("---------------------------------------------------------------------------------------------");
    }
}
