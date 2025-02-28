import model.GameMap;
import model.abstractClasses.GameController;
import model.abstractClasses.GamePhase;

import java.util.Objects;

public class GameEngine {

    private GamePhase d_GamePhase;

    public void start() {
        try {
            GameController l_GameController = d_GamePhase.getController();
            if (Objects.isNull(l_GameController)) {
                throw new Exception("No Controller found");
            }
            d_GamePhase = l_GameController.startPhase(d_GamePhase);
            GameMap.getInstance().setGamePhase(d_GamePhase);
            start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setGamePhase(GamePhase p_GamePhase) {
        d_GamePhase = p_GamePhase;
    }

}
