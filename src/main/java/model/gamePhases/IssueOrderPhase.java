package model.gamePhases;

import controller.IssueOrder;
import model.abstractClasses.GameController;
import model.abstractClasses.GamePhase;
import utils.GameEngine;

/**
 * The type Issue order phase.
 *  @author Taha Mirza
 *  @author  Shariq Anwar
 */
public class IssueOrderPhase extends GamePhase {

    /**
     * Instantiates a new Issue order phase.
     *
     * @param p_GameEngine the p game engine
     */
    public IssueOrderPhase(GameEngine p_GameEngine) {
        super(p_GameEngine);
    }

    @Override
    public GameController getController() {
        return new IssueOrder(this.d_GameEngine);
    }

}
