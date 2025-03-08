package model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for Player class
 */
public class PlayerTest {
    private Player player;

    @Before
    public void setUp() {
        player = new Player();
        player.setD_Name("TestPlayer");
    }

    /**
     * Test reinforcement calculation
     */
    @Test
    public void testAssignReinforcements() {
        player.assignReinforcements(5);
        assertEquals(5, player.getReinforcementArmies());

        player.assignReinforcements(3);
        assertEquals(8, player.getReinforcementArmies());
    }

    /**
     * Test that player cannot deploy more armies than available reinforcements
     */
    @Test
    public void testDeployMoreThanReinforcements() {
        player.assignReinforcements(5);

        DeployOrder validOrder = new DeployOrder(player, "CountryA", 5);
        assertEquals(5, player.getReinforcementArmies());
        validOrder.execute();
        assertEquals(0, player.getReinforcementArmies()); // Reinforcement should be 0 after valid deploy

        // Trying to deploy 6 armies when only 5 were available should not work
        DeployOrder invalidOrder = new DeployOrder(player, "CountryA", 6);
        invalidOrder.execute();
        assertEquals(0, player.getReinforcementArmies()); // Count should remain 0, as invalid deploy should not execute
    }
}
