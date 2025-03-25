package model.gamePhases;

import controller.GamePlay;
import controller.Reinforcement;
import model.abstractClasses.GameController;
import model.abstractClasses.GamePhase;
import utils.GameEngine;

public class ReinforcementPhase extends GamePhase {

    public ReinforcementPhase(GameEngine p_GameEngine) {
        super(p_GameEngine);
    }

    @Override
    public GameController getController() {
        return new Reinforcement(this.d_GameEngine);
    }

}
