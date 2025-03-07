package controller;

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
            l_player.assignReinforcements(5);
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
}
