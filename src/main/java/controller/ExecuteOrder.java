package controller;

import model.GameMap;
import model.order.Order;
import model.Player;
import model.abstractClasses.GameController;
import model.gamePhases.ExitGamePhase;
import model.gamePhases.ReinforcementPhase;
import utils.GameEngine;
import utils.logger.LogEntryBuffer;

import java.util.HashMap;

public class ExecuteOrder extends GameController {

    private final HashMap<String, Player> d_players;

    /**
     * Logger instance
     */
    private LogEntryBuffer d_Logger = LogEntryBuffer.getInstance();

    public ExecuteOrder(GameEngine p_GameEngine) {
        super(p_GameEngine);
        d_GameMap = GameMap.getInstance();
        d_players = d_GameMap.getPlayers();
        d_NextPhase = new ReinforcementPhase(this.d_GameEngine);
    }

    @Override
    public void startPhase() throws Exception {
        d_Logger.log("\n\n--------------- EXECUTE ORDER PHASE ---------------");

        // Execute orders
        for (Player l_player : d_players.values()) {
            d_Logger.log("\n\nCurrent Player Execution: " + l_player.getD_Name());
            Order l_order;
            while ((l_order = l_player.nextOrder()) != null) {
                l_order.execute();
            }
        }

        isGameWon();

        this.d_GameEngine.setGamePhase(this.d_NextPhase);
        this.d_GameMap.setGamePhase(this.d_NextPhase);
    }

    public void isGameWon(){
        for (Player l_player : d_players.values()) {
            if (l_player.getCapturedCountries().size() == d_GameMap.getCountries().size()) {
                d_Logger.log("\n--------------- WINNER WINNER ---------------\n");
                d_Logger.log("The Player " + l_player.getD_Name() + " has won the game");
                d_NextPhase = new ExitGamePhase(this.d_GameEngine);
            }
        }

    }

}
