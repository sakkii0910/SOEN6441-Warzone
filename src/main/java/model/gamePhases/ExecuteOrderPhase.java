package model.gamePhases;

import controller.ExecuteOrder;
import model.abstractClasses.GameController;
import model.abstractClasses.GamePhase;
import utils.GameEngine;

/**
 * The type Execute order phase.
 */
public class ExecuteOrderPhase extends GamePhase {

    /**
     * Instantiates a new Execute order phase.
     *
     * @param p_GameEngine the p game engine
     */
    public ExecuteOrderPhase(GameEngine p_GameEngine) {
        super(p_GameEngine);
    }

    @Override
    public GameController getController() {
        return new ExecuteOrder(this.d_GameEngine);
    }
}
