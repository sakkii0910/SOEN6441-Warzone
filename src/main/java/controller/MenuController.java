package controller;

import model.abstractClasses.GameController;
import model.abstractClasses.GamePhase;
import model.gamePhases.ExitGamePhase;
import model.gamePhases.MapEditorPhase;
import model.gamePhases.StartUpPhase;
import utils.GameEngine;
import utils.logger.LogEntryBuffer;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * The type Menu controller.
 */
public class MenuController extends GameController {

    /**
     * Logger instance
     */
    private LogEntryBuffer d_Logger = LogEntryBuffer.getInstance();

    public MenuController(GameEngine p_GameEngine) {
        super(p_GameEngine);
    }

    @Override
    public void startPhase() throws Exception {
        Scanner sc = new Scanner(System.in);

        d_Logger.log("\n");
        d_Logger.log("=========================================");
        d_Logger.log("            WARZONE: RISK GAME   ️         ");
        d_Logger.log("=========================================");
        d_Logger.log("               MAIN MENU                 ");
        d_Logger.log("=========================================");
        d_Logger.log("  [1] Start New Game");
        d_Logger.log("  [2] Map Editing");
        d_Logger.log("  [5] Exit");
        d_Logger.log("=========================================");
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
