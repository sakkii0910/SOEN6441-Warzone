package model.abstractClasses;

import java.util.List;

public abstract class GamePhase {

    public abstract List<Class<? extends GamePhase>> possiblePhases();

    public abstract GameController getController();

}
