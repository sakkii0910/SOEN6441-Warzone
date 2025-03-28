package model.gamePhases;

import controller.GamePlay;
import controller.Reinforcement;
import model.abstractClasses.GameController;
import model.abstractClasses.GamePhase;
import utils.GameEngine;

/**
 * The type Reinforcement phase.
 *  @author Taha Mirza
 *  * @author  Shariq Anwar
 */
public class ReinforcementPhase extends GamePhase {

    /**
     * Instantiates a new Reinforcement phase.
     *
     * @param p_GameEngine the p game engine
     */
    public ReinforcementPhase(GameEngine p_GameEngine) {
        super(p_GameEngine);
    }

    @Override
    public GameController getController() {
        return new Reinforcement(this.d_GameEngine);
    }

}
