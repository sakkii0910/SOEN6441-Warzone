package model.gamePhases;

import controller.ExecuteOrder;
import model.abstractClasses.GameController;
import model.abstractClasses.GamePhase;
import utils.GameEngine;

public class ExecuteOrderPhase extends GamePhase {

    public ExecuteOrderPhase(GameEngine p_GameEngine) {
        super(p_GameEngine);
    }

    @Override
    public GameController getController() {
        return new ExecuteOrder(this.d_GameEngine);
    }
}
