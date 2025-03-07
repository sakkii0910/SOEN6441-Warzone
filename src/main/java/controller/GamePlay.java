package controller;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import model.GameMap;
import model.abstractClasses.GameController;
import model.abstractClasses.GamePhase;
import model.gamePhases.GameLoopPhase;
import utils.MapReader;

/**
 * This class implements the Game Controller and it executes the current phases
 */
public class GamePlay extends GameController {
    private final Scanner SCANNER = new Scanner(System.in);

    private final List<String> CLI_COMMANDS = Arrays.asList("gameplayer", "assigncountries", "help", "loadmap", "showmap");

    private final GameMap d_GameMap;

    private final GamePhase d_NextPhase = new GameLoopPhase();

    /**
     * Instantiates a new Game play.
     */
    public GamePlay() {
        d_GameMap = GameMap.getInstance();
    }

    @Override
    public GamePhase startPhase(GamePhase p_GamePhase) throws Exception {

        boolean d_IsCountriesAssigned = false;

        System.out.println();
        System.out.println("=========================================");
        System.out.println("\t****** GAME PLAY MODE ******\t");
        while (true) {
            int i;
            System.out.print("Enter command (\"help\" for all commands): ");
            String l_Input = SCANNER.nextLine();
            String[] l_Commands = l_Input.split(" ");

            if (l_Commands[0].equalsIgnoreCase("exit")) {
                if (d_GameMap.getPlayers().isEmpty()) {
                    System.out.println("There are no players in this game.");
                } else if (d_GameMap.isGameMapEmpty()) {
                    System.out.println("There is no map loaded.");
                } else {
                    if (!d_IsCountriesAssigned) {
                        d_GameMap.assignCountries();
                    }
                    d_GameMap.setGamePhase(d_NextPhase);
                    return d_NextPhase;
                }
            } else if (inputValidator(l_Commands)) {
                try {
                    switch (l_Commands[0].toLowerCase()) {
                        case "help":
                            System.out.println("=========================================");
                            System.out.println(" List of Game Play Commands ");
                            System.out.println("=========================================");
                            System.out.println("\nTo add or remove a player:");
                            System.out.println("  gameplayer -add playername -remove playername");
                            System.out.println("\nTo assign countries to all players: ");
                            System.out.println("  assigncountries");
                            System.out.println("\nTo load a domination map from the provided file: ");
                            System.out.println(" loadmap <filename>");
                            System.out.println("\nTo show all countries, continents, armies and ownership: ");
                            System.out.println(" showmap");
                            System.out.println("=========================================");
                            break;
                        case "gameplayer":
                            i = 1;
                            while (i < l_Commands.length) {
                                switch (l_Commands[i].toLowerCase()) {
                                    case "-add":
                                        if (i + 1 < l_Commands.length) {
                                            try {
                                                d_GameMap.addPlayer(l_Commands[i + 1]);
                                            } catch (IllegalArgumentException e) {
                                                System.out.println(e.getMessage());
                                            }
                                            i += 2;
                                        } else {
                                            throw new ArrayIndexOutOfBoundsException();
                                        }
                                        break;
                                    case "-remove":
                                        if (i + 1 < l_Commands.length) {
                                            try {
                                                d_GameMap.removePlayer(l_Commands[i + 1]);
                                            } catch (IllegalArgumentException e) {
                                                System.out.println(e.getMessage());
                                            }
                                            i += 2;
                                        } else {
                                            throw new ArrayIndexOutOfBoundsException();
                                        }
                                        break;
                                    default:
                                        i++;
                                }
                            }
                            break;
                        case "assigncountries":
                            if (d_GameMap.isGameMapEmpty()) {
                                System.out.println("Please load a valid map file before assigning countries.");
                            } else if (d_GameMap.getPlayers().isEmpty()) {
                                System.out.println("Please add players before assigning countries.");
                            } else {
                                d_GameMap.assignCountries();
                                d_IsCountriesAssigned = true;
                            }
                            break;
                        case "loadmap": // Add this case for the loadmap command
                            if (l_Commands.length == 2) {
                                System.out.println("Loading map file: " + l_Commands[1]); // Debug
                                MapReader.readMap(d_GameMap, l_Commands[1]); // Call the readMap method
                                System.out.println("Map loaded successfully.");
                            } else {
                                System.out.println("Invalid command. Usage: loadmap <filename>");
                            }
                            break;
                        case "showmap":
                            d_GameMap.showMap();
                            break;
                        default:
                            break;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Incomplete command.");
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format.");
                }
            } else {
                System.out.println("Invalid command.");
            }
        }
    }

    /**
     * Input validator boolean.
     *
     * @param p_InputCommands the p input commands
     * @return the boolean
     */
    public boolean inputValidator(String[] p_InputCommands) {
        if (p_InputCommands.length > 0) {
            String l_MainCommand = p_InputCommands[0];
            return CLI_COMMANDS.contains(l_MainCommand.toLowerCase());
        }
        return false;
    }

}
