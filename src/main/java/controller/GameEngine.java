package controller;

import model.Player;
import java.util.Queue;
import java.util.LinkedList;

/**
 * Manages the game's main logic, handling player actions and execution flow.
 */
public class GameEngine {
    private Queue<Order> d_orderQueue = new LinkedList<>();
    private List<Player> d_players = new ArrayList<>();

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
    public void mainGameLoop() {
        while (true) {
            // Assign reinforcements
            for (Player l_player : d_players) {
                l_player.assignReinforcements(5);
            }

            // Issue orders
            for (Player l_player : d_players) {
                l_player.issueOrder();
            }

            // Execute orders
            for (Player l_player : d_players) {
                Order l_order;
                while ((l_order = l_player.nextOrder()) != null) {
                    l_order.execute();
                }
            }
        }
    }
}
