import utils.GameEngine;

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

        GameEngine d_GameEngine = new GameEngine();

        try {
            d_GameEngine.start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}











