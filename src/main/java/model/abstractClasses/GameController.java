package model.abstractClasses;

import model.GameMap;
import utils.GameEngine;

/**
 * The type Game controller.
 */
public abstract class GameController {

    protected GamePhase d_NextPhase;
    protected GameMap d_GameMap;
    protected GameEngine d_GameEngine;

    public GameController(GameEngine p_GameEngine) {
        d_GameMap = GameMap.getInstance();
        d_GameEngine = p_GameEngine;
    }
    /**
     * Starts the game phase.
     *
     * @throws Exception the exception
     */
    public abstract void startPhase() throws Exception;

}