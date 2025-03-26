package model;

import controller.IssueOrder;

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

    private List<Card> d_PlayerCards = new ArrayList<>();

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
     *
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
     *
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

        System.out.print("Enter command (deploy countryID(name) num): ");
        String l_input = l_scanner.nextLine();

        Order l_Order = OrderCreator.CreateOrder(l_input.split(" "), this);
        addOrder(l_Order);
    }

    /**
     * Gets player cards.
     *
     * @return the player cards
     */
    public List<Card> getPlayerCards() {
        return d_PlayerCards;
    }

    /**
     * Sets player cards.
     *
     * @param p_Cards the p cards
     */
    public void setPlayerCards(List<Card> p_Cards) {
        this.d_PlayerCards = p_Cards;
    }

    /**
     * Checks if a certain card is available.
     *
     * @param p_CardType the p card type
     * @return the boolean
     */
    public boolean cardAvailable(CardType p_CardType) {
        return this.d_PlayerCards.stream().anyMatch(l_Card -> l_Card.getCardType().equals(p_CardType));
    }

    /**
     * Add card.
     *
     * @param p_Card the p card
     */
    public void addCard(Card p_Card) {
        this.d_PlayerCards.add(p_Card);
    }

    /**
     * Remove card.
     *
     * @param p_Card the p card
     */
    public void removeCard(Card p_Card) {
        this.d_PlayerCards.remove(p_Card);
    }

    /**
     * Clear cards.
     */
    public void clearCards() {
        this.d_PlayerCards.clear();
    }

}
