package model.gamePhases;

import controller.IssueOrder;
import model.abstractClasses.GameController;
import model.abstractClasses.GamePhase;
import utils.GameEngine;

public class IssueOrderPhase extends GamePhase {

    public IssueOrderPhase(GameEngine p_GameEngine) {
        super(p_GameEngine);
    }

    @Override
    public GameController getController() {
        return new IssueOrder(this.d_GameEngine);
    }

}
