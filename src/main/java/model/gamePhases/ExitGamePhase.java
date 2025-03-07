package model.gamePhases;

import model.abstractClasses.GameController;
import model.abstractClasses.GamePhase;

import java.util.List;

/**
 * The type Exit game phase.
 */
public class ExitGamePhase extends GamePhase {

    @Override
    public List<Class<? extends GamePhase>> possibleNextPhases() {
        return null;
    }

    @Override
    public GameController getController() {
        return null;
    }

}
