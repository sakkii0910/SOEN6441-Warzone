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
 * Test class for {@link IssueOrder} to validate command parsing and order issuance.
 * Covers deployment validation, invalid command handling, and turn management.
 * @author Sakshi Sudhir Mulik
 */
class IssueOrderTest {
    private IssueOrder d_issueOrder;
    private GameMap d_gameMap;
    private Player d_player;
    private Country d_country;

    /**
     * Initializes test environment before each test case.
     * Sets up a fresh game map, player, and country for isolated testing.
     */
    @BeforeEach
    void setUp() {
        // Minimal test setup
        d_gameMap = GameMap.getInstance();
        d_gameMap.resetGameMap();

        d_player = new Player(null);
        d_player.setD_Name("TestPlayer");
        d_player.assignReinforcements(5); // Give some armies

        d_country = new Country();
        d_country.setD_CountryName("TestCountry");
        d_country.setPlayer(d_player);
        d_player.getCapturedCountries().add(d_country);

        d_issueOrder = new IssueOrder(new GameEngine());
    }

    /**
     * Tests valid deployment order creation and queueing.
     * Verifies command parsing and DeployOrder instance generation.
     * @throws Exception if order issuance fails
     */
    @Test
    void testDeployOrderCreation() throws Exception {
        // Setup test command
        IssueOrder.Commands = "deploy TestCountry 3";

        // Execute
        boolean isValid = d_issueOrder.validateCommand(IssueOrder.Commands, d_player);
        d_player.issueOrder();

        // Verify
        assertTrue(isValid);
        assertEquals(1, d_player.getOrders().size());
        assertTrue(d_player.getOrders().peek() instanceof DeployOrder);
    }

    /**
     * Tests rejection of malformed or invalid commands.
     * Validates error handling for syntax errors and negative army values.
     */
    @Test
    void testInvalidCommandRejection() {
        // Test wrong command format
        IssueOrder.Commands = "deploy invalid command";
        assertFalse(d_issueOrder.validateCommand(IssueOrder.Commands, d_player));

        // Test negative army count
        IssueOrder.Commands = "deploy TestCountry -1";
        assertFalse(d_issueOrder.validateCommand(IssueOrder.Commands, d_player));
    }

    /**
     * Tests player turn completion via "pass" command.
     * Ensures the player's turn flag is correctly updated.
     * @throws Exception if command validation fails
     */
    @Test
    void testPlayerTurnCompletion() throws Exception {
        // Setup
        IssueOrder.Commands = "pass";
        d_issueOrder.validateCommand(IssueOrder.Commands, d_player);

        // Verify
        assertTrue(d_player.isD_TurnCompleted());
    }

    /**
     * Tests command length validation for different order types.
     * Verifies parameter count checks for deploy/advance commands.
     */
    @Test
    void testCommandLengthValidation() {
        // Valid deploy command
        assertTrue(d_issueOrder.checkCommandLength("deploy", 3));

        // Invalid deploy command
        assertFalse(d_issueOrder.checkCommandLength("deploy", 2));

        // Valid advance command
        assertTrue(d_issueOrder.checkCommandLength("advance", 4));
    }
}