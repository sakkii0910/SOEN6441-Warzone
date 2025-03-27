import utils.GameEngine;
import utils.logger.ConsoleWriter;
import utils.logger.LogEntryBuffer;
import utils.logger.LogEntryWriter;

/**
 * The type Game.
 */
public class Game {

    private final LogEntryBuffer d_Logger = LogEntryBuffer.getInstance();

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        new Game().start();
    }

    /**
     * Default Constructor
     */
    public Game() {
        d_Logger.addObserver(new LogEntryWriter());
        d_Logger.addObserver(new ConsoleWriter());
    }

    /**
     * Starts the game with Initial Phase.
     */
    public void start() {

        GameEngine d_GameEngine = new GameEngine();

        try {
            d_GameEngine.start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}











