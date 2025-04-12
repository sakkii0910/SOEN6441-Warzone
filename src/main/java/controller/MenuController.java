package controller;

import model.abstractClasses.GameController;
import model.abstractClasses.GamePhase;
import model.gamePhases.ExitGamePhase;
import model.gamePhases.LoadGamePhase;
import model.gamePhases.MapEditorPhase;
import model.gamePhases.StartUpPhase;
import utils.GameEngine;
import utils.SingleGameMode;
import utils.TournamentMode;
import utils.logger.LogEntryBuffer;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * The type Menu controller.
 *  @author Taha Mirza
 *  @author Poorav Panchal
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
        d_Logger.log("  [3] Load Game");
        d_Logger.log("  [4] Single Game Mode");
        d_Logger.log("  [5] Tournament Mode");
        d_Logger.log("  [6] Exit");
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
            case 3:
                this.d_NextPhase = new LoadGamePhase(this.d_GameEngine);
                break;
            case 4:
                SingleGameMode mode = new SingleGameMode();
                mode.startSingleGame();
                this.d_NextPhase = new ExitGamePhase(this.d_GameEngine); // to return after game
                break;
            case 5:
                TournamentMode tournamentMode = new TournamentMode();
                tournamentMode.startTournamentMode();
                this.d_NextPhase = new ExitGamePhase(this.d_GameEngine);
                break;
            case 6:
                this.d_NextPhase = new ExitGamePhase(this.d_GameEngine);
                break;
                default:
                    throw new InputMismatchException();
        }

        this.d_GameEngine.setGamePhase(this.d_NextPhase);
        this.d_GameMap.setGamePhase(this.d_NextPhase);
    }
}
