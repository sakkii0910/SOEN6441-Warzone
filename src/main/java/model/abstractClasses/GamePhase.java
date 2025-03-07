package model.abstractClasses;

import java.util.List;

/**
 * The type Game phase.
 */
public abstract class GamePhase {

    /**
     * Possible next phases list.
     *
     * @return the list
     */
    public abstract List<Class<? extends GamePhase>> possibleNextPhases();

    /**
     * Gets controller.
     *
     * @return the controller
     */
    public abstract GameController getController();

}
