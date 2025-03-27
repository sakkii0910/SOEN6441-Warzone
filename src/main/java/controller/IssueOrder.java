package controller;

import model.GameMap;
import model.Player;
import model.abstractClasses.GameController;
import model.abstractClasses.GamePhase;
import model.gamePhases.ExecuteOrderPhase;
import model.gamePhases.IssueOrderPhase;
import utils.GameEngine;
import utils.logger.LogEntryBuffer;

import java.util.HashMap;

public class IssueOrder extends GameController {

    private final HashMap<String, Player> d_players;

    /**
     * Logger instance
     */
    private LogEntryBuffer d_Logger = LogEntryBuffer.getInstance();

    public IssueOrder(GameEngine p_GameEngine) {
        super(p_GameEngine);
        d_GameMap = GameMap.getInstance();
        d_players = d_GameMap.getPlayers();
        d_NextPhase = new ExecuteOrderPhase(this.d_GameEngine);
    }

    public void startPhase() throws Exception {
        d_Logger.log("\n--------------- ISSUE ORDER PHASE ---------------\n");

        // Issue orders
        for (Player l_player : d_players.values()) {
            l_player.issueOrder();
        }

        this.d_GameEngine.setGamePhase(this.d_NextPhase);
        this.d_GameMap.setGamePhase(this.d_NextPhase);
    }

}
