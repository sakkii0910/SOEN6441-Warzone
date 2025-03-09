package controller;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameEngineTest {
    private Player player;
    private GameMap gameMap;
    private Continent continent;
    private Country country1, country2, country3;
    private GameEngine gameEngine;

    @BeforeEach
    public void setUp() {
        gameEngine = new GameEngine();
        gameMap = GameMap.getInstance();
        gameMap.resetGameMap();

        // Create player
        player = new Player();
        player.setD_Name("Alice");

        gameMap.addPlayer("Alice");

        // Create a continent with a reinforcement bonus of 5
        continent = new Continent();
        continent.setD_ContinentName("North America");
        continent.setD_ContinentArmies(5);

        // Create countries and assign them to the player
        country1 = new Country();
        country1.setD_CountryName("Canada");
        country1.setPlayer(player);

        country2 = new Country();
        country2.setD_CountryName("USA");
        country2.setPlayer(player);

        country3 = new Country();
        country3.setD_CountryName("Mexico");
        country3.setPlayer(player);

        // Assign countries to continent
        continent.getD_ContinentCountries().add(country1);
        continent.getD_ContinentCountries().add(country2);
        continent.getD_ContinentCountries().add(country3);

        // Add to game map
        gameMap.getContinents().put("North America", continent);
        gameMap.getCountries().put("Canada", country1);
        gameMap.getCountries().put("USA", country2);
        gameMap.getCountries().put("Mexico", country3);

        // Assign captured countries to player
        player.getCapturedCountries().add(country1);
        player.getCapturedCountries().add(country2);
        player.getCapturedCountries().add(country3);
    }

    @Test
    public void testReinforcementCalculation() {
        int reinforcements = gameEngine.calculateReinforcements(player);
        int expectedReinforcements = Math.max(3 / 3, 3) + 5; // 3 from territories, 5 from continent
        assertEquals(expectedReinforcements, reinforcements);
    }


    @Test
    public void testMinimumReinforcements() {
        // Ensure player owns only one country
        player.getCapturedCountries().clear();
        country1.setPlayer(player);
        player.getCapturedCountries().add(country1);
    
        // Remove all existing continents (to ensure no continent bonus is applied)
        gameMap.getContinents().clear();
    
        // Remove the country from any existing continent
        for (Continent continent : gameMap.getContinents().values()) {
            continent.getD_ContinentCountries().remove(country1);
        }
    
        // Calculate reinforcements
        int reinforcements = gameEngine.calculateReinforcements(player);
    
        // Ensure minimum reinforcements of 3
        assertEquals(3, reinforcements);
    }
    

    

}
