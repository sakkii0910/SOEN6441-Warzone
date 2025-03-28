package model.order;

import model.Country;
import model.Player;
import model.GameMap;
import model.GameSettings;
import model.strategy.game.GameStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.logger.LogEntryBuffer;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link AdvanceOrder} functionality.
 * Validates army movement between countries under different ownership scenarios.
 * @author Sakshi Sudhir Mulik
 */
public class AdvanceOrderTest {
    private Player d_Player1;
    private Player d_Player2;
    private Country d_SourceCountry;
    private Country d_TargetCountry;
    private GameMap d_GameMap;
    private GameStrategy d_OriginalStrategy;

    /**
     * Initializes test environment before each test.
     * Sets up players, countries, and resets game state.
     */
    @BeforeEach
    public void setUp() {
        d_GameMap = GameMap.getInstance();
        d_GameMap.resetGameMap();
        d_OriginalStrategy = GameSettings.getInstance().getStrategy();

        d_Player1 = new Player(null);
        d_Player1.setD_Name("Player1");
        d_Player2 = new Player(null);
        d_Player2.setD_Name("Player2");

        d_SourceCountry = new Country();
        d_SourceCountry.setD_CountryName("SourceCountry");
        d_SourceCountry.setPlayer(d_Player1);
        d_SourceCountry.setD_Armies(10);

        d_TargetCountry = new Country();
        d_TargetCountry.setD_CountryName("TargetCountry");
        d_TargetCountry.setD_Armies(5);

        d_SourceCountry.getD_CountryNeighbors().add(d_TargetCountry);
        d_TargetCountry.getD_CountryNeighbors().add(d_SourceCountry);
        d_Player1.getCapturedCountries().add(d_SourceCountry);
    }

    /**
     * Tests valid army transfer between player-owned countries.
     * Verifies correct army redistribution without ownership change.
     */
    @Test
    public void testValidAdvanceBetweenOwnedCountries() {
        d_TargetCountry.setPlayer(d_Player1);
        d_Player1.getCapturedCountries().add(d_TargetCountry);

        AdvanceOrder order = new AdvanceOrder();
        order.setOrderInfo(createOrderInfo(5));

        assertTrue(order.validateCommand());
        assertTrue(order.execute());
        assertEquals(5, d_SourceCountry.getD_Armies());
        assertEquals(10, d_TargetCountry.getD_Armies());
    }

    /**
     * Tests advancing to an unoccupied neutral territory.
     * Verifies automatic conquest and army transfer.
     */
    @Test
    public void testAdvanceToUnoccupiedCountry() {
        AdvanceOrder order = new AdvanceOrder();
        order.setOrderInfo(createOrderInfo(5));

        assertTrue(order.validateCommand());
        assertTrue(order.execute());
        assertEquals(5, d_SourceCountry.getD_Armies());
        assertEquals(10, d_TargetCountry.getD_Armies());
        assertEquals(d_Player1, d_TargetCountry.getPlayer());
        assertTrue(d_Player1.getCapturedCountries().contains(d_TargetCountry));
    }

    /**
     * Tests combat scenario when advancing to enemy territory.
     * Uses predictable attack strategy to verify conquest logic.
     */
    @Test
    public void testAdvanceToEnemyCountry() {
        d_TargetCountry.setPlayer(d_Player2);
        d_Player2.getCapturedCountries().add(d_TargetCountry);
        GameSettings.getInstance().setStrategy(new PredictableAttackStrategy());

        AdvanceOrder order = new AdvanceOrder();
        order.setOrderInfo(createOrderInfo(5));

        assertTrue(order.validateCommand());
        assertTrue(order.execute());
        assertEquals(5, d_SourceCountry.getD_Armies());
        assertEquals(d_Player1, d_TargetCountry.getPlayer());
    }

    /**
     * Tests advance order between neutral players.
     * Verifies neutral status is revoked after attack.
     */
    @Test
    public void testAdvanceToNeutralPlayer() {
        d_TargetCountry.setPlayer(d_Player2);
        d_Player1.addNeutralPlayers(d_Player2);
        d_Player2.addNeutralPlayers(d_Player1);

        AdvanceOrder order = new AdvanceOrder();
        order.setOrderInfo(createOrderInfo(5));

        assertTrue(order.validateCommand());
        assertTrue(order.execute());
        assertFalse(d_Player1.getNeutralPlayers().contains(d_Player2));
        assertFalse(d_Player2.getNeutralPlayers().contains(d_Player1));
    }

    /**
     * Tests invalid advance to non-neighboring country.
     * Verifies command validation fails.
     */
    @Test
    public void testInvalidAdvanceNonNeighbor() {
        d_SourceCountry.getD_CountryNeighbors().clear();
        d_TargetCountry.getD_CountryNeighbors().clear();

        AdvanceOrder order = new AdvanceOrder();
        order.setOrderInfo(createOrderInfo(5));

        assertFalse(order.validateCommand());
        assertFalse(order.execute());
    }

    /**
     * Tests invalid advance from non-owned source country.
     * Verifies command validation fails.
     */
    @Test
    public void testInvalidAdvanceNotOwnedSource() {
        d_SourceCountry.setPlayer(d_Player2);
        d_Player1.getCapturedCountries().remove(d_SourceCountry);

        AdvanceOrder order = new AdvanceOrder();
        order.setOrderInfo(createOrderInfo(5));

        assertFalse(order.validateCommand());
        assertFalse(order.execute());
    }

    /**
     * Tests order command printing functionality.
     * Verifies no exceptions are thrown during print operation.
     */
    @Test
    public void testPrintOrderCommand() {
        AdvanceOrder order = new AdvanceOrder();
        order.setOrderInfo(createOrderInfo(5));
        order.printOrderCommand(); // Just verify no exception
    }

    /**
     * Creates standardized OrderInfo for test cases.
     * @param armies Number of armies for the order
     * @return Configured OrderInfo object
     */
    private OrderInfo createOrderInfo(int armies) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setPlayer(d_Player1);
        orderInfo.setDeparture(d_SourceCountry);
        orderInfo.setDestination(d_TargetCountry);
        orderInfo.setNumberOfArmy(armies);
        return orderInfo;
    }

    /**
     * Predictable attack strategy for testing combat outcomes.
     * Always transfers ownership to attacker for deterministic tests.
     */
    private static class PredictableAttackStrategy implements GameStrategy {
        @Override
        public boolean attack(Player attacker, Country from, Country to, int armies) {
            from.depleteArmies(armies);
            to.setPlayer(attacker);
            attacker.getCapturedCountries().add(to);
            to.getPlayer().getCapturedCountries().remove(to);
            return true;
        }
    }
}