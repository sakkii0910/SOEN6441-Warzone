package controller;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MapEditor {

    private final Scanner SCANNER = new Scanner(System.in);
    private final List<String> CLI_COMMANDS = Arrays.asList("editcontinent", "editcountry", "editneighbor", "showmap", "savemap", "editmap", "validatemap");


    public void startPhase() {
        System.out.println("\t****** MAP EDITOR PHASE ******\t");
        while (true) {
            System.out.print("Enter command: ");
            String l_Input = SCANNER.nextLine();
            String[] l_Commands = l_Input.split(" ");

            if (inputValidator(l_Commands)) {
                switch (l_Commands[0].toLowerCase()) {
                    case "editcontinent":

                }
            } else {
                System.out.println("Invalid command");
                System.out.println("=========================================");
                System.out.println(" List of User Map Creation Commands ");
                System.out.println("=========================================");
                System.out.println("\nTo add or remove a continent:");
                System.out.println("  editcontinent -add <continentID> <continentValue> -remove <continentID>");

                System.out.println("\nTo add or remove a country:");
                System.out.println("  editcountry -add <countryID> <continentID> -remove <countryID>");

                System.out.println("\nTo add or remove a neighbor to a country:");
                System.out.println("  editneighbor -add <countryID> <neighborCountryID> -remove <countryID> <neighborCountryID>");

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
