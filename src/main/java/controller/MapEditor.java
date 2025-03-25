package controller;

import model.abstractClasses.GameController;
import model.GameMap;
import model.abstractClasses.GamePhase;
import model.gamePhases.InitialPhase;
import utils.GameEngine;
import utils.MapReader;


import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Map Editor Class to handle all maps related command.
 */
public class MapEditor extends GameController {

    private final Scanner SCANNER = new Scanner(System.in);
    private final List<String> CLI_COMMANDS = Arrays.asList("editcontinent", "editcountry", "editneighbor", "showmap",
            "savemap", "editmap", "validatemap", "help", "exit");

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
        System.out.println();
        System.out.println("=========================================");
        System.out.println("\t****** MAP EDITING MODE ******\t");
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
                            System.out.println("=========================================");
                            System.out.println(" List of User Map Creation Commands ");
                            System.out.println("=========================================");
                            System.out.println("\nTo add or remove a continent:");
                            System.out.println(
                                    "  editcontinent -add <continentID> <continentValue> -remove <continentID>");

                            System.out.println("\nTo add or remove a country:");
                            System.out.println("  editcountry -add <countryID> <continentID> -remove <countryID>");

                            System.out.println("\nTo add or remove a neighbor to a country:");
                            System.out.println(
                                    "  editneighbor -add <countryID> <neighborCountryID> -remove <countryID> <neighborCountryID>");

                            System.out.println("\n-----------------------------------------");
                            System.out.println(" Map Commands (Edit/Save) ");
                            System.out.println("-----------------------------------------");

                            System.out.println("To edit a map:  editmap <filename>");
                            System.out.println("To save a map:  savemap <filename>");

                            System.out.println("\n-----------------------------------------");
                            System.out.println(" Additional Map Commands ");
                            System.out.println("-----------------------------------------");

                            System.out.println("To show the map:      showmap");
                            System.out.println("To validate the map:  validatemap");

                            System.out.println("=========================================");
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
                                                System.out.println(e.getMessage());
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
                                                System.out.println(e.getMessage());
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
                                                System.out.println(e.getMessage());
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
                                                System.out.println(e.getMessage());
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
                                                System.out.println(e.getMessage());
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
                                                System.out.println(e.getMessage());
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
                                System.out.println("Map is valid.");
                            } else {
                                System.out.println("Map is invalid.");
                            }
                            break;
                        case "savemap":
                            if (l_Commands.length == 2) {
                                if (MapReader.validateMap(d_GameMap)) {
                                    boolean l_HasSaved = MapReader.saveMap(d_GameMap, l_Commands[1]);
                                    if (l_HasSaved) {
                                        System.out.println("Map validated & saved successfully.");
                                    } else {
                                        System.out.println("Map validated but could not be saved.");
                                    }
                                } else {
                                    System.out.println("Map is invalid. Please validate the map before saving.");
                                }
                            }
                            break;
                        default:
                            // To be implemented
                            break;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Incomplete command.");
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format.");
                }

            } else {
                System.out.println("Invalid command");
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
