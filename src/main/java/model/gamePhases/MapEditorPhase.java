package model.gamePhases;

import controller.MapEditor;
import model.abstractClasses.GameController;
import model.abstractClasses.GamePhase;
import utils.GameEngine;

import java.util.List;

/**
 * The type Map editor phase.
 */
public class MapEditorPhase extends GamePhase {

    public MapEditorPhase(GameEngine p_GameEngine) {
        super(p_GameEngine);
    }

    @Override
    public GameController getController() {
        return new MapEditor(this.d_GameEngine);
    }
}
