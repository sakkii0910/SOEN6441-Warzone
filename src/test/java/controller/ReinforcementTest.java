package controller;

import model.Continent;
import model.Country;
import model.GameMap;
import model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.GameEngine;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link Reinforcement} phase logic.
 * Validates reinforcement army calculations and phase execution flow.
 * @author Sakshi Sudhir Mulik
 */
class ReinforcementTest {
    private Reinforcement d_reinforcement;
    private GameMap d_gameMap;
    private Player d_player;
    private Continent d_continent;

    /**
     * Initializes test environment with a clean game map, player, and continent.
     * Ensures isolation between test cases.
     */
    @BeforeEach
    void setUp() {
        d_gameMap = GameMap.getInstance();
        d_gameMap.resetGameMap();

        d_player = new Player(null);
        d_player.setD_Name("TestPlayer");

        d_continent = new Continent();
        d_continent.setD_ContinentName("TestContinent");
        d_continent.setD_ContinentArmies(5);
        d_gameMap.getContinents().put("TestContinent", d_continent);

        d_reinforcement = new Reinforcement(new GameEngine());
    }

    /**
     * Tests continent bonus application in reinforcement calculation.
     * Verifies player receives correct armies for owning all continent countries.
     */
    @Test
    void testContinentBonus() {
        addCountriesToPlayer(3);
        d_continent.getD_ContinentCountries().forEach(c -> c.setPlayer(d_player));
        int l_armies = d_reinforcement.calculateReinforcements(d_player);
        assertEquals(8, l_armies); // 3 (territories) + 5 (continent bonus)
    }

    /**
     * Tests full reinforcement phase execution.
     * Validates if player's reinforcement pool updates correctly after phase.
     * @throws Exception if phase execution fails
     */

    private void addCountriesToPlayer(int p_count) {
        for (int i = 1; i <= p_count; i++) {
            Country l_country = new Country();
            l_country.setD_CountryName("TestCountry" + i);
            l_country.setPlayer(d_player);
            d_player.getCapturedCountries().add(l_country);
            d_continent.getD_ContinentCountries().add(l_country);
            d_gameMap.getCountries().put(l_country.getD_CountryName(), l_country);
        }
    }
}