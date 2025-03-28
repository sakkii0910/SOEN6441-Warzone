package model.order;

import model.*;
import utils.logger.LogEntryBuffer;

import java.io.Serializable;


/**
 * This class helps in executing the Blockade Card
 *
 */
public class BlockadeOrder extends Order implements Serializable {

    /**
     * Logger Observable
     */
    private LogEntryBuffer d_Logger = LogEntryBuffer.getInstance();

    /**
     * Constructor for class Blockade Order
     */
    public BlockadeOrder() {
        super();
        setType("blockade");
    }

    /**
     * Execute the Blockade Order
     *
     * @return true if the execute was successful else false
     */
    @Override
    public boolean execute() {
        Player l_Player = getOrderInfo().getPlayer();
        Country l_Country = getOrderInfo().getTargetCountry();
        d_Logger.log("---------------------------------------------------------------------------------------------");
        d_Logger.log(getOrderInfo().getCommand());
        if (validateCommand()) {
            l_Country.setD_Armies(l_Country.getD_Armies() * 3);
            l_Country.addNeutralCountry(l_Country);
            l_Player.getCapturedCountries().remove(l_Country);
            l_Player.removeCard(new Card(CardType.BLOCKADE));
            return true;
        }
        return false;
    }

    /**
     * Validate the command
     *
     * @return true if successful or else false
     */
    @Override
    public boolean validateCommand() {
        Player l_Player = getOrderInfo().getPlayer();
        Country l_Country = getOrderInfo().getTargetCountry();

        if (l_Player == null) {
            System.err.println("The Player is not valid.");
            d_Logger.log("The Player is not valid.");
            return false;
        }

        if (l_Country.getPlayer() != l_Player) {
            System.err.println("The target country does not belong to the player");
            d_Logger.log("The target country does not belong to the player");
            return false;
        }
        if (!l_Player.cardAvailable(CardType.BLOCKADE)) {
            System.err.println("Player doesn't have Blockade Card.");
            d_Logger.log("Player doesn't have Blockade Card.");
            return false;
        }
        return true;
    }

    /**
     * Print the command
     */
    @Override
    public void printOrderCommand() {
        d_Logger.log("Blockade on " + getOrderInfo().getTargetCountry().getD_CountryName() + " by " + getOrderInfo().getPlayer().getD_Name());
        d_Logger.log("---------------------------------------------------------------------------------------------");
    }
}