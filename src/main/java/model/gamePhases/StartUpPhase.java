package model.gamePhases;

import controller.GamePlay;
import model.abstractClasses.GameController;
import model.abstractClasses.GamePhase;
import utils.GameEngine;

import java.util.List;

/**
 * The type Start up phase.
 */
public class StartUpPhase extends GamePhase {

    public StartUpPhase(GameEngine p_GameEngine) {
        super(p_GameEngine);
    }

    @Override
    public GameController getController() {
      return new GamePlay(this.d_GameEngine);
    }

}
