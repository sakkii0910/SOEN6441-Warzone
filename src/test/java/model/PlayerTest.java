package model;
import model.*;
import model.strategy.player.PlayerStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    private Player player;
    private Country country;

    @BeforeEach
public void setUp() {
    // Initialize game map
    GameMap gameMap = GameMap.getInstance();
    gameMap.resetGameMap(); // Ensure a clean state before each test

    // Create player
    player = new Player(PlayerStrategy.getStrategy("human"));
    player.setD_Name("Alice");

    // Create and add country to the game map
    country = new Country();
    country.setD_CountryName("Canada");
    country.setPlayer(player);

    gameMap.getCountries().put("Canada", country); // ✅ Ensure country is registered in the game map

    // Assign player some reinforcements
    player.assignReinforcements(5);
}


    @Test
    public void testDeployMoreThanAvailable() {
//        int initialReinforcements = player.getReinforcementArmies();
//
//        DeployOrder order = new DeployOrder(player, "Canada", 10);
//        order.execute();
//
//        assertEquals(initialReinforcements, player.getReinforcementArmies());
    }

    @Test
public void testDeployExactReinforcements() {
//    int initialReinforcements = player.getReinforcementArmies();
//
//    // Ensure the player has the exact number of reinforcements
//    DeployOrder order = new DeployOrder(player, "Canada", initialReinforcements);
//
//    // Manually reduce reinforcements since the test does not call issueOrder()
//    player.assignReinforcements(-initialReinforcements);
//
//    // Execute the deploy order
//    order.execute();
//
//    // Now check if reinforcements are 0
//    assertEquals(0, player.getReinforcementArmies());
}


}
