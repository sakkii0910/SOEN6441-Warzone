package model.gamePhases;

import controller.LoadGame;
import model.abstractClasses.GameController;
import model.abstractClasses.GamePhase;
import utils.GameEngine;

/**
 * The type Load game phase.
 *
 * @author Taha Mirza
 */
public class LoadGamePhase extends GamePhase {

    /**
     * Instantiates a new Load game phase.
     *
     * @param p_GameEngine the p game engine
     */
    public LoadGamePhase(GameEngine p_GameEngine) {
        super(p_GameEngine);
    }

    @Override
    public GameController getController() {
        return new LoadGame(this.d_GameEngine);
    }

}
