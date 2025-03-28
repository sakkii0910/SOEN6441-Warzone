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

/**
 * The type Execute order.
 * @author Taha Mirza
 * @author Poorav Panchal
 */
public class ExecuteOrder extends GameController {

    private final HashMap<String, Player> d_players;

    /**
     * Logger instance
     */
    private LogEntryBuffer d_Logger = LogEntryBuffer.getInstance();

    /**
     * Instantiates a new Execute order.
     *
     * @param p_GameEngine the p game engine
     */
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

        int l_Counter = 0;
        while (l_Counter < d_players.size()) {
            for (Player l_Player : d_GameMap.getPlayers().values()) {
                if(l_Player.isD_TurnCompleted()){
                    continue;
                }
                d_Logger.log("\n\nCurrent Player Execution: " + l_Player.getD_Name());
                Order l_Order = l_Player.nextOrder();
                if (l_Order == null) {
                    d_Logger.log("-----------------------------------------------------------------------------------------");
                    d_Logger.log("Player " + l_Player.getD_Name() + " orders completed.");
                    l_Player.setD_TurnCompleted(true);
                    l_Counter++;
                } else {
                    if (l_Order.execute()) {
                        l_Order.printOrderCommand();
                    }
                }
            }
        }

        for (Player l_Player : d_players.values()) {
            l_Player.setD_TurnCompleted(false);
        }

        isGameWon();

        this.d_GameEngine.setGamePhase(this.d_NextPhase);
        this.d_GameMap.setGamePhase(this.d_NextPhase);
    }

    /**
     * Is game won.
     */
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
