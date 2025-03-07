import model.abstractClasses.GamePhase;
import model.gamePhases.InitialPhase;

/**
 * The type Game.
 */
public class Game {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        start();
    }

    /**
     * Starts the game with Initial Phase.
     */
    public static void start() {

        GamePhase d_GamePhase = new InitialPhase();
        PhaseManager d_PhaseManager = new PhaseManager();
        d_PhaseManager.setGamePhase(d_GamePhase);

        try {
            d_PhaseManager.start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}











