import model.GameMap;
import model.abstractClasses.GameController;
import model.abstractClasses.GamePhase;
import model.gamePhases.ExitGamePhase;
import model.gamePhases.MapEditorPhase;

import java.util.Objects;

/**
 * The type Phase manager.
 */
public class PhaseManager {

    private GamePhase d_GamePhase;

    /**
     * Manages different phases for game.
     * Takes the controller for current Game Phase and executes it.
     */
    public void start() {
        try {
            GameController l_GameController = d_GamePhase.getController();
            if (Objects.isNull(l_GameController)) {
                throw new Exception("No Controller found");
            }
            d_GamePhase = l_GameController.startPhase(d_GamePhase);
            GameMap.getInstance().setGamePhase(d_GamePhase);
            if (d_GamePhase instanceof ExitGamePhase) {
                System.out.println("\n==============================");
                System.out.println("    Thank you for playing!    ");
                System.out.println("      Exiting the game...     ");
                System.out.println("==============================\n");
                System.exit(0);
            } else {
                start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets game phase.
     *
     * @param p_GamePhase the p game phase
     */
    public void setGamePhase(GamePhase p_GamePhase) {
        d_GamePhase = p_GamePhase;
    }

}
