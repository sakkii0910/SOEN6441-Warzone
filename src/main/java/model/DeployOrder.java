package model;

import controller.Country;
import controller.Order;

/**
 * Represents a deployment order issued by a player.
 */
public class DeployOrder extends Order {
    private Player d_player;
    private int d_countryID;
    private int d_numArmies;

    /**
     * Constructor for DeployOrder.
     *
     * @param p_player The player issuing the order.
     * @param p_countryID The country to deploy armies to.
     * @param p_numArmies The number of armies to deploy.
     */
    public DeployOrder(Player p_player, int p_countryID, int p_numArmies) {
        this.d_player = p_player;
        this.d_countryID = p_countryID;
        this.d_numArmies = p_numArmies;
    }

    /**
     * Executes the deploy order.
     */
    public void execute() {
        Country l_country = GameMap.getCountryById(d_countryID);
        if (l_country != null) {
            l_country.addArmies(d_numArmies);
            System.out.println("Deployed " + d_numArmies + " armies to country " + d_countryID);
        } else {
            System.out.println("Invalid country ID.");
        }
    }
}
