package controller;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import model.GameMap;
import utils.MapReader;

/**
 * This class implements the Game Controller and it executes the current phases
 *
 * @author Poorav Panchal
 */

public class GamePlay extends GameController {
    private final Scanner SCANNER = new Scanner(System.in);

    private final List<String> CLI_COMMANDS = Arrays.asList("gameplayer", "assigncountries");

    private GameMap d_GameMap;

    public GamePlay() {
        d_GameMap = GameMap.getInstance();
    }
    
    @Override
    public void startPhase(GamePhase p_GamePhase) throws Exception {
        System.out.println("\t****** GAME PLAY MODE ******\t");
        while (true) {
            int i;
            System.out.println("Enter command (\"help\" for all commands): ");
            String l_Input = SCANNER.nextLine();
            String[] l_Commands = l_Input.split(" ");

            if(l_Commands[0].equalsIgnoreCase("exit")) {
                break;
            } else if(inputValidator(l_Commands)) {
                try {
                    switch (l_Commands[0].toLowerCase()) {
                        case "help":
                            System.out.println("=========================================");
                            System.out.println(" List of Game Play Commands ");
                            System.out.println("=========================================");
                            System.out.println("\nTo add or remove a player:");
                            System.out.println("  gameplayer -add playername -remove playername");
                            System.out.println("\nTo assign countries to all players: ");
                            System.out.println("assigncountries");
                            System.out.println("=========================================");
                            break;
                        case "gameplayer":
                            i = 1;
                            while (i < l_Commands.length) {
                                switch (l_Commands[i].toLowerCase()) {
                                    case "-add":
                                        if (i + 2 < l_Commands.length) {
                                            try {
                                                d_GameMap.addPlayer(l_Commands[i+1]);
                                            } catch (IllegalArgumentException e) {
                                                System.out.println(e.getMessage());
                                            }
                                        } else {
                                            throw new ArrayIndexOutOfBoundsException();
                                        }
                                        break;
                                    case "-remove":
                                        if (i + 2 < l_Commands.length) {
                                            try {
                                                d_GameMap.removePlayer(l_Commands[i+1]);
                                            } catch (IllegalArgumentException e) {
                                                System.out.println(e.getMessage());
                                            }
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
                            d_GameMap.assignCountries();
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

    public boolean inputValidator(String[] p_InputCommands) {
        if (p_InputCommands.length > 0) {
            String l_MainCommand = p_InputCommands[0];
            return CLI_COMMANDS.contains(l_MainCommand.toLowerCase());
        }
        return false;
    }
    
}
