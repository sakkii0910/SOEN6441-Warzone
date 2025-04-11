package model.abstractClasses;
import utils.GameEngine;

import java.io.Serializable;

/**
 * The type Game phase.
 *  @author Taha Mirza
 */
public abstract class GamePhase implements Serializable {

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
