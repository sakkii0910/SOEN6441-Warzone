package model.gamePhases;

import controller.GamePlay;
import model.abstractClasses.GameController;
import model.abstractClasses.GamePhase;
import utils.GameEngine;

import java.util.List;

/**
 * The type Start up phase.
 *  @author Taha Mirza
 */
public class StartUpPhase extends GamePhase {

    /**
     * Instantiates a new Start up phase.
     *
     * @param p_GameEngine the p game engine
     */
    public StartUpPhase(GameEngine p_GameEngine) {
        super(p_GameEngine);
    }

    @Override
    public GameController getController() {
      return new GamePlay(this.d_GameEngine);
    }

}
