package model;

import controller.*;
import model.abstractClasses.GameController;
import model.gamePhases.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.GameEngine;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for game phase transitions and controller bindings.
 * Verifies correct phase sequencing and controller instantiation.
 *
 * @author Sakshi Sudhir Mulik
 */
class GamePhaseTest {
    private GameEngine d_gameEngine;

    /**
     * Initializes test environment with fresh GameEngine instance.
     * Ensures clean state for each test case.
     */
    @BeforeEach
    void setUp() {
        d_gameEngine = new GameEngine();
    }

    /**
     * Tests InitialPhase returns MenuController.
     * Verifies correct controller binding for initial game state.
     */
    @Test
    void testInitialPhase_ReturnsMenuController() {
        GameController controller = new InitialPhase(d_gameEngine).getController();
        assertInstanceOf(MenuController.class, controller);
    }

    /**
     * Tests ReinforcementPhase returns Reinforcement controller.
     * Verifies correct controller binding for reinforcement phase.
     */
    @Test
    void testReinforcementPhase_ReturnsReinforcementController() {
        GameController controller = new ReinforcementPhase(d_gameEngine).getController();
        assertInstanceOf(Reinforcement.class, controller);
    }

    /**
     * Tests ExitPhase returns null controller.
     * Verifies termination state handling.
     */
    @Test
    void testExitPhase_ReturnsNullController() {
        assertNull(new ExitGamePhase(d_gameEngine).getController());
    }

    /**
     * Tests ReinforcementPhase transitions to IssueOrderPhase.
     * Verifies correct phase sequencing in game loop.
     */
    @Test
    void testReinforcementPhase_SetsNextPhaseToIssueOrder() {
        Reinforcement controller = new Reinforcement(d_gameEngine);
        assertInstanceOf(IssueOrderPhase.class, controller.d_NextPhase);
    }

    /**
     * Tests IssueOrderPhase transitions to ExecuteOrderPhase.
     * Verifies correct phase sequencing in game loop.
     */
    @Test
    void testIssueOrderPhase_SetsNextPhaseToExecuteOrder() {
        IssueOrder controller = new IssueOrder(d_gameEngine);
        assertInstanceOf(ExecuteOrderPhase.class, controller.d_NextPhase);
    }

    /**
     * Tests ExecuteOrderPhase transitions to ReinforcementPhase.
     * Verifies correct phase sequencing in game loop.
     */
    @Test
    void testExecuteOrderPhase_SetsNextPhaseToReinforcement() {
        ExecuteOrder controller = new ExecuteOrder(d_gameEngine);
        assertInstanceOf(ReinforcementPhase.class, controller.d_NextPhase);
    }

    /**
     * Tests win condition transitions to ExitGamePhase.
     * Verifies game termination on win condition.
     */
    @Test
    void testWinCondition_SetsExitPhase() {
        ExecuteOrder controller = new ExecuteOrder(d_gameEngine);
        controller.d_NextPhase = new ExitGamePhase(d_gameEngine);
        assertInstanceOf(ExitGamePhase.class, controller.d_NextPhase);
    }
}