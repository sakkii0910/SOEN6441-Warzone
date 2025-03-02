package model;

import controller.Country;
import controller.Order;

//todo: change coding conventions
//todo: need to create Order class
public class DeployOrder extends Order {
    private Player player;
    private int countryID;
    private int numArmies;

    public DeployOrder(Player player, int countryID, int numArmies) {
        this.player = player;
        this.countryID = countryID;
        this.numArmies = numArmies;
    }


    public void execute() {
        //todo: ll also req country and game map to get country
        Country country = GameMap.getCountryById(countryID);
        if (country != null) {
            country.addArmies(numArmies);
            System.out.println("Deployed " + numArmies + " armies to country " + countryID);
        } else {
            System.out.println("Invalid country ID.");
        }
    }
    //todo: instaed of terminating, ll need to loop for user input
}

