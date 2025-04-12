package utils;

import model.GameSettings;
import model.abstractClasses.GameController;
import model.abstractClasses.GamePhase;
import model.gamePhases.ExitGamePhase;
import model.gamePhases.InitialPhase;
import utils.logger.LogEntryBuffer;

import java.io.Serializable;
import java.util.Objects;

/**
 * The type Game Engine.
 * @author Taha Mirza
 */
public class GameEngine implements Serializable {

    private GamePhase d_GamePhase;
    private static GameSettings d_GameSettings;

    /**
     * Logger instance
     */
    private LogEntryBuffer d_Logger = LogEntryBuffer.getInstance();

    /**
     * Instantiates an initial state for Phase manager.
     */
    public GameEngine() {
        this.d_GamePhase = new InitialPhase(this);
        d_GameSettings = GameSettings.getInstance();
        d_GameSettings.setStrategy("default");
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
        d_Logger.log("\n==============================");
        d_Logger.log("    Thank you for playing!    ");
        d_Logger.log("      Exiting the game...     ");
        d_Logger.log("==============================\n");
        System.exit(0);
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
