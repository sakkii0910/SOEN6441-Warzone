package model.order;

import model.Card;
import model.CardType;
import model.Country;
import model.GameMap;
import model.Player;
import utils.logger.LogEntryBuffer;

import java.io.Serializable;

/**
 * Represents an Airlift Order - transfers armies between two owned countries.
 * @author Pruthvirajsinh Dodiya
 */
public class AirliftOrder extends Order implements Serializable {

    /** Game Map singleton instance */
    public GameMap d_GameMap;

    /** Logger instance */
    public LogEntryBuffer d_Logger;

    /**
     * Constructor for AirliftOrder
     */
    public AirliftOrder() {
        super();
        setType("airlift");
        d_GameMap = GameMap.getInstance();
        d_Logger = LogEntryBuffer.getInstance();
    }

    /**
     * Executes the airlift order - moves armies from one country to another owned by the player.
     *
     * @return true if successful, false otherwise
     */
    @Override
    public boolean execute() {
        Player l_Player = getOrderInfo().getPlayer();
        Country l_From = getOrderInfo().getDeparture();
        Country l_To = getOrderInfo().getDestination();
        int l_Armies = getOrderInfo().getNumberOfArmy();

        d_Logger.log("---------------------------------------------------------------------------------------------");
        d_Logger.log(getOrderInfo().getCommand());

        if (!validateCommand()) {
            return false;
        }

        l_From.setD_Armies(l_From.getD_Armies() - l_Armies);
        l_To.setD_Armies(l_To.getD_Armies() + l_Armies);
        l_Player.removeCard(new Card(CardType.AIRLIFT));

        d_Logger.log(String.format("Airlift executed: %d armies moved from %s to %s.",
                l_Armies, l_From.getD_CountryName(), l_To.getD_CountryName()));
        d_Logger.log("---------------------------------------------------------------------------------------------");
        return true;
    }

    /**
     * Validates the airlift command.
     *
     * @return true if valid, false otherwise
     */
    @Override
    public boolean validateCommand() {
        Player l_Player = getOrderInfo().getPlayer();
        Country l_From = getOrderInfo().getDeparture();
        Country l_To = getOrderInfo().getDestination();
        int l_Armies = getOrderInfo().getNumberOfArmy();

        if (l_Player == null || l_From == null || l_To == null) {
            d_Logger.log("Airlift failed: Missing order info.");
            return false;
        }

        if (!l_Player.cardAvailable(CardType.AIRLIFT)) {
            d_Logger.log("Airlift failed: Player does not have an Airlift card.");
            return false;
        }

        if (!l_Player.isCaptured(l_From) || !l_Player.isCaptured(l_To)) {
            d_Logger.log("Airlift failed: Both source and destination countries must belong to the player.");
            return false;
        }

        if (l_Armies <= 0 || l_From.getD_Armies() < l_Armies) {
            d_Logger.log("Airlift failed: Invalid number of armies.");
            return false;
        }

        return true;
    }

    /**
     * Logs the airlift order command.
     */
    @Override
    public void printOrderCommand() {
        d_Logger.log(String.format("Airlift Order: %s moves %d armies from %s to %s.",
                getOrderInfo().getPlayer().getD_Name(),
                getOrderInfo().getNumberOfArmy(),
                getOrderInfo().getDeparture().getD_CountryName(),
                getOrderInfo().getDestination().getD_CountryName()));
        d_Logger.log("---------------------------------------------------------------------------------------------");
    }
}
