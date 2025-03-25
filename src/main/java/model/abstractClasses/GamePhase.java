package model.abstractClasses;
import utils.GameEngine;

/**
 * The type Game phase.
 */
public abstract class GamePhase {

    protected GameEngine d_GameEngine;

    public GamePhase(GameEngine p_GameEngine) {
        this.d_GameEngine = p_GameEngine;
    }

    /**
     * Gets controller.
     *
     * @return the controller
     */
    public abstract GameController getController();

}
