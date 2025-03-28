package model.order;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.logger.LogEntryBuffer;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link AirliftOrder} functionality.
 * Validates army transfers, card usage, and error handling for airlift operations.
 * @author Sakshi Sudhir Mulik
 */
class AirliftOrderTest {
    private Player d_player;
    private Country d_sourceCountry;
    private Country d_targetCountry;
    private AirliftOrder d_order;
    private OrderInfo d_orderInfo;

    /**
     * Initializes test environment before each test case.
     * Sets up player, countries, and a default valid airlift order.
     */
    @BeforeEach
    void setUp() {
        GameMap.getInstance().resetGameMap();

        d_player = new Player(null);
        d_player.setD_Name("TestPlayer");
        d_player.addCard(new Card(CardType.AIRLIFT));

        d_sourceCountry = new Country();
        d_sourceCountry.setD_CountryName("SourceLand");
        d_sourceCountry.setPlayer(d_player);
        d_sourceCountry.setD_Armies(5);

        d_targetCountry = new Country();
        d_targetCountry.setD_CountryName("TargetLand");
        d_targetCountry.setPlayer(d_player);
        d_targetCountry.setD_Armies(2);

        d_orderInfo = new OrderInfo();
        d_orderInfo.setPlayer(d_player);
        d_orderInfo.setDeparture(d_sourceCountry);
        d_orderInfo.setDestination(d_targetCountry);
        d_orderInfo.setNumberOfArmy(3);

        d_order = new AirliftOrder();
        d_order.setOrderInfo(d_orderInfo);
    }

    /**
     * Tests airlift failure when player lacks the required airlift card.
     * Verifies armies remain unchanged when card is unavailable.
     */
    @Test
    void testAirliftWithoutCard_ShouldFail() {
        d_player.clearCards();
        assertFalse(d_order.validateCommand());
        assertFalse(d_order.execute());
        assertEquals(5, d_sourceCountry.getD_Armies());
        assertEquals(2, d_targetCountry.getD_Armies());
    }

    /**
     * Tests airlift failure when targeting enemy territory.
     * Ensures armies cannot be airlifted to opponent-owned countries.
     */
    @Test
    void testAirliftToEnemyTerritory_ShouldFail() {
        Player enemy = new Player(null);
        d_targetCountry.setPlayer(enemy);
        assertFalse(d_order.validateCommand());
        assertFalse(d_order.execute());
    }

    /**
     * Tests airlift failure due to insufficient armies at source.
     * Verifies validation rejects transfers exceeding available armies.
     */
    @Test
    void testAirliftWithInsufficientArmies_ShouldFail() {
        d_orderInfo.setNumberOfArmy(10);
        assertFalse(d_order.validateCommand());
        assertFalse(d_order.execute());
        assertEquals(5, d_sourceCountry.getD_Armies());
    }

    /**
     * Tests airlift failure with zero army transfer.
     * Validates rejection of non-positive army values.
     */
    @Test
    void testAirliftWithZeroArmies_ShouldFail() {
        d_orderInfo.setNumberOfArmy(0);
        assertFalse(d_order.validateCommand());
        assertFalse(d_order.execute());
    }

    /**
     * Tests airlift failure with negative army count.
     * Ensures validation rejects invalid negative values.
     */
    @Test
    void testAirliftWithNegativeArmies_ShouldFail() {
        d_orderInfo.setNumberOfArmy(-2);
        assertFalse(d_order.validateCommand());
        assertFalse(d_order.execute());
    }

    /**
     * Tests airlift failure with null source country.
     * Verifies proper handling of missing departure country.
     */
    @Test
    void testAirliftFromNullCountry_ShouldFail() {
        d_orderInfo.setDeparture(null);
        assertFalse(d_order.validateCommand());
        assertFalse(d_order.execute());
    }

    /**
     * Tests airlift failure with null destination country.
     * Validates rejection of missing target country.
     */
    @Test
    void testAirliftToNullCountry_ShouldFail() {
        d_orderInfo.setDestination(null);
        assertFalse(d_order.validateCommand());
        assertFalse(d_order.execute());
    }

    /**
     * Tests airlift failure with null player.
     * Ensures validation rejects orders without an issuing player.
     */
    @Test
    void testAirliftWithNullPlayer_ShouldFail() {
        d_orderInfo.setPlayer(null);
        assertFalse(d_order.validateCommand());
        assertFalse(d_order.execute());
    }
}