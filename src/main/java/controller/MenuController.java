package controller;

import model.abstractClasses.GameController;
import model.abstractClasses.GamePhase;
import model.gamePhases.ExitGamePhase;
import model.gamePhases.MapEditorPhase;
import model.gamePhases.StartUpPhase;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * The type Menu controller.
 */
public class MenuController extends GameController {

    @Override
    public GamePhase startPhase(GamePhase p_GamePhase) throws Exception {
        Scanner sc = new Scanner(System.in);

        System.out.println();
        System.out.println("=========================================");
        System.out.println("            WARZONE: RISK GAME   ️         ");
        System.out.println("=========================================");
        System.out.println("               MAIN MENU                 ");
        System.out.println("=========================================");
        System.out.println("  [1] Start New Game");
        System.out.println("  [2] Map Editing");
        System.out.println("  [5] Exit");
        System.out.println("=========================================");
        System.out.print("\tSelect an option: ");


        int option = sc.nextInt();
        // Returns the phase selected by user.
        // User can either start playing game or go to map editing phase.
        return switch (option) {
            case 1 -> new StartUpPhase();
            case 2 -> new MapEditorPhase();
            case 5 -> new ExitGamePhase();
            default -> throw new InputMismatchException();
        };
    }
}
