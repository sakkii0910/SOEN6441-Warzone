package model.gamePhases;

import controller.GameEngine;
import model.abstractClasses.GameController;
import model.abstractClasses.GamePhase;

import java.util.List;

public class GameLoopPhase extends GamePhase {

    @Override
    public List<Class<? extends GamePhase>> possiblePhases() {
        return List.of(ExitGamePhase.class);
    }

    @Override
    public GameController getController() {
        return new GameEngine();
    }

}
