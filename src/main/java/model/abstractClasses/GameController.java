package model.abstractClasses;

/**
 * The type Game controller.
 */
public abstract class GameController {

    /**
     * Starts the game phase.
     *
     * @param p_GamePhase the p game phase
     * @return the game phase
     * @throws Exception the exception
     */
    public abstract GamePhase startPhase(GamePhase p_GamePhase) throws Exception;

}