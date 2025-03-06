package model.gamePhases;

import controller.GameEngine;
import model.abstractClasses.GameController;
import model.abstractClasses.GamePhase;

import java.util.List;

public class ExitGamePhase extends GamePhase {

    @Override
    public List<Class<? extends GamePhase>> possiblePhases() {
        return null;
    }

    @Override
    public GameController getController() {
        return null;
    }

}
