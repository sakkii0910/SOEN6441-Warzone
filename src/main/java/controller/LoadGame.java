package controller;

import model.GameMap;
import model.abstractClasses.GameController;
import model.abstractClasses.GamePhase;
import model.gamePhases.InitialPhase;
import model.gamePhases.StartUpPhase;
import utils.GameEngine;
import utils.GameProgress;
import utils.logger.LogEntryBuffer;

import java.io.IOException;
import java.util.Scanner;

/**
 * The type Load game.
 */
public class LoadGame extends GameController {

    private LogEntryBuffer d_Logger = LogEntryBuffer.getInstance();

    private final Scanner SCANNER = new Scanner(System.in);

    /**
     * Instantiates a new Map editor using singleton design pattern.
     *
     * @param p_GameEngine the p game engine
     */
    public LoadGame(GameEngine p_GameEngine) {
        super(p_GameEngine);
        d_GameMap = GameMap.getInstance();
        d_NextPhase = new InitialPhase(this.d_GameEngine);
    }

    public void startPhase() throws IOException {
        GameProgress.showFiles();
        d_Logger.log("\n===========================================");
        d_Logger.log("**************** LOAD GAME ****************");
        d_Logger.log("===========================================");

        while (true) {
            int i;
            d_Logger.log("\n-----------------------------------------------------------------------------------------");
            d_Logger.log("List of Load Game Commands ");
            d_Logger.log("loadgame {filename}");
            d_Logger.log("To exit the phase: exit");
            d_Logger.log("-----------------------------------------------------------------------------------------");
            System.out.print("Enter command: ");
            String l_Input = SCANNER.nextLine();
            String[] l_Commands = l_Input.split(" ");

            if (l_Commands[0].equalsIgnoreCase("exit")) {
                this.d_GameEngine.setGamePhase(d_NextPhase);
                this.d_GameMap.setGamePhase(d_NextPhase);
                break;
            } else if (l_Commands[0].equalsIgnoreCase("loadgame") && l_Commands.length == 2) {
                d_NextPhase = GameProgress.loadGameProgress(l_Commands[1]);
                if(d_NextPhase == null){
                    d_NextPhase = new StartUpPhase(this.d_GameEngine);
                }
                this.d_GameEngine.setGamePhase(d_NextPhase);
                this.d_GameMap.setGamePhase(d_NextPhase);
                break;
            } else {
                d_Logger.log("Invalid loadgame command");
            }
        }

    }

}
