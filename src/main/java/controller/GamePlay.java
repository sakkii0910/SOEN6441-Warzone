package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import model.GameMap;
import model.abstractClasses.GameController;
import model.abstractClasses.GamePhase;
import model.gamePhases.IssueOrderPhase;
import model.gamePhases.ReinforcementPhase;
import utils.Adaptee;
import utils.Adapter;
import utils.GameEngine;
import utils.MapReader;
import utils.logger.LogEntryBuffer;

/**
 * This class implements the Game Controller and it executes the current phases
 * @author Poorav Panchal
 * @author Taha Mirza
 */
public class GamePlay extends GameController {
    private final Scanner SCANNER = new Scanner(System.in);

    private final List<String> CLI_COMMANDS = Arrays.asList("gameplayer", "assigncountries", "loadmap", "showmap");

    /**
     * Logger instance
     */
    private LogEntryBuffer d_Logger = LogEntryBuffer.getInstance();

    /**
     * Instantiates a new Game play.
     */
    public GamePlay(GameEngine p_GameEngine) {
        super(p_GameEngine);
        d_GameMap = GameMap.getInstance();
        d_NextPhase = new ReinforcementPhase(this.d_GameEngine);
    }

    @Override
    public void startPhase() throws Exception {

        boolean d_IsCountriesAssigned = false;

        d_Logger.log("\n===========================================");
        d_Logger.log("************ GAMEPLAY SETTINGS ************");
        d_Logger.log("===========================================");
        while (true) {
            int i;
            d_Logger.log("\n-----------------------------------------------------------------------------------------");
            d_Logger.log("List of Game Play Commands ");
            d_Logger.log("To add or remove a player: gameplayer -add playername -remove playername");
            d_Logger.log("To assign countries to all players: assigncountries");
            d_Logger.log("To load a domination/conquest map from the provided file: loadmap <filename>");
            d_Logger.log("To show all countries, continents, armies and ownership: showmap");
            d_Logger.log("To start the game: exit");
            d_Logger.log("-----------------------------------------------------------------------------------------");
            System.out.print("Enter command: ");
            String l_Input = SCANNER.nextLine();
            String[] l_Commands = l_Input.split(" ");

            if (l_Commands[0].equalsIgnoreCase("exit")) {
                if (d_GameMap.getPlayers().isEmpty()) {
                    d_Logger.log("There are no players in this game.");
                } else if (d_GameMap.isGameMapEmpty()) {
                    d_Logger.log("There is no map loaded.");
                } else {
                    if (!d_IsCountriesAssigned) {
                        d_GameMap.assignCountries();
                    }
                    this.d_GameEngine.setGamePhase(this.d_NextPhase);
                    this.d_GameMap.setGamePhase(this.d_NextPhase);
                    d_Logger.log("\n");
                    d_Logger.log("==========================================");
                    d_Logger.log("************** GAME STARTED **************");
                    d_Logger.log("==========================================");
                    break;
                }
            } else if (inputValidator(l_Commands)) {
                try {
                    switch (l_Commands[0].toLowerCase()) {
                        case "gameplayer":
                            i = 1;
                            while (i < l_Commands.length) {
                                switch (l_Commands[i].toLowerCase()) {
                                    case "-add":
                                        if (i + 1 < l_Commands.length) {
                                            try {
                                                d_GameMap.addPlayer(l_Commands[i + 1]);
                                            } catch (IllegalArgumentException e) {
                                                d_Logger.log(e.getMessage());
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
                                                d_Logger.log(e.getMessage());
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
                                d_Logger.log("Please load a valid map file before assigning countries.");
                            } else if (d_GameMap.getPlayers().isEmpty()) {
                                d_Logger.log("Please add players before assigning countries.");
                            } else {
                                d_GameMap.assignCountries();
                                d_IsCountriesAssigned = true;
                            }
                            break;
                        case "loadmap": // Add this case for the loadmap command
                            if (l_Commands.length == 2) {
                                d_Logger.log("Loading map file: " + l_Commands[1]); // Debug
                                loadMap(l_Commands[1]); // Call the readMap method
                                d_Logger.log("Map loaded successfully.");
                            } else {
                                d_Logger.log("Invalid command. Usage: loadmap <filename>");
                            }
                            break;
                        case "showmap":
                            d_GameMap.showMap();
                            break;
                        default:
                            break;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    d_Logger.log("Incomplete command.");
                } catch (NumberFormatException e) {
                    d_Logger.log("Invalid number format.");
                }
            } else {
                d_Logger.log("Invalid command.");
            }
        }
    }

    private void loadMap(String p_Filename) throws Exception {
        boolean l_ShouldUseConquestAdapter = p_Filename.endsWith(".conquest");

        MapReader l_MapReader = l_ShouldUseConquestAdapter ? new Adapter(new Adaptee()) : new MapReader();
        l_MapReader.readMap(d_GameMap, p_Filename);

        if (!MapReader.validateMap(d_GameMap)) {
            throw new Exception("Invalid Map");
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
