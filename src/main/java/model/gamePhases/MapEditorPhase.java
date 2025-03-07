package model.gamePhases;

import controller.MapEditor;
import model.abstractClasses.GameController;
import model.abstractClasses.GamePhase;

import java.util.List;

/**
 * The type Map editor phase.
 */
public class MapEditorPhase extends GamePhase {

    @Override
    public List<Class<? extends GamePhase>> possibleNextPhases() {
        return List.of(InitialPhase.class);
    }

    @Override
    public GameController getController() {
        return new MapEditor();
    }
}
