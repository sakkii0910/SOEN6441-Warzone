package controller;

import model.abstractClasses.GameController;
import model.GameMap;
import model.abstractClasses.GamePhase;
import model.gamePhases.InitialPhase;
import utils.GameEngine;
import utils.MapReader;
import utils.logger.LogEntryBuffer;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Map Editor Class to handle all maps related command.
 *   @author Taha Mirza
 *   @author Poorav Panchal
 *   @author  Shariq Anwar
 * @author Sakshi Sudhir Mulik
 */
public class MapEditor extends GameController {

    private final Scanner SCANNER = new Scanner(System.in);
    private final List<String> CLI_COMMANDS = Arrays.asList("editcontinent", "editcountry", "editneighbor", "showmap",
            "savemap", "editmap", "validatemap", "help", "exit");
    
    /**
     * Logger instance
     */
    private LogEntryBuffer d_Logger = LogEntryBuffer.getInstance();

    /**
     * Instantiates a new Map editor using singleton design pattern.
     */
    public MapEditor(GameEngine p_GameEngine) {
        super(p_GameEngine);
        d_GameMap = GameMap.getInstance();
        d_NextPhase = new InitialPhase(this.d_GameEngine);
    }

    /**
     * Starts the phase for Map Editing, allowing user to execute commands.
     *
     * @throws Exception
     */
    public void startPhase() throws Exception {
        d_Logger.log("\n");
        d_Logger.log("=========================================");
        d_Logger.log("\t****** MAP EDITING MODE ******\t");
        while (true) {
            int i;
            System.out.print("Enter command (\"help\" for all commands): ");

            // Splitting commands in a String array.
            String l_Input = SCANNER.nextLine();
            String[] l_Commands = l_Input.split(" ");

            if (l_Commands[0].equalsIgnoreCase("exit")) {

                // Returns the next phase when user exits map phase.
                this.d_GameEngine.setGamePhase(this.d_NextPhase);
                this.d_GameMap.setGamePhase(this.d_NextPhase);
                break;

            } else if (inputValidator(l_Commands)) {
                try {
                    switch (l_Commands[0].toLowerCase()) {
                        case "help":
                            d_Logger.log("=========================================");
                            d_Logger.log(" List of User Map Creation Commands ");
                            d_Logger.log("=========================================");
                            d_Logger.log("\nTo add or remove a continent:");
                            d_Logger.log(
                                    "  editcontinent -add <continentID> <continentValue> -remove <continentID>");

                            d_Logger.log("\nTo add or remove a country:");
                            d_Logger.log("  editcountry -add <countryID> <continentID> -remove <countryID>");

                            d_Logger.log("\nTo add or remove a neighbor to a country:");
                            d_Logger.log(
                                    "  editneighbor -add <countryID> <neighborCountryID> -remove <countryID> <neighborCountryID>");

                            d_Logger.log("\n-----------------------------------------");
                            d_Logger.log(" Map Commands (Edit/Save) ");
                            d_Logger.log("-----------------------------------------");

                            d_Logger.log("To edit a map:  editmap <filename>");
                            d_Logger.log("To save a map:  savemap <filename>");

                            d_Logger.log("\n-----------------------------------------");
                            d_Logger.log(" Additional Map Commands ");
                            d_Logger.log("-----------------------------------------");

                            d_Logger.log("To show the map:      showmap");
                            d_Logger.log("To validate the map:  validatemap");

                            d_Logger.log("=========================================");
                            break;
                        case "editcontinent":
                            i = 1;
                            while (i < l_Commands.length) {
                                switch (l_Commands[i].toLowerCase()) {
                                    case "-add":
                                        if (i + 2 < l_Commands.length) {
                                            try {
                                                d_GameMap.addContinent(l_Commands[i + 1],
                                                        Integer.parseInt(l_Commands[i + 2]));
                                            } catch (IllegalArgumentException e) {
                                                d_Logger.log(e.getMessage());
                                            }
                                            i += 3;
                                        } else {
                                            throw new ArrayIndexOutOfBoundsException();
                                        }
                                        break;
                                    case "-remove":
                                        if (i + 1 < l_Commands.length) {
                                            try {
                                                d_GameMap.removeContinent(l_Commands[i + 1]);
                                            } catch (IllegalArgumentException e) {
                                                d_Logger.log(e.getMessage());
                                            }
                                            i += 2;
                                        } else {
                                            throw new ArrayIndexOutOfBoundsException();
                                        }
                                    default:
                                        i++;
                                }
                            }
                            break;
                        case "editcountry":
                            i = 1;
                            while (i < l_Commands.length) {
                                switch (l_Commands[i].toLowerCase()) {
                                    case "-add":
                                        if (i + 2 < l_Commands.length) {
                                            try {
                                                d_GameMap.addCountry(l_Commands[i + 1], l_Commands[i + 2]);
                                            } catch (IllegalArgumentException e) {
                                                d_Logger.log(e.getMessage());
                                            }
                                            i += 3;
                                        } else {
                                            throw new ArrayIndexOutOfBoundsException();
                                        }
                                        break;
                                    case "-remove":
                                        if (i + 1 < l_Commands.length) {
                                            try {
                                                d_GameMap.removeCountry(l_Commands[i + 1]);
                                            } catch (IllegalArgumentException e) {
                                                d_Logger.log(e.getMessage());
                                            }
                                            i += 2;
                                        } else {
                                            throw new ArrayIndexOutOfBoundsException();
                                        }
                                    default:
                                        i++;
                                }
                            }
                            break;
                        case "editneighbor":
                            i = 1;
                            while (i < l_Commands.length) {
                                switch (l_Commands[i].toLowerCase()) {
                                    case "-add":
                                        if (i + 2 < l_Commands.length) {
                                            try {
                                                d_GameMap.addNeighbor(l_Commands[i + 1], l_Commands[i + 2]);
                                            } catch (IllegalArgumentException e) {
                                                d_Logger.log(e.getMessage());
                                            }
                                            i += 3;
                                        } else {
                                            throw new ArrayIndexOutOfBoundsException();
                                        }
                                        break;
                                    case "-remove":
                                        if (i + 2 < l_Commands.length) {
                                            try {
                                                d_GameMap.removeNeighbor(l_Commands[i + 1], l_Commands[i + 2]);
                                            } catch (IllegalArgumentException e) {
                                                d_Logger.log(e.getMessage());
                                            }
                                            i += 3;
                                        } else {
                                            throw new ArrayIndexOutOfBoundsException();
                                        }
                                    default:
                                        i++;
                                }
                            }
                            break;
                        case "showmap":
                            d_GameMap.displayMap();
                            break;
                        case "editmap":
                            if (l_Commands.length == 2) {
                                MapReader.readMap(d_GameMap, l_Commands[1]);
                            }
                            break;
                        case "validatemap":
                            if (MapReader.validateMap(d_GameMap)) {
                                d_Logger.log("Map is valid.");
                            } else {
                                d_Logger.log("Map is invalid.");
                            }
                            break;
                        case "savemap":
                            if (l_Commands.length == 2) {
                                if (MapReader.validateMap(d_GameMap)) {
                                    boolean l_HasSaved = MapReader.saveMap(d_GameMap, l_Commands[1]);
                                    if (l_HasSaved) {
                                        d_Logger.log("Map validated & saved successfully.");
                                    } else {
                                        d_Logger.log("Map validated but could not be saved.");
                                    }
                                } else {
                                    d_Logger.log("Map is invalid. Please validate the map before saving.");
                                }
                            }
                            break;
                        default:
                            // To be implemented
                            break;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    d_Logger.log("Incomplete command.");
                } catch (NumberFormatException e) {
                    d_Logger.log("Invalid number format.");
                }

            } else {
                d_Logger.log("Invalid command");
            }


        }

    }

    /**
     * Input validator for commands.
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

    /**
     * Gets game map.
     *
     * @return the game map
     */
    public GameMap getGameMap() {
        return d_GameMap;
    }

}
