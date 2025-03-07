package model;

import model.Country;
import model.Order;

/**
 * Represents a deployment order issued by a player.
 */
public class DeployOrder extends Order {
    private Player d_player;
    private String d_countryID;
    private int d_numArmies;

    /**
     * Constructor for DeployOrder.
     *
     * @param p_player    The player issuing the order.
     * @param p_countryID The country to deploy armies to.
     * @param p_numArmies The number of armies to deploy.
     */
    public DeployOrder(Player p_player, String p_countryID, int p_numArmies) {
        this.d_player = p_player;
        this.d_countryID = p_countryID;
        this.d_numArmies = p_numArmies;
    }

    /**
     * Executes the deploy order.
     */
    public void execute() {
        Country l_country = GameMap.getInstance().getCountryByName(d_countryID);
        l_country.setD_Armies(d_numArmies);
        System.out.println("Deployed " + d_numArmies + " armies to country " + d_countryID);
    }
}
