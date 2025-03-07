package model.gamePhases;

import controller.GameEngine;
import model.abstractClasses.GameController;
import model.abstractClasses.GamePhase;

import java.util.List;

/**
 * The type Game loop phase.
 */
public class GameLoopPhase extends GamePhase {

    @Override
    public List<Class<? extends GamePhase>> possibleNextPhases() {
        return List.of(ExitGamePhase.class);
    }

    @Override
    public GameController getController() {
        return new GameEngine();
    }

}
