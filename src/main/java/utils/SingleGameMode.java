package utils;

import controller.GamePlay;
import model.GameMap;
import model.Player;
import model.gamePhases.ReinforcementPhase;
import model.strategy.player.PlayerStrategy;
import model.gamePhases.StartUpPhase;
import utils.logger.LogEntryBuffer;

import java.util.*;

public class SingleGameMode {

    private final Scanner scanner = new Scanner(System.in);
    private final LogEntryBuffer logger = LogEntryBuffer.getInstance();
    private final GameMap gameMap;
    private final GameEngine gameEngine;

    public SingleGameMode() {
        gameEngine = new GameEngine();
        gameEngine.setGamePhase(new StartUpPhase(this.gameEngine));
        gameMap = GameMap.getInstance();
    }

    /**
     * Entry point for starting a single game mode using the Build 2 engine.
     * Human players interact normally, non-human players run automatically.
     */
    public void startSingleGame() {
        GamePlay gamePlayController = (GamePlay) this.gameEngine.getGamePhase().getController();
        logger.log("\n===========================================");
        logger.log("************ SINGLE GAME MODE ************");
        logger.log("===========================================");

        // Step 1: Ask for and load map
        String filename = null;
        while (true) {
            try {
                System.out.print("Enter map filename to load: ");
                filename = scanner.nextLine();
                gamePlayController.loadMap(filename);
                break;
            } catch (Exception e) {
                logger.log("Failed to load map. Please try again. Error: " + e.getMessage());
            }
        }

        // Step 2: Ask number of players
        int numPlayers = 0;
        while (true) {
            try {
                System.out.print("Enter number of players: ");
                numPlayers = Integer.parseInt(scanner.nextLine());
                if (numPlayers < 2 || numPlayers > 6) {
                    logger.log("Number of players must be between 2 and 6.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                logger.log("Invalid number. Please enter a valid integer.");
            }
        }

        for (int i = 0; i < numPlayers; i++) {
            System.out.print("Enter player " + (i + 1) + " name: ");
            String name = scanner.nextLine();

            PlayerStrategy strategy = null;
            while (strategy == null) {
                System.out.print("Enter strategy for " + name + " (human, aggressive, benevolent, random, cheater): ");
                String strategyName = scanner.nextLine().toLowerCase();
                strategy = PlayerStrategy.getStrategy(strategyName);
                if (strategy == null) {
                    logger.log("Invalid strategy. Please try again.");
                }
            }

            Player player = new Player(strategy);
            player.setD_Name(name);
            gameMap.getPlayers().put(player.getD_Name(), player);
        }

        gameMap.assignCountries();
        gameEngine.setGamePhase(new ReinforcementPhase(this.gameEngine));

        try {
            gameEngine.start();
        } catch (Exception e) {
            logger.log("Unexpected error during game start: " + e.getMessage());
        }
    }
}