package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Player implements Serializable {
    // Interger ID of the player
    private int d_Id;

    // Name of the player
    private String d_Name;

    // List of captured countries of the player
    private List<Country> d_CapturedCountries = new ArrayList<>();

    // Integer to store number reinforcement armies of player
    private int d_ReinforcementArmies;

    // Get ID of the player
    public int getD_Id() {
        return d_Id;
    }

    // Set ID of the player
    // @param p_Id Player ID
    public void setD_Id(int p_Id) {
        this.d_Id = p_Id;
    }

    // Get the name of the player
    public String getD_Name() {
        return d_Name;
    }

    // Set the name of the player
    public void setD_Name(String p_Name) {
        this.d_Name = p_Name;
    }

    // Get the captured countries of the player
    public List<Country> getCapturedCountries() {
        return d_CapturedCountries;
    }

    // Function to create a list of countries assigned to the player
    public String createACaptureList(List<Country> p_Capture) {
        StringBuilder l_Result = new StringBuilder();
        for (Country l_Capture : p_Capture) {
            l_Result.append(l_Capture.getD_CountryName()).append("-");
        }
        return (!l_Result.isEmpty()) ? l_Result.substring(0, l_Result.length() - 1) : "";
    }

    // Function to get reinforcement countries of each player
    public int getReinforcementArmies() {
        return d_ReinforcementArmies;
    }


    //Pruthviraj's edit
    private List<Order> d_orders = new ArrayList<>();

    /**
     * Retrieves the next order in the queue.
     *
     * @return the next Order object or null if no orders are available.
     */
    public Order nextOrder() {
        if (d_orders.isEmpty()) {
            return null;
        }
        return d_orders.remove(0);
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
        System.out.println("\n-----------------------------------------");
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
                d_orders.add(l_order);
                d_ReinforcementArmies -= l_num;
                System.out.println("Order added by player " + getD_Name() + ": Deploy " + l_num + " armies to country " + l_countryID);
                System.out.println("Armies left: " + d_ReinforcementArmies);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format.");
            }
        }
    }

}
