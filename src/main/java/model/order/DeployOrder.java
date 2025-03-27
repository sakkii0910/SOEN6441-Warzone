package model.order;

import model.Country;
import model.Player;
import utils.logger.LogEntryBuffer;

import java.io.Serializable;

/**
 * Represents a deployment order issued by a player.
 */
public class DeployOrder extends Order implements Serializable {

    /**
     * Logger instance
     */
    private LogEntryBuffer d_Logger = LogEntryBuffer.getInstance();

    public DeployOrder() {
        super();
        setType("deploy");
    }

    /**
     * Executes the deploy order.
     */
    @Override
    public boolean execute() {
        Country l_Destination = getOrderInfo().getDestination();
        int l_ArmiesToDeploy = getOrderInfo().getNumberOfArmy();
        d_Logger.log("---------------------------------------------------------------------------------------------");
        if (validateCommand()) {
            d_Logger.log(l_ArmiesToDeploy + " has been deployed to " + l_Destination + ".");
            l_Destination.setD_Armies(l_ArmiesToDeploy);
            return true;
        }
        return false;
    }

    /**
     * A function to validate the commands
     *
     * @return true if command can be executed else false
     */
    @Override
    public boolean validateCommand() {
        Player l_Player = getOrderInfo().getPlayer();
        Country l_Destination = getOrderInfo().getDestination();
        int l_Reinforcements = getOrderInfo().getNumberOfArmy();

        if (l_Player == null || l_Destination == null) {
            d_Logger.log("Invalid order information.The entered values are invalid.");
            return false;
        }
        if (!l_Player.isCaptured(l_Destination)) {
            d_Logger.log("The country does not belong to you");
            return false;
        }

        if (!l_Player.deployReinforcementArmiesFromPlayer(l_Reinforcements)) {
            System.out.println("You do not have enough Reinforcement Armies to deploy.");
            return false;
        }
        return true;
    }

    /**
     * A function to print the order on completion
     */
    public void printOrderCommand() {
        d_Logger.log("Deployed " + getOrderInfo().getNumberOfArmy() + " armies to " + getOrderInfo().getDestination().getD_CountryName() + ".");
        d_Logger.log("---------------------------------------------------------------------------------------------");
    }
}
