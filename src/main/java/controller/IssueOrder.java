package controller;

import model.GameMap;
import model.Player;
import model.abstractClasses.GameController;
import model.gamePhases.ExecuteOrderPhase;
import utils.GameEngine;
import utils.logger.LogEntryBuffer;

import java.util.HashMap;

public class IssueOrder extends GameController {

    private final HashMap<String, Player> d_Players;

    /**
     * Logger instance
     */
    private LogEntryBuffer d_Logger = LogEntryBuffer.getInstance();

    public IssueOrder(GameEngine p_GameEngine) {
        super(p_GameEngine);
        d_GameMap = GameMap.getInstance();
        d_Players = d_GameMap.getPlayers();
        d_NextPhase = new ExecuteOrderPhase(this.d_GameEngine);
    }

    public void startPhase() throws Exception {
        d_Logger.log("\n\n--------------- ISSUE ORDER PHASE ---------------");

        int count = 0;
        // Issue orders
        while (count < d_Players.size()) {
            for (Player l_player : d_Players.values()) {
                d_Logger.log("\n\nCurrent Player Turn: " + l_player.getD_Name());
                if (l_player.isD_TurnCompleted()) {
                    count++;
                    d_Logger.log("-----------------------------------------");
                    d_Logger.log("User has completed his turns.");
                } else
                    l_player.issueOrder();
            }
        }

        for (Player l_player : d_Players.values()) {
            l_player.setD_TurnCompleted(false);
        }

        this.d_GameEngine.setGamePhase(this.d_NextPhase);
        this.d_GameMap.setGamePhase(this.d_NextPhase);
    }

}
