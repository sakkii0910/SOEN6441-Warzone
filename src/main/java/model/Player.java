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

    // Function to create a list of list of countries assigned to the player
    public String createACaptureList(List<Country> p_Capture) {
        String l_Result = "";
        for (Country l_Capture : p_Capture) {
            l_Result += l_Capture.getD_CountryName() + "-";
        }
        return l_Result.length() > 0 ? l_Result.substring(0, l_Result.length() - 1) : "";
    }

    // Function to get reinforcement countries of each player
    public int getReinforcementArmies() {
        return d_ReinforcementArmies;
    }


//Pruthviraj's edit
    private List<Order> d_orders = new ArrayList<>();
    private int d_reinforcementPool;

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
        d_reinforcementPool += p_num;
        System.out.println("Assigned " + p_num + " reinforcements.");
    }

    /**
     * Handles user input for issuing an order.
     */
    public void issueOrder() {
        Scanner l_scanner = new Scanner(System.in);
        System.out.println("Enter command (deploy countryID num): ");
        String l_input = l_scanner.nextLine();
        String[] l_parts = l_input.split(" ");

        if (l_parts.length != 3 || !l_parts[0].equalsIgnoreCase("deploy")) {
            System.out.println("Invalid command format. Use: deploy countryID num");
            return;
        }

        try {
            int l_countryID = Integer.parseInt(l_parts[1]);
            int l_num = Integer.parseInt(l_parts[2]);

            if (l_num > d_reinforcementPool) {
                System.out.println("Not enough reinforcements available.");
                return;
            }

            DeployOrder l_order = new DeployOrder(this, l_countryID, l_num);
            d_orders.add(l_order);
            d_reinforcementPool -= l_num;
            System.out.println("Order added: Deploy " + l_num + " armies to country " + l_countryID);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format.");
        }
    }

}
