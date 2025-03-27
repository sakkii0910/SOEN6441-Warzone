package controller;

import model.Continent;
import model.Country;
import model.GameMap;
import model.Player;
import model.abstractClasses.GameController;
import model.gamePhases.IssueOrderPhase;
import utils.GameEngine;
import utils.logger.LogEntryBuffer;

import java.util.HashMap;

/**
 * The type Reinforcement.
 */
public class Reinforcement extends GameController {

    private final HashMap<String, Player> d_players;

    /**
     * Logger instance
     */
    private LogEntryBuffer d_Logger = LogEntryBuffer.getInstance();

    /**
     * Instantiates a new Reinforcement.
     *
     * @param p_GameEngine the p game engine
     */
    public Reinforcement(GameEngine p_GameEngine) {
        super(p_GameEngine);
        d_GameMap = GameMap.getInstance();
        d_players = d_GameMap.getPlayers();
        d_NextPhase = new IssueOrderPhase(this.d_GameEngine);
    }

    @Override
    public void startPhase() throws Exception {

        d_Logger.log("\n\n--------------- REINFORCEMENT PHASE ---------------\n\n");


        // Assign reinforcements
        for (Player l_player : d_players.values()) {
            int l_reinforcements = calculateReinforcements(l_player);
            l_player.assignReinforcements(l_reinforcements);
        }

        this.d_GameEngine.setGamePhase(this.d_NextPhase);
        this.d_GameMap.setGamePhase(this.d_NextPhase);
    }

    /**
     * Calculates the reinforcement armies for a player.
     *
     * @param p_player The player whose reinforcements are to be calculated.
     * @return The number of reinforcement armies.
     */
    public int calculateReinforcements(Player p_player) {
        int l_territoryCount = p_player.getCapturedCountries().size();
        int l_reinforcements = Math.max(l_territoryCount / 3, 3); // Minimum of 3 reinforcements

        // Check if the player controls an entire continent
        for (Continent l_continent : GameMap.getInstance().getContinents().values()) {
            boolean l_controlsAll = true;
            for (Country l_country : l_continent.getD_ContinentCountries()) {
                if (l_country.getPlayer() != p_player) { // If any country is not owned by the player
                    l_controlsAll = false;
                    break;
                }
            }
            if (l_controlsAll) {
                l_reinforcements += l_continent.getD_ContinentArmies(); // Add continent bonus
            }
        }

        return l_reinforcements;
    }
}
