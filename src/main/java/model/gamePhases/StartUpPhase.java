package model.gamePhases;

import controller.GamePlay;
import controller.MapEditor;
import model.abstractClasses.GameController;
import model.abstractClasses.GamePhase;

import java.util.List;

public class StartUpPhase extends GamePhase {

    public List<Class<? extends GamePhase>> possiblePhases() {
//      Next Phase to be implemented in next build
        return List.of(GameLoopPhase.class);
    }

    @Override
    public GameController getController() {
      return new GamePlay();
    }

}
