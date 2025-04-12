
package utils;

import model.Country;
import model.GameMap;
import model.Player;
import model.gamePhases.ExitGamePhase;
import model.gamePhases.ReinforcementPhase;
import model.strategy.player.PlayerStrategy;
import utils.logger.LogEntryBuffer;

import java.util.*;

/**
 * The type Tournament mode.
 *
 * @author Taha Mirza
 * @author Pruthvirajsinh Dodiya
 */
public class TournamentMode {

    private final Scanner scanner = new Scanner(System.in);
    private final LogEntryBuffer logger = LogEntryBuffer.getInstance();

    /**
     * Start tournament mode.
     */
    public void startTournamentMode() {
        logger.log("\n===========================================");
        logger.log("************ TOURNAMENT GAME MODE ************");
        logger.log("===========================================");


        System.out.print("Enter comma-separated map filenames (max 5): ");
        List<String> mapFiles = Arrays.stream(scanner.nextLine().split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();

        System.out.print("Enter comma-separated player strategies (aggressive,benevolent,random,cheater only, max 4): ");
        List<String> strategyNames = Arrays.stream(scanner.nextLine().split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();

        System.out.print("Enter number of games per map: ");
        int numGames = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter max number of turns per game: ");
        int maxTurns = Integer.parseInt(scanner.nextLine());

        logger.log("\n====== Tournament Setup ======");
        logger.log("Maps: " + mapFiles);
        logger.log("Strategies: " + strategyNames);
        logger.log("Games per map: " + numGames);
        logger.log("Max turns per game: " + maxTurns);
        logger.log("==============================\n");

        Map<String, List<String>> results = new LinkedHashMap<>();

        for (String mapFile : mapFiles) {
            List<String> gameResults = new ArrayList<>();

            for (int gameNum = 1; gameNum <= numGames; gameNum++) {
                logger.log("Playing Map: " + mapFile + " | Game: " + gameNum);

                GameMap gameMap = GameMap.getInstance();
                gameMap.resetGameMap();
                gameMap.flushGameMap();
                try {
                    new MapReader().readMap(gameMap, mapFile);
                     for (Country country : gameMap.getCountries().values()) {
        country.setPlayer(null);
    }
                } catch (Exception e) {
                    logger.log("Map load failed for " + mapFile);
                    gameResults.add("InvalidMap");
                    break; // Skip rest of the games for this map
                }

                gameMap.getPlayers().clear();
                Map<String, Integer> nameCounts = new HashMap<>();

                for (String strategyName : strategyNames) {
                    PlayerStrategy strategy = PlayerStrategy.getStrategy(strategyName);
                    if (strategy != null) {
                        nameCounts.put(strategyName, nameCounts.getOrDefault(strategyName, 0) + 1);
                        int count = nameCounts.get(strategyName);
                        String playerName = count > 1 ? strategyName + count : strategyName;
                        Player player = new Player(strategy);
                        player.setD_Name(playerName);
                        gameMap.getPlayers().put(playerName, player);
                    }
                }

                gameMap.assignCountries();

                GameEngine engine = new GameEngine();
                boolean winnerFound = false;
                int turnCount = 0;

                while (turnCount < maxTurns) {
                    turnCount++;
                    engine.setGamePhase(new ReinforcementPhase(engine));
                    engine.start();
                    if (isGameOver(gameMap)) {
                        winnerFound = true;
                        break;
                    }
                }

                engine.setGamePhase(new ExitGamePhase(engine));
                engine.start();

                Player winner = determineWinner(gameMap);
                if (winnerFound && winner != null) {
                    logger.log("Winner: " + winner.getD_Name());
                    gameResults.add(winner.getD_Name());
                } else {
                    logger.log("Game Drawn (No winner in " + maxTurns + " turns)");
                    gameResults.add("Draw");
                }
                logger.log("==============================================");
            }

            results.put(mapFile, gameResults);
        }

        logger.log("\n=========== TOURNAMENT RESULT ===========");
        System.out.println("+--------------+" + "------------".repeat(numGames) + "+");
        System.out.printf("| %-12s |", "Map/Game");
        for (int i = 1; i <= numGames; i++) {
            System.out.printf(" %-10s|", "Game" + i);
        }
        System.out.println();
        System.out.println("+--------------+" + "------------".repeat(numGames) + "+");

        for (Map.Entry<String, List<String>> entry : results.entrySet()) {
            System.out.printf("| %-12s |", entry.getKey());
            for (String result : entry.getValue()) {
                System.out.printf(" %-10s|", result);
            }
            System.out.println();
        }
        System.out.println("+--------------+" + "------------".repeat(numGames) + "+");
        logger.log("=========================================");
    }

    private boolean isGameOver(GameMap gameMap) {
        long playersWithCountries = gameMap.getPlayers().values().stream()
                .filter(p -> !p.getCapturedCountries().isEmpty())
                .count();
        return playersWithCountries <= 1;
    }

    private Player determineWinner(GameMap gameMap) {
        return gameMap.getPlayers().values().stream()
                .max(Comparator.comparingInt(p -> p.getCapturedCountries().size()))
                .orElse(null);
    }
}
