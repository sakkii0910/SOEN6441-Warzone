package controller;

import model.Player;

//todo: creating alll the missing symbols/classes esp Order, orderQueue and completing the Player.java
public class GameEngine {
    public void addOrder(Order order) {
        orderQueue.add(order);
    }
    public void executeOrders() {
        while (!orderQueue.isEmpty()) {
            Order order = orderQueue.poll();
            order.execute();
        }
    }
    public void mainGameLoop() {
        while (true) {
            // Assign reinforcements
            for (Player player : players) {
                player.assignReinforcements(5);
            }

            // Issue orders
            for (Player player : players) {
                player.issueOrder();
            }

            // Execute orders
            for (Player player : players) {
                Order order;
                while ((order = player.nextOrder()) != null) {
                    order.execute();
                }
            }
        }
    }

}
