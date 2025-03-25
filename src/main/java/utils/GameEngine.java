package utils;

import model.abstractClasses.GameController;
import model.abstractClasses.GamePhase;
import model.gamePhases.ExitGamePhase;
import model.gamePhases.InitialPhase;

import java.util.Objects;

/**
 * The type Game Engine.
 */
public class GameEngine {

    private GamePhase d_GamePhase;

    /**
     * Instantiates an initial state for Phase manager.
     */
    public GameEngine() {
        this.d_GamePhase = new InitialPhase(this);
    }

    /**
     * Manages different phases for game.
     * Takes the controller for current Game Phase and executes it.
     */
    public void start() {
        while (!(d_GamePhase instanceof ExitGamePhase)) {
            try {
                GameController l_GameController = d_GamePhase.getController();
                if (Objects.isNull(l_GameController)) {
                    throw new Exception("No Controller found for phase: " + d_GamePhase.getClass().getSimpleName());
                }

                // Transition to the next phase
                l_GameController.startPhase();

            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                break;
            }
        }

        // Exit message when game ends
        System.out.println("\n==============================");
        System.out.println("    Thank you for playing!    ");
        System.out.println("      Exiting the game...     ");
        System.out.println("==============================\n");
    }


    /**
     * Sets game phase.
     *
     * @param p_GamePhase the p game phase
     */
    public void setGamePhase(GamePhase p_GamePhase) {
        d_GamePhase = p_GamePhase;
    }

    public GamePhase getGamePhase() {
        return d_GamePhase;
    }
}
