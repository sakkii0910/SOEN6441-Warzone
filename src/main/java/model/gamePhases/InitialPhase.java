package model.gamePhases;

import controller.MenuController;
import model.abstractClasses.GameController;
import model.abstractClasses.GamePhase;
import utils.GameEngine;

/**
 * The type Initial phase.
 *  @author Taha Mirza
 */
public class InitialPhase extends GamePhase {

    /**
     * Instantiates a new Initial phase.
     *
     * @param p_GameEngine the p game engine
     */
    public InitialPhase(GameEngine p_GameEngine) {
        super(p_GameEngine);
    }

    @Override
    public GameController getController() {
        return new MenuController(this.d_GameEngine);
    }
}
