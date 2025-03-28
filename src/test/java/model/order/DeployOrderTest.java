package model.order;

import model.Country;
import model.GameMap;
import model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link DeployOrder} validation and execution.
 * Verifies deployment rules including ownership checks and army validation.
 *
 * @author Sakshi Sudhir Mulik
 */
public class DeployOrderTest {
    private Player d_Player;
    private Country d_Country;
    private GameMap d_GameMap;

    /**
     * Initializes test environment with player, country and game map.
     * Sets up a player with 10 reinforcement armies and an owned country.
     */
    @BeforeEach
    public void setUp() {
        d_GameMap = GameMap.getInstance();
        d_GameMap.resetGameMap();

        d_Player = new Player(null);
        d_Player.setD_Name("TestPlayer");
        d_Player.assignReinforcements(10);

        d_Country = new Country();
        d_Country.setD_CountryName("TestCountry");
        d_Country.setPlayer(d_Player);
        d_Country.setD_Armies(0);
        d_Player.getCapturedCountries().add(d_Country);
        d_GameMap.getCountries().put("TestCountry", d_Country);
    }

    /**
     * Tests deployment to unowned country.
     * Verifies deployment fails when target country isn't owned by player.
     */
    @Test
    public void testDeployToUnownedCountry() {
        Country l_UnownedCountry = new Country();
        l_UnownedCountry.setD_CountryName("UnownedCountry");
        l_UnownedCountry.setD_Armies(0);
        d_GameMap.getCountries().put("UnownedCountry", l_UnownedCountry);

        OrderInfo l_OrderInfo = new OrderInfo();
        l_OrderInfo.setPlayer(d_Player);
        l_OrderInfo.setDestination(l_UnownedCountry);
        l_OrderInfo.setNumberOfArmy(5);

        DeployOrder l_DeployOrder = new DeployOrder();
        l_DeployOrder.setOrderInfo(l_OrderInfo);

        assertFalse(l_DeployOrder.validateCommand());
        assertFalse(l_DeployOrder.execute());
        assertEquals(0, l_UnownedCountry.getD_Armies());
    }

    /**
     * Tests deployment with zero armies.
     * Verifies deployment fails when army count is zero.
     */
    @Test
    public void testDeployZeroArmies() {
        OrderInfo l_OrderInfo = new OrderInfo();
        l_OrderInfo.setPlayer(d_Player);
        l_OrderInfo.setDestination(d_Country);
        l_OrderInfo.setNumberOfArmy(0);

        DeployOrder l_DeployOrder = new DeployOrder();
        l_DeployOrder.setOrderInfo(l_OrderInfo);

        assertFalse(l_DeployOrder.validateCommand());
        assertFalse(l_DeployOrder.execute());
        assertEquals(0, d_Country.getD_Armies());
    }

    /**
     * Tests deployment with negative armies.
     * Verifies deployment fails when army count is negative.
     */
    @Test
    public void testDeployNegativeArmies() {
        OrderInfo l_OrderInfo = new OrderInfo();
        l_OrderInfo.setPlayer(d_Player);
        l_OrderInfo.setDestination(d_Country);
        l_OrderInfo.setNumberOfArmy(-5);

        DeployOrder l_DeployOrder = new DeployOrder();
        l_DeployOrder.setOrderInfo(l_OrderInfo);

        assertFalse(l_DeployOrder.validateCommand());
        assertFalse(l_DeployOrder.execute());
        assertEquals(0, d_Country.getD_Armies());
    }
}