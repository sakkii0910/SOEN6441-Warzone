package controller;

import model.Country;
import model.GameMap;
import model.Player;
import model.order.DeployOrder;
import model.order.OrderInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.GameEngine;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ExecuteOrder} to validate order execution logic.
 * Covers deployment, phase completion, and queue processing scenarios.
 * @author Sakshi Sudhir Mulik
 */
class ExecuteOrderTest {
    private ExecuteOrder d_executeOrder;
    private GameMap d_gameMap;
    private Player d_player;
    private Country d_country;

    /**
     * Initializes test environment before each test case.
     * Creates a fresh game map, player, and country for isolation.
     */
    @BeforeEach
    void setUp() {
        d_gameMap = GameMap.getInstance();
        d_gameMap.resetGameMap();

        d_player = new Player(null);
        d_player.setD_Name("TestPlayer");
        d_player.assignReinforcements(5);

        d_country = new Country();
        d_country.setD_CountryName("TestCountry");
        d_country.setPlayer(d_player);

        d_player.getCapturedCountries().add(d_country);
        d_gameMap.getCountries().put("TestCountry", d_country);
        d_gameMap.getPlayers().put("TestPlayer", d_player);

        d_executeOrder = new ExecuteOrder(new GameEngine());
    }

    /**
     * Tests single-order deployment execution.
     * Verifies armies are correctly deployed to the target country.
     * @throws Exception if order execution fails
     */
    @Test
    void testQuickOrderExecution() throws Exception {
        OrderInfo l_info = new OrderInfo();
        l_info.setPlayer(d_player);
        l_info.setNumberOfArmy(1);
        l_info.setDestination(d_country);

        DeployOrder l_order = new DeployOrder();
        l_order.setOrderInfo(l_info);
        d_player.addOrder(l_order);

        d_executeOrder.startPhase();
        assertEquals(1, d_country.getD_Armies());
    }

    /**
     * Tests phase completion with no pending orders.
     * Ensures the phase exits cleanly when the order queue is empty.
     * @throws Exception if phase transition fails
     */
    @Test
    void testImmediatePhaseCompletion() throws Exception {
        d_executeOrder.startPhase();
        assertTrue(d_player.getOrders().isEmpty());
    }

    /**
     * Tests processing of multiple orders in the queue.
     * Validates cumulative army deployment and queue clearance.
     * @throws Exception if order processing fails
     */
    @Test
    void testOrderQueueProcessing() throws Exception {
        for (int i = 1; i <= 3; i++) {
            OrderInfo l_info = new OrderInfo();
            l_info.setPlayer(d_player);
            l_info.setNumberOfArmy(1);
            l_info.setDestination(d_country);

            DeployOrder l_order = new DeployOrder();
            l_order.setOrderInfo(l_info);
            d_player.addOrder(l_order);
        }

        d_executeOrder.startPhase();
        assertEquals(3, d_country.getD_Armies());
        assertTrue(d_player.getOrders().isEmpty());
    }
}