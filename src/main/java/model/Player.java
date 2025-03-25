package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The type Player.
 */
public class Player implements Serializable {
    private int d_Id;

    private String d_Name;

    private List<Country> d_CapturedCountries = new ArrayList<>();

    private int d_ReinforcementArmies;

    private List<Order> d_Orders = new ArrayList<>();

    /**
     * Gets d id.
     *
     * @return the d id
     */
    public int getD_Id() {
        return d_Id;
    }

    /**
     * Sets d id.
     *
     * @param p_Id the p id
     */
    public void setD_Id(int p_Id) {
        this.d_Id = p_Id;
    }

    /**
     * Gets d name.
     *
     * @return the d name
     */
    public String getD_Name() {
        return d_Name;
    }

    /**
     * Sets d name.
     *
     * @param p_Name the p name
     */
    public void setD_Name(String p_Name) {
        this.d_Name = p_Name;
    }

    /**
     * Gets captured countries.
     *
     * @return the captured countries
     */
    public List<Country> getCapturedCountries() {
        return d_CapturedCountries;
    }

    /**
     * Create a list of countries assigned to the player.
     *
     * @param p_Capture the p capture
     * @return the string
     */
    public String createACaptureList(List<Country> p_Capture) {
        StringBuilder l_Result = new StringBuilder();
        for (Country l_Capture : p_Capture) {
            l_Result.append(l_Capture.getD_CountryName()).append("-");
        }
        return (!l_Result.isEmpty()) ? l_Result.substring(0, l_Result.length() - 1) : "";
    }

    /**
     * Gets reinforcement armies.
     *
     * @return the reinforcement armies
     */
    public int getReinforcementArmies() {
        return d_ReinforcementArmies;
    }

    /**
     * Retrieves the next order in the queue.
     *
     * @return the next Order object or null if no orders are available.
     */
    public Order nextOrder() {
        if (d_Orders.isEmpty()) {
            return null;
        }
        return d_Orders.remove(0);
    }

    /**
     * Assigns reinforcements to the player.
     *
     * @param p_num Number of reinforcements to assign.
     */
    public void assignReinforcements(int p_num) {
        d_ReinforcementArmies += p_num;
        System.out.println("The player: " + getD_Name() + " is assigned " + p_num + " reinforcements.");
    }

    /**
     * Handles user input for issuing an order.
     */
    public void issueOrder() {
        System.out.println("-----------------------------------------");
        Scanner l_scanner = new Scanner(System.in);
        System.out.println("Current Player: " + getD_Name());
        while (d_ReinforcementArmies > 0) {
            System.out.print("Enter command (deploy countryID(name) num): ");
            String l_input = l_scanner.nextLine();
            String[] l_parts = l_input.split(" ");

            if (l_parts.length != 3 || !l_parts[0].equalsIgnoreCase("deploy")) {
                System.out.println("Invalid command format. Use: deploy countryID num");
                continue;
            }

            try {
                String l_countryID = l_parts[1];
                int l_num = Integer.parseInt(l_parts[2]);

                if (GameMap.getInstance().getCountryByName(l_countryID) == null) {
                    System.out.println("Country " + l_countryID + " does not exist.");
                    continue;
                }

                if (!getCapturedCountries().contains(GameMap.getInstance().getCountryByName(l_countryID))) {
                    System.out.println("Country " + l_countryID + " does not belong to user.");
                    continue;
                }

                if (l_num > d_ReinforcementArmies) {
                    System.out.println("Not enough reinforcements available.");
                    continue;
                }

                DeployOrder l_order = new DeployOrder(this, l_countryID, l_num);
                d_Orders.add(l_order);
                d_ReinforcementArmies -= l_num;
                System.out.println("Order added by player " + getD_Name() + ": Deploy " + l_num + " armies to country " + l_countryID);
                System.out.println("Armies left: " + d_ReinforcementArmies);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format.");
            }
        }
    }

}
