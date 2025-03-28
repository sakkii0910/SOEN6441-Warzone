package model.order;

import model.Card;
import model.CardType;
import model.Country;
import model.GameMap;
import model.Player;
import utils.logger.LogEntryBuffer;

import java.io.Serializable;

/**
 * Represents a Blockade Order - triples armies and turns country neutral.
 */
public class BlockadeOrder extends Order implements Serializable {

    /** Game Map singleton instance */
    public GameMap d_GameMap;

    /** Logger instance */
    public LogEntryBuffer d_Logger;

    /**
     * Constructor for BlockadeOrder
     */
    public BlockadeOrder() {
        super();
        setType("blockade");
        d_GameMap = GameMap.getInstance();
        d_Logger = LogEntryBuffer.getInstance();
    }

    /**
     * Executes the blockade order: triples the armies and converts country to neutral.
     *
     * @return true if successful, false otherwise
     */
    @Override
    public boolean execute() {
        Player l_Player = getOrderInfo().getPlayer();
        Country l_Country = getOrderInfo().getTargetCountry();

        d_Logger.log("---------------------------------------------------------------------------------------------");
        d_Logger.log(getOrderInfo().getCommand());

        if (!validateCommand()) {
            return false;
        }

        int l_OriginalArmies = l_Country.getD_Armies();
        int l_TripledArmies = l_OriginalArmies * 3;
        l_Country.setD_Armies(l_TripledArmies);

        // Remove ownership
        l_Player.getCapturedCountries().remove(l_Country);
        l_Country.setPlayer(null);
        

        l_Player.removeCard(new Card(CardType.BLOCKADE));

        d_Logger.log(String.format("Blockade executed on %s: Armies tripled from %d to %d, now neutral territory.",
                l_Country.getD_CountryName(), l_OriginalArmies, l_TripledArmies));
        d_Logger.log("---------------------------------------------------------------------------------------------");

        return true;
    }

    /**
     * Validates whether the blockade order is legal.
     *
     * @return true if valid, false otherwise
     */
    @Override
    public boolean validateCommand() {
        Player l_Player = getOrderInfo().getPlayer();
        Country l_Country = getOrderInfo().getTargetCountry();

        if (l_Player == null || l_Country == null) {
            d_Logger.log("Invalid blockade order: Player or target country is null.");
            return false;
        }

        if (!l_Player.cardAvailable(CardType.BLOCKADE)) {
            d_Logger.log("Invalid blockade order: Player does not have a Blockade card.");
            return false;
        }

        if (!l_Player.isCaptured(l_Country)) {
            d_Logger.log("Invalid blockade order: Country does not belong to player.");
            return false;
        }

        return true;
    }

    /**
     * Logs the blockade order command
     */
    @Override
    public void printOrderCommand() {
        d_Logger.log(String.format("Blockade Order: Player %s triples armies in country %s and turns it neutral.",
                getOrderInfo().getPlayer().getD_Name(), getOrderInfo().getTargetCountry().getD_CountryName()));
        d_Logger.log("---------------------------------------------------------------------------------------------");
    }
}
