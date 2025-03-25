package controller;

import model.GameMap;
import model.Order;
import model.Player;
import model.abstractClasses.GameController;
import model.abstractClasses.GamePhase;
import model.gamePhases.ExitGamePhase;
import model.gamePhases.IssueOrderPhase;
import utils.GameEngine;

import java.util.HashMap;

public class ExecuteOrder extends GameController {

    private final HashMap<String, Player> d_players;

    public ExecuteOrder(GameEngine p_GameEngine) {
        super(p_GameEngine);
        d_GameMap = GameMap.getInstance();
        d_players = d_GameMap.getPlayers();
        d_NextPhase = new ExitGamePhase(this.d_GameEngine);
    }

    @Override
    public void startPhase() throws Exception {
        System.out.println("\n--------------- EXECUTE ORDER PHASE ---------------\n");

        // Execute orders
        for (Player l_player : d_players.values()) {
            Order l_order;
            while ((l_order = l_player.nextOrder()) != null) {
                l_order.execute();
            }
        }

        this.d_GameEngine.setGamePhase(this.d_NextPhase);
        this.d_GameMap.setGamePhase(this.d_NextPhase);
    }

}
