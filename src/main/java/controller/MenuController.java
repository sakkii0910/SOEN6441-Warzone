package controller;

import model.abstractClasses.GameController;
import model.abstractClasses.GamePhase;
import model.gamePhases.ExitGamePhase;
import model.gamePhases.MapEditorPhase;
import model.gamePhases.StartUpPhase;
import utils.GameEngine;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * The type Menu controller.
 */
public class MenuController extends GameController {

    public MenuController(GameEngine p_GameEngine) {
        super(p_GameEngine);
    }

    @Override
    public void startPhase() throws Exception {
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
        switch (option) {
            case 1:
                this.d_NextPhase = new StartUpPhase(this.d_GameEngine);
                break;
            case 2:
                this.d_NextPhase = new MapEditorPhase(this.d_GameEngine);
                break;
            case 5:
                this.d_NextPhase = new ExitGamePhase(this.d_GameEngine);
                break;
            default:
                throw new InputMismatchException();
        }

        this.d_GameEngine.setGamePhase(this.d_NextPhase);
        this.d_GameMap.setGamePhase(this.d_NextPhase);
    }
}
