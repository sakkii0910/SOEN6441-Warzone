package controller;

import model.Continent;
import model.Country;
import model.GameMap;
import model.Order;
import model.Player;
import model.abstractClasses.GameController;
import model.abstractClasses.GamePhase;
import model.gamePhases.ExitGamePhase;
import model.gamePhases.GameLoopPhase;

import java.util.*;

/**
 * Manages the game's main logic, handling player actions and execution flow.
 */
public class GameEngine extends GameController {
    private final Queue<Order> d_orderQueue = new LinkedList<>();
    private final HashMap<String, Player> d_players;

    private final GamePhase d_NextPhase = new ExitGamePhase();

    /**
     * Instantiates a new Game engine.
     */
    public GameEngine() {
        GameMap d_GameMap = GameMap.getInstance();
        d_players = d_GameMap.getPlayers();
    }

    /**
     * Adds an order to the execution queue.
     *
     * @param p_order The order to be added.
     */
    public void addOrder(Order p_order) {
        d_orderQueue.add(p_order);
    }

    /**
     * Executes all orders in the queue.
     */
    public void executeOrders() {
        while (!d_orderQueue.isEmpty()) {
            Order l_order = d_orderQueue.poll();
            l_order.execute();
        }
    }

    /**
     * Main game loop that handles the reinforcement, order issuing, and execution phases.
     */
    public GamePhase startPhase(GamePhase p_gamePhase) {
        System.out.println();
        System.out.println("=========================================");
        System.out.println("\t****** GAME STARTED ******\t");
        // Assign reinforcements
        for (Player l_player : d_players.values()) {
            int l_reinforcements = calculateReinforcements(l_player);
            l_player.assignReinforcements(l_reinforcements);
        }

        System.out.println("\n-----------------------------------------");
        System.out.println(" ISSUE ORDER PHASE ");

        // Issue orders
        for (Player l_player : d_players.values()) {
            l_player.issueOrder();
        }

        System.out.println("\n-----------------------------------------");
        System.out.println(" EXECUTE ORDER PHASE ");

        // Execute orders
        for (Player l_player : d_players.values()) {
            Order l_order;
            while ((l_order = l_player.nextOrder()) != null) {
                l_order.execute();
            }
        }

        return d_NextPhase;

    }

    /**
 * Calculates the reinforcement armies for a player.
 * @param p_player The player whose reinforcements are to be calculated.
 * @return The number of reinforcement armies.
 */
private int calculateReinforcements(Player p_player) {
    int l_territoryCount = p_player.getCapturedCountries().size();
    int l_reinforcements = Math.max(l_territoryCount / 3, 3); // Minimum of 3 reinforcements

    // Check if the player controls an entire continent
    for (Continent l_continent : GameMap.getInstance().getContinents().values()) {
        boolean l_controlsAll = true;
        for (Country l_country : l_continent.getD_ContinentCountries()) {
            if (l_country.getPlayer() != p_player) { // If any country is not owned by the player
                l_controlsAll = false;
                break;
            }
        }
        if (l_controlsAll) {
            l_reinforcements += l_continent.getD_ContinentArmies(); // Add continent bonus
        }
    }

    return l_reinforcements;
}

}
