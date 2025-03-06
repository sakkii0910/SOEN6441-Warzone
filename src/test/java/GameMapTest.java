// import java.util.*;
// import model.Country;
// import model.GameMap;
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions;


//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotEquals;
// import static org.junit.jupiter.api.Assertions.assertTrue;
// import static org.junit.jupiter.api.Assertions.assertFalse;
//
///**
//* Writing the test cases to Load map , save map, validate map ,
//*  add the country in map
//* @author Sakshi Mulik
//* */
// import java.io.File;
//
// public class GameMapTest {
//
//    @Test
//    public void testLoadMap() {
//        GameMap gameMap = new GameMap();
//        String filePath = "maps/test_map.map";
//
//        boolean result = gameMap.loadMap(filePath);
//        System.out.println("Load Map result: " + result);
//        assertTrue(result, "Map should load successfully");
//    }
//
//    @Test
//    public void testValidateMap() {
//        GameMap gameMap = new GameMap();
//        String filePath = "maps/test_map.map";
//        boolean result = gameMap.loadMap(filePath);
//        System.out.println("Map loaded successfully: " + result);
//        assertTrue(gameMap.validateMap(), "Map validation should pass for a well-formed map");
//    }
//
//    @Test
//    public void testAddCountry() {
//        GameMap map = new GameMap();
//        map.addCountry("Asia", "India");
//        Country india = map.getCountry("India");
//        assertNotNull(india, "India should be added to the map");
//    }
//
//    @Test
//    void testSaveMap() {
//        GameMap gameMap = new GameMap();
//        gameMap.addCountry("India", "Asia");
//        gameMap.addCountry("China", "Asia");
//        gameMap.addBorder("India", "China");
//
//        boolean isSaved = gameMap.saveMap("output_test.map");
//        assertTrue(isSaved, "Map should be saved successfully");
//
//        // Checking if the file exists
//        File file = new File("output_test.map");
//        assertTrue(file.exists(), "Saved map file should exist");
//    }

	// @Test
	// public void assignCountries() {
    //     // Sample countries
    //     List<String> countryList = new ArrayList<>(Arrays.asList(
    //         "India", "Pakistan", "Canada", "England", "Australia", "USA"
    //     ));

    //     // Sample players
    //     List<String> playerList = new ArrayList<>(Arrays.asList(
    //         "Player_1", "Player_2", "Player_3"
    //     ));

    //     // Copy of country list to check for shuffle
    //     List<String> originalCountryList = new ArrayList<>(countryList);

    //     // Shuffle countries
    //     Collections.shuffle(countryList);

    //     // Check that shuffled list is not equal to original
    //     assertFalse(countryList.equals(originalCountryList), 
    //             "Countries should be shuffled before assignment");

    //     // Map to store which countries are assigned to each player
    //     Map<String, List<String>> playerCountries = new HashMap<>();
    //     playerList.forEach(player -> playerCountries.put(player, new ArrayList<>()));

    //     // Assign countries to players in a round-robin fashion
    //     int playerIndex = 0;
    //     for (String country : countryList) {
    //         String player = playerList.get(playerIndex);
    //         playerCountries.get(player).add(country);
    //         playerIndex = (playerIndex + 1) % playerList.size();
    //     }

    //     // Output the assignment for visual verification (optional)
    //     System.out.println("+-----------------+------------------------------------------------+");
    //     System.out.println("| Player          | Assigned Countries                             |");
    //     System.out.println("+-----------------+------------------------------------------------+");
    //     playerCountries.forEach((player, countries) -> 
    //         System.out.printf("| %-15s | %-46s |\n", player, String.join(", ", countries))
    //     );
    //     System.out.println("+-----------------+------------------------------------------------+");
    // }
//
// }
