package model.order;

import model.Card;
import model.CardType;
import model.GameMap;
import model.Player;
import utils.logger.LogEntryBuffer;

import java.io.Serializable;

/**
 * Represents a Diplomacy Order - negotiates peace with another player.
 *  @author Pruthvirajsinh Dodiya
 */
public class NegotiateOrder extends Order implements Serializable {

    /** GameMap singleton instance */
    public final GameMap d_GameMap;

    /** Logger instance */
    public LogEntryBuffer d_Logger;

    /**
     * Constructor for NegotiateOrder
     */
    public NegotiateOrder() {
        super();
        setType("negotiate");
        d_GameMap = GameMap.getInstance();
        d_Logger = LogEntryBuffer.getInstance();
    }

    /**
     * Executes the diplomacy order: establishes truce between two players.
     *
     * @return true if executed successfully, false otherwise
     */
    @Override
    public boolean execute() {
        Player l_Player = getOrderInfo().getPlayer();
        Player l_TargetPlayer = getOrderInfo().getNeutralPlayer();

        d_Logger.log("---------------------------------------------------------------------------------------------");
        d_Logger.log(getOrderInfo().getCommand());

        if (!validateCommand()) {
            return false;
        }

        l_Player.addNeutralPlayers(l_TargetPlayer);
        l_TargetPlayer.addNeutralPlayers(l_Player);
        l_Player.removeCard(new Card(CardType.DIPLOMACY));

        d_Logger.log(String.format("Diplomacy successful: %s and %s will not attack each other this turn.",
                l_Player.getD_Name(), l_TargetPlayer.getD_Name()));
        d_Logger.log("---------------------------------------------------------------------------------------------");

        return true;
    }

    /**
     * Validates the diplomacy order.
     *
     * @return true if valid, false otherwise
     */
    @Override
    public boolean validateCommand() {
        Player l_Player = getOrderInfo().getPlayer();
        Player l_TargetPlayer = getOrderInfo().getNeutralPlayer();

        if (l_Player == null || l_TargetPlayer == null) {
            d_Logger.log("Invalid diplomacy order: Player or target is null.");
            return false;
        }

        if (!l_Player.cardAvailable(CardType.DIPLOMACY)) {
            d_Logger.log("Invalid diplomacy order: Player does not have a Diplomacy card.");
            return false;
        }

        if (!d_GameMap.getPlayers().containsKey(l_TargetPlayer.getD_Name())) {
            d_Logger.log("Invalid diplomacy order: Target player does not exist in the game.");
            return false;
        }

        return true;
    }

    /**
     * Logs the diplomacy order command
     */
    @Override
    public void printOrderCommand() {
        d_Logger.log(String.format("Diplomacy Order: Player %s negotiates with %s.",
                getOrderInfo().getPlayer().getD_Name(), getOrderInfo().getNeutralPlayer().getD_Name()));
        d_Logger.log("---------------------------------------------------------------------------------------------");
    }
}
