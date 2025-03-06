package controller;

import model.abstractClasses.GameController;
import model.GameMap;
import model.abstractClasses.GamePhase;
import model.gamePhases.StartUpPhase;
import utils.MapReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MapEditor extends GameController {

    private final Scanner SCANNER = new Scanner(System.in);
    private final List<String> CLI_COMMANDS = Arrays.asList("editcontinent", "editcountry", "editneighbor", "showmap",
            "savemap", "editmap", "validatemap", "help", "exit");
    private GameMap d_GameMap;

    private GamePhase d_NextPhase = new StartUpPhase();

    public MapEditor() {
        this.d_GameMap = GameMap.getInstance();
    }

    public GamePhase startPhase(GamePhase p_GamePhase) throws Exception {
        System.out.println();
        System.out.println("=========================================");
        System.out.println("\t****** MAP EDITING MODE ******\t");
        while (true) {
            int i;
            System.out.print("Enter command (\"help\" for all commands): ");
            String l_Input = SCANNER.nextLine();
            String[] l_Commands = l_Input.split(" ");

            if (l_Commands[0].equalsIgnoreCase("exit")) {
                d_GameMap.setGamePhase(d_NextPhase);
                return d_NextPhase;
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
                                }
                                else {
                                    System.out.println("Map is invalid. Please validate the map before saving.");
                                }
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

    public boolean inputValidator(String[] p_InputCommands) {
        if (p_InputCommands.length > 0) {
            String l_MainCommand = p_InputCommands[0];
            return CLI_COMMANDS.contains(l_MainCommand.toLowerCase());
        }
        return false;
    }

    public GameMap getGameMap() {
        return d_GameMap;
    }

}
