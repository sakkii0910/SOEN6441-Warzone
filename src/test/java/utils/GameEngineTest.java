package utils;

import model.GameSettings;
import model.abstractClasses.GameController;
import model.abstractClasses.GamePhase;
import model.gamePhases.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link GameEngine} phase management and transitions.
 * Verifies engine state, phase transitions, controller bindings and settings.
 *
 * @author Sakshi Sudhir Mulik
 */
class GameEngineTest {
    private GameEngine d_gameEngine;

    /**
     * Initializes test environment with mocked GameEngine.
     * Skips actual game loop to focus on phase testing.
     */
    @BeforeEach
    void setUp() {
        d_gameEngine = new GameEngine() {
            @Override
            public void start() {
                setGamePhase(new InitialPhase(this));
            }
        };
    }

    /**
     * Tests engine initial state.
     * Verifies game starts in InitialPhase by default.
     */
    @Test
    void testInitialState_ShouldBeInitialPhase() {
        assertTrue(d_gameEngine.getGamePhase() instanceof InitialPhase);
    }

    /**
     * Tests phase transition functionality.
     * Verifies engine correctly switches between game phases.
     */
    @Test
    void testPhaseTransition_ShouldChangeCurrentPhase() {
        GamePhase l_newPhase = new ReinforcementPhase(d_gameEngine);
        d_gameEngine.setGamePhase(l_newPhase);
        assertSame(l_newPhase, d_gameEngine.getGamePhase());
    }

    /**
     * Tests controller binding in initial phase.
     * Verifies InitialPhase returns proper MenuController instance.
     */
    @Test
    void testPhaseControllerBinding_InitialPhaseReturnsMenuController() {
        GameController l_controller = d_gameEngine.getGamePhase().getController();
        assertNotNull(l_controller);
        assertEquals("MenuController", l_controller.getClass().getSimpleName());
    }

    /**
     * Tests exit phase behavior.
     * Verifies ExitGamePhase has no controller binding.
     */
    @Test
    void testExitPhase_ShouldHaveNullController() {
        GamePhase l_exitPhase = new ExitGamePhase(d_gameEngine);
        assertNull(l_exitPhase.getController());
    }

    /**
     * Tests default game settings strategy.
     * Verifies engine uses default strategy when no strategy is specified.
     */
    @Test
    void testDefaultGameSettings_ShouldUseDefaultStrategy() {
        GameSettings l_settings = GameSettings.getInstance();
        l_settings.setStrategy("default");

        assertEquals("defaultstrategy",
                l_settings.getStrategy()
                        .getClass().getSimpleName().toLowerCase());
    }
}