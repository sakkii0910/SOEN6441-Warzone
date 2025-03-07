package model.gamePhases;

import controller.MapEditor;
import controller.MenuController;
import model.abstractClasses.GameController;
import model.abstractClasses.GamePhase;

import java.util.List;

/**
 * The type Initial phase.
 */
public class InitialPhase extends GamePhase {
    @Override
    public List<Class<? extends GamePhase>> possibleNextPhases() {
        return List.of(MapEditorPhase.class, StartUpPhase.class);
    }

    @Override
    public GameController getController() {
        return new MenuController();
    }
}
