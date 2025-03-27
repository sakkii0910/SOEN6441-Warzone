package model.order;

import model.Country;
import model.GameSettings;
import model.Player;
import model.strategy.game.GameStrategy;
import utils.logger.LogEntryBuffer;

import java.io.Serializable;
import java.util.Objects;

/**
 * Class holding the properties of advance order
 */
public class AdvanceOrder extends Order implements Serializable {
    /**
     * Game Settings object
     */
    GameSettings d_Settings = GameSettings.getInstance();

    /**
     * Game Strategy object
     */
    GameStrategy d_GameStrategy;

    /**
     * Log Entry Buffer Object
     */
    private LogEntryBuffer d_Logger = LogEntryBuffer.getInstance();

    /**
     * Constructor for class AdvanceOrder
     */
    public AdvanceOrder() {
        super();
        setType("advance");
        d_GameStrategy = d_Settings.getStrategy();
    }

    /**
     * Advance the number of armies from a source country to destination country
     * Advance if self conquered
     * Advance if destination does not belong to any player.
     * Attack if a country belongs to enemy player.
     * Skips the attack command if player exists in NeutralPlayers list.
     * Remove the neutral player from the list once the attack is negotiated.
     * The attack happens in the next turn of the same phase on same player if the
     * negotiation happened already once in the phase.
     *
     * @return true if command is successfully executed or skipped else false
     */
    @Override
    public boolean execute() {
        d_Logger.log("---------------------------------------------------------------------------------------------");
        d_Logger.log(getOrderInfo().getCommand());
        if (validateCommand()) {
            Player l_Player = getOrderInfo().getPlayer();
            Country l_From = getOrderInfo().getDeparture();
            Country l_To = getOrderInfo().getDestination();
            int l_Armies = getOrderInfo().getNumberOfArmy();
            if (l_Player.getNeutralPlayers().contains(l_To.getPlayer())) {
                d_Logger.log(String.format("Truce between %s and %s\n", l_Player.getD_Name(), l_To.getPlayer().getD_Name()));
                l_Player.getNeutralPlayers().remove(l_To.getPlayer());
                l_To.getPlayer().getNeutralPlayers().remove(l_Player);
                return true;
            }
            if (l_Player.isCaptured(l_To) || Objects.isNull(l_To.getPlayer())) {
                l_From.depleteArmies(l_Armies);
                l_To.deployArmies(l_Armies);
                if (!l_Player.getCapturedCountries().contains(l_To)) {
                    l_Player.getCapturedCountries().add(l_To);
                }
                l_To.setPlayer(l_Player);
                d_Logger.log("Advanced/Moved " + l_Armies + " from " + l_From.getD_CountryName() + " to " + l_To.getD_CountryName());
                return true;
            } else if (d_GameStrategy.attack(l_Player, l_From, l_To, l_Armies)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Validate command
     *
     * @return Returns true if valid command else false
     */
    @Override
    public boolean validateCommand() {
        Player l_Player = getOrderInfo().getPlayer();
        Country l_From = getOrderInfo().getDeparture();
        Country l_To = getOrderInfo().getDestination();
        int l_Armies = getOrderInfo().getNumberOfArmy();
        boolean success = true;
        String log = "Failed due to some errors\n";
        if (l_Player == null || l_To == null || l_From == null) {
            System.out.println("Invalid order information.");
            return false;
        }
        if (!l_Player.isCaptured(l_From)) {
            System.out.format("Failed due to this error-\n");
            System.out.format("\t-> Country %s does not belong to player %s\n", l_From.getD_CountryName(), l_Player.getD_Name());
            success = false;
        }
        if (!l_From.getD_CountryNeighbors().contains(l_To)) {
            System.out.println("Failed due to this error-\n");
            System.out.format("\t-> Destination country %s is not a neighbor of %s\n", l_To.getD_CountryName(), l_From.getD_CountryName());
            success = false;
        }
        if (l_Armies > l_From.getD_Armies()) {
            System.out.println("Failed due to this error-\n");
            System.out.format("\t-> Attacking troop count %d is greater than available troops %d", l_Armies, l_From.getD_CountryName());

            success = false;
        }
        if (!success) {
            d_Logger.log(log);
        }
        return success;
    }

    /**
     * Print the command
     */
    @Override
    public void printOrderCommand() {
        d_Logger.log("Order Info: Advance " + getOrderInfo().getNumberOfArmy() + " armies " + " from " + getOrderInfo().getDeparture().getD_CountryName() + " to " + getOrderInfo().getDestination().getD_CountryName() + ".");
        d_Logger.log("---------------------------------------------------------------------------------------------");
    }
}