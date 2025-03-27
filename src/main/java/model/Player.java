package model;

import controller.IssueOrder;
import model.strategy.PlayerStrategy;

import java.io.Serializable;
import java.util.*;

/**
 * The type Player.
 */
public class Player implements Serializable {
    private int d_Id;

    private String d_Name;

    private List<Country> d_CapturedCountries = new ArrayList<>();

    private int d_ReinforcementArmies;

    private Deque<Order> d_Orders = new ArrayDeque<>();

    /**
     * number of armies to issue
     */
    private int d_ArmiesToIssue = 0;

    /**
     * Player Strategy to create the commands
     */
    private final PlayerStrategy d_PlayerStrategy;

    /**
     * the constructor for player class
     *
     * @param p_PlayerStrategy player strategy
     */
    public Player(PlayerStrategy p_PlayerStrategy) {
        this.d_PlayerStrategy = p_PlayerStrategy;
    }

    /**
     * method to get armies issued
     *
     * @return issues armies
     */
    public int getIssuedArmies() {
        return d_ArmiesToIssue;
    }

    /**
     * method to set the armies issued
     * @param p_ArmiesToIssue armies to issue to player
     */
    public void setIssuedArmies(int p_ArmiesToIssue) {
        d_ArmiesToIssue = p_ArmiesToIssue;
    }

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
     * A function to check if the country exists in the list of player captured countries
     *
     * @param p_Country The country to be checked if present
     * @return true if country exists in the assigned country list else false
     */
    public boolean isCaptured(Country p_Country) {
        return d_CapturedCountries.contains(p_Country);
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
     * Assigns reinforcements to the player.
     *
     * @param p_num Number of reinforcements to assign.
     */
    public void assignReinforcements(int p_num) {
        d_ReinforcementArmies += p_num;
        System.out.println("The player: " + getD_Name() + " is assigned " + p_num + " reinforcements.");
    }

    /**
     * A function to get the list of orders
     *
     * @return list of orders
     */
    public Deque<Order> getOrders() {
        return d_Orders;
    }

    /**
     * method to set orders
     * @param p_Orders the orders
     */
    public void setOrders(Deque<Order> p_Orders){
        this.d_Orders = p_Orders;
    }
    /**
     * A function to add the orders to the issue order list
     *
     * @param p_Order The order to be added
     */
    public void addOrder(Order p_Order) {
        this.d_Orders.add(p_Order);
    }

    /**
     * Retrieves the next order in the queue.
     *
     * @return the next Order object
     */
    public Order nextOrder() {
        return d_Orders.poll();
    }

    /**
     * Handles user input for issuing an order.
     */
    public void issueOrder() {
        System.out.println("-----------------------------------------");
        
        Scanner l_scanner = new Scanner(System.in);

        System.out.println("Current Player: " + getD_Name());

        System.out.print("Enter command (deploy countryID(name) num): ");
        String l_input = l_scanner.nextLine();

        Order l_Order = OrderCreator.CreateOrder(l_input.split(" "), this);
        addOrder(l_Order);
    }

    public String readCommand() {
        return this.d_PlayerStrategy.createCommand();
    }

}
