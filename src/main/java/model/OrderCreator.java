package model;

import model.Country;
import model.GameMap;
import model.Player;

import java.io.Serializable;
import java.util.StringJoiner;

/**
 * A class to create Orders in the game.
 */
public class OrderCreator implements Serializable {
    /**
     * Static object of Game Map to hold instance of game map
     */
    public static GameMap d_GameMap = GameMap.getInstance();

    /**
     * A function to create an order
     *
     * @param p_Commands the command entered
     * @param p_Player   object parameter of type Player
     * @return the order
     */
    public static Order CreateOrder(String[] p_Commands, Player p_Player) {
        String l_Type = p_Commands[0].toLowerCase();
        Order l_Order;
        switch (l_Type) {
            case "deploy":
                l_Order = new DeployOrder();
                l_Order.setOrderInfo(GenerateDeployOrderInfo(p_Commands, p_Player));
                break;

            default:
                System.out.println("\nFailed to create an order due to invalid arguments");
                l_Order = null;

        }
        return l_Order;
    }

    /**
     * A function to generate the information of Deploying the order
     *
     * @param p_Command the command entered
     * @param p_Player  object parameter of type Player
     * @return the order information of deploy
     */
    public static OrderInfo GenerateDeployOrderInfo(String[] p_Command, Player p_Player) {
        Country l_Country = d_GameMap.getCountry(p_Command[1]);
        int l_NumberOfArmies = Integer.parseInt(p_Command[2]);
        OrderInfo l_OrderInfo = new OrderInfo();
        l_OrderInfo.setCommand(ConvertToString(p_Command));
        l_OrderInfo.setPlayer(p_Player);
        l_OrderInfo.setDestination(l_Country);
        l_OrderInfo.setNumberOfArmy(l_NumberOfArmies);
        if(p_Player.getReinforcementArmies() > 0 && l_NumberOfArmies <= p_Player.getIssuedArmies() && l_NumberOfArmies > 0){
            p_Player.setIssuedArmies(p_Player.getIssuedArmies() - l_NumberOfArmies);
        }
        return l_OrderInfo;
    }

    /**
     * The method to convert command to string
     *
     * @param p_Commands the command entered
     * @return the string
     */
    private static String ConvertToString(String[] p_Commands) {
        StringJoiner l_Joiner = new StringJoiner(" ");
        for (int l_Index = 0; l_Index < p_Commands.length; l_Index++) {
            l_Joiner.add(p_Commands[l_Index]);
        }
        return l_Joiner.toString();
    }

}