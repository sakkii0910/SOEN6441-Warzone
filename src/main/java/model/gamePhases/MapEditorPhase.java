package model.gamePhases;

import controller.MapEditor;
import model.abstractClasses.GameController;
import model.abstractClasses.GamePhase;

import java.util.List;

public class MapEditorPhase extends GamePhase {

    @Override
    public List<Class<? extends GamePhase>> possiblePhases() {
        return List.of(StartUpPhase.class);
    }

    @Override
    public GameController getController() {
        return new MapEditor();
    }
}
