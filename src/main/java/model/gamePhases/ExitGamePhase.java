package model.gamePhases;

import model.abstractClasses.GameController;
import model.abstractClasses.GamePhase;
import utils.GameEngine;

import java.util.List;

/**
 * The type Exit game phase.
 */
public class ExitGamePhase extends GamePhase {

    /**
     * Instantiates a new Exit game phase.
     *
     * @param p_GameEngine the p game engine
     */
    public ExitGamePhase(GameEngine p_GameEngine) {
        super(p_GameEngine);
    }

    @Override
    public GameController getController() {
        return null;
    }

}
