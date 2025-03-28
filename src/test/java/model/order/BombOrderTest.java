package model.order;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.logger.LogEntryBuffer;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link BombOrder} validation and execution logic.
 * Covers edge cases like invalid targets, adjacency checks, and card requirements.
 * @author Sakshi Sudhir Mulik
 */
class BombOrderTest {
    private Player d_attacker;
    private Player d_defender;
    private Country d_attackerCountry;
    private Country d_targetCountry;
    private BombOrder d_order;
    private OrderInfo d_orderInfo;

    /**
     * Initializes test environment with players, countries, and a bomb order.
     * Resets game state and logger before each test for isolation.
     */
    @BeforeEach
    void setUp() {
        GameMap.getInstance().resetGameMap();
        LogEntryBuffer.getInstance().clear();

        d_attacker = new Player(null);
        d_attacker.setD_Name("Attacker");
        d_attacker.addCard(new Card(CardType.BOMB));

        d_defender = new Player(null);
        d_defender.setD_Name("Defender");

        d_attackerCountry = new Country();
        d_attackerCountry.setD_CountryName("AttackerLand");
        d_attackerCountry.setPlayer(d_attacker);
        d_attackerCountry.setD_Armies(5);

        d_targetCountry = new Country();
        d_targetCountry.setD_CountryName("TargetLand");
        d_targetCountry.setPlayer(d_defender);
        d_targetCountry.setD_Armies(10);

        d_attackerCountry.getD_CountryNeighbors().add(d_targetCountry);
        d_targetCountry.getD_CountryNeighbors().add(d_attackerCountry);

        d_orderInfo = new OrderInfo();
        d_orderInfo.setPlayer(d_attacker);
        d_orderInfo.setTargetCountry(d_targetCountry);
        d_orderInfo.setCommand("bomb TargetLand");

        d_order = new BombOrder();
        d_order.setOrderInfo(d_orderInfo);
    }

    /**
     * Tests bomb order execution without required bomb card.
     * Verifies order fails and target armies remain unchanged.
     */
    @Test
    void testBombWithoutCard_ShouldFail() {
        d_attacker.clearCards();
        assertFalse(d_order.execute(), "Should fail without bomb card");
        assertEquals(10, d_targetCountry.getD_Armies(), "Armies should remain unchanged");
    }

    /**
     * Tests bomb order targeting attacker's own country.
     * Verifies self-targeting is blocked and armies preserved.
     */
    @Test
    void testBombOwnCountry_ShouldFail() {
        d_targetCountry.setPlayer(d_attacker);
        assertFalse(d_order.execute());
        assertEquals(10, d_targetCountry.getD_Armies());
    }

    /**
     * Tests bomb order on non-adjacent country.
     * Validates adjacency requirement enforcement.
     */
    @Test
    void testBombNonAdjacent_ShouldFail() {
        d_attackerCountry.getD_CountryNeighbors().clear();
        assertFalse(d_order.execute());
        assertEquals(10, d_targetCountry.getD_Armies());
    }

    /**
     * Tests bomb order during truce between players.
     * Ensures neutral players cannot attack each other.
     */
    @Test
    void testBombWithTruce_ShouldFail() {
        d_attacker.addNeutralPlayers(d_defender);
        d_defender.addNeutralPlayers(d_attacker);
        assertFalse(d_order.execute());
        assertEquals(10, d_targetCountry.getD_Armies());
    }

    /**
     * Tests bomb order on country with zero armies.
     * Verifies explicit validation for empty targets.
     */
    @Test
    void testBombWithZeroArmies_ShouldFail() {
        d_targetCountry.setD_Armies(0);
        assertFalse(d_order.execute());
    }

    /**
     * Tests bomb order with null target country.
     * Validates null-check handling in order execution.
     */
    @Test
    void testBombNullTarget_ShouldFail() {
        d_orderInfo.setTargetCountry(null);
        assertFalse(d_order.execute());
    }

    /**
     * Tests bomb order with null player reference.
     * Ensures proper error handling for invalid player state.
     */
    @Test
    void testBombNullPlayer_ShouldFail() {
        d_orderInfo.setPlayer(null);
        assertFalse(d_order.execute());
    }
}