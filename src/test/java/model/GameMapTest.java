package model;
/**
 * Test class for the GameMap class, which handles map creation, validation, and file operations.
 * @author
 */

import controller.MapEditor;
import model.abstractClasses.GamePhase;
import model.gamePhases.StartUpPhase;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import utils.MapReader;

class GameMapTest {
    /**
     * Tests adding a continent to the game map.
     */
    @Test
    void testAddContinent() {
        GameMap gameMap = GameMap.getInstance();
        gameMap.resetGameMap(); // Reset the GameMap instance
        gameMap.addContinent("Asia", 5);
        assertTrue(gameMap.getContinents().containsKey("Asia"));
        assertEquals(5, gameMap.getContinents().get("Asia").getD_ContinentArmies());
    }
    /**
     * Tests adding a country to the game map.
     */
    @Test
    void testAddCountry() {
        GameMap gameMap = GameMap.getInstance();
        gameMap.resetGameMap(); // Reset the GameMap instance
        gameMap.addContinent("Asia", 5);
        gameMap.addCountry("India", "Asia");
        assertTrue(gameMap.getCountries().containsKey("India"));
        assertEquals("Asia", gameMap.getCountries().get("India").getD_CountryContinent().getD_ContinentName());
    }
    /**
     * Tests adding a neighbor relationship between two countries.
     */
    @Test
    void testAddNeighbor() {
        GameMap gameMap = GameMap.getInstance();
        gameMap.resetGameMap(); // Reset the GameMap instance
        gameMap.addContinent("Asia", 5);
        gameMap.addCountry("India", "Asia");
        gameMap.addCountry("China", "Asia");
        gameMap.addNeighbor("India", "China");

        // Verify that India has China as a neighbor
        assertTrue(gameMap.getCountries().get("India").getD_CountryNeighbors().contains(gameMap.getCountries().get("China")));

        // Verify that China has India as a neighbor (if the relationship is bidirectional)
        assertTrue(gameMap.getCountries().get("China").getD_CountryNeighbors().contains(gameMap.getCountries().get("India")));
    }
    /**
     * Tests validating a correctly structured map.
     */
    @Test
    void testValidateMap() {
        GameMap gameMap = GameMap.getInstance();
        gameMap.resetGameMap(); // Reset the GameMap instance
        gameMap.addContinent("Asia", 5);
        gameMap.addCountry("India", "Asia");
        gameMap.addCountry("China", "Asia");
        gameMap.addNeighbor("India", "China");

        assertTrue(MapReader.validateMap(gameMap));
    }
    /**
     * Tests validating an incorrectly structured map.
     */
    @Test
    void testInvalidMap() {
        GameMap gameMap = GameMap.getInstance();
        gameMap.resetGameMap(); // Reset the GameMap instance

        //  Map has no continents (invalid)
        assertFalse(MapReader.validateMap(gameMap), "Map should be invalid because it has no continents.");

        //  Map has a continent but no countries (invalid)
        gameMap.addContinent("Asia", 5);
        assertFalse(MapReader.validateMap(gameMap), "Map should be invalid because it has no countries.");

        //  Map has a country but no neighbors (invalid)
        gameMap.addCountry("India", "Asia");
        assertFalse(MapReader.validateMap(gameMap), "Map should be invalid because it has a country with no neighbors.");

        //  Map has a country with neighbors (valid)
        gameMap.addCountry("China", "Asia");
        gameMap.addNeighbor("India", "China");
        assertTrue(MapReader.validateMap(gameMap), "Map should be valid because all countries have neighbors.");
    }

    /**
     * Tests assigning countries to players in a round-robin fashion.
     */

    @Test
     public void assignCountries() {
         // Sample countries
        List<String> countryList = new ArrayList<>(Arrays.asList(
             "India", "Pakistan", "Canada", "England", "Australia", "USA"
         ));

         // Sample players
         List<String> playerList = new ArrayList<>(Arrays.asList(
             "Player_1", "Player_2", "Player_3"
         ));

         // Copy of country list to check for shuffle
         List<String> originalCountryList = new ArrayList<>(countryList);

         // Shuffle countries
         Collections.shuffle(countryList);

         // Check that shuffled list is not equal to original
         assertFalse(countryList.equals(originalCountryList),
                 "Countries should be shuffled before assignment");

         // Map to store which countries are assigned to each player
         Map<String, List<String>> playerCountries = new HashMap<>();
         playerList.forEach(player -> playerCountries.put(player, new ArrayList<>()));

         // Assign countries to players in a round-robin fashion
         int playerIndex = 0;
         for (String country : countryList) {
             String player = playerList.get(playerIndex);
             playerCountries.get(player).add(country);
             playerIndex = (playerIndex + 1) % playerList.size();
         }

        // Output the assignment for visual verification (optional)
         System.out.println("+-----------------+------------------------------------------------+");
         System.out.println("| Player          | Assigned Countries                             |");
         System.out.println("+-----------------+------------------------------------------------+");
         playerCountries.forEach((player, countries) ->
             System.out.printf("| %-15s | %-46s |\n", player, String.join(", ", countries))
         );
         System.out.println("+-----------------+------------------------------------------------+");
     }
    /**
     * Tests if the map is a connected graph.
     */
    @Test
    void testMapIsConnectedGraph() {
        GameMap gameMap = GameMap.getInstance();
        gameMap.resetGameMap();
        gameMap.addContinent("Asia", 5);
        gameMap.addCountry("India", "Asia");
        gameMap.addCountry("China", "Asia");
        gameMap.addNeighbor("India", "China");

        assertTrue(MapReader.validateMap(gameMap), "Map should be a connected graph.");
    }
    /**
     * Tests handling of invalid commands in the map editor.
     */
    @Test
    void testInvalidCommand() throws Exception {
        // Simulate user input for an invalid command
        String input = "invalidcommand\nexit\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        MapEditor mapEditor = new MapEditor();
        GamePhase gamePhase = new StartUpPhase();
        mapEditor.startPhase(gamePhase);


    }
    /**
     * Tests adding a duplicate country to the map.
     */
    @Test
    void testInvalidMapWithDuplicateCountries() {
        GameMap gameMap = GameMap.getInstance();
        gameMap.resetGameMap();
        gameMap.addContinent("Asia", 5);

        // Add the first country
        gameMap.addCountry("India", "Asia");

        // Try to add the same country again (should throw an exception)
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            gameMap.addCountry("India", "Asia"); // Duplicate country
        });

        // Verify the exception message
        String expectedMessage = "Country India already exists.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage), "Expected exception message: " + expectedMessage);
    }
    /**
     * Tests saving the map to a text file.
     */
    @Test
    void testSaveMapToFile() throws Exception {
        // Reset the GameMap instance
        GameMap.getInstance().resetGameMap();

        // Add a continent and country to the map
        GameMap.getInstance().addContinent("Asia", 5);
        GameMap.getInstance().addCountry("India", "Asia");

        // Save the map directly using MapReader
        String fileName = "testmap.txt";
        boolean isSaved = MapReader.saveMap(GameMap.getInstance(), fileName);

        // Verify that the map was saved successfully
        assertTrue(isSaved, "Map should be saved successfully.");

        // Verify that the file was created in the "maps" directory
        File file = new File("maps/" + fileName);
        assertTrue(file.exists(), "Map file should be saved.");

        // Debug: Print the file content
        System.out.println("File content:");
        Files.readAllLines(file.toPath()).forEach(System.out::println);

        // Verify the file content
        List<String> fileContent = Files.readAllLines(file.toPath());
        assertTrue(fileContent.contains("[map]"), "File should contain the [map] section.");
        assertTrue(fileContent.contains("author=Team 5"), "File should contain the author.");
        assertTrue(fileContent.contains("mapname=" + fileName), "File should contain the map name.");
        assertTrue(fileContent.contains("[continents]"), "File should contain the [continents] section.");
        assertTrue(fileContent.contains("Asia 5"), "File should contain the continent 'Asia' with value 5.");
        assertTrue(fileContent.contains("[countries]"), "File should contain the [countries] section.");
        assertTrue(fileContent.contains("1 India 1"), "File should contain the country 'India'.");
        assertTrue(fileContent.contains("[borders]"), "File should contain the [borders] section.");

        // Clean up the test file
        if (file.exists()) {
            file.delete();
        }
    }
    /**
     * Tests loading a map from a text file.
     */
    @Test
    void testLoadMapFromFile() throws Exception {
        // Reset the GameMap instance
        GameMap.getInstance().resetGameMap();

        // Create a sample map file in the correct format
        String mapContent = "[map]\n" +
                "author=Team 5\n" +
                "mapname=testmap.txt\n" +
                "\n[continents]\n" +
                "Asia 5\n" +
                "\n[countries]\n" +
                "1 India 1\n" +
                "2 China 1\n" +
                "\n[borders]\n" +
                "1 2\n" +
                "2 1\n";
        Path filePath = Paths.get("maps/testmap.txt");
        Files.write(filePath, mapContent.getBytes());

        // Debug: Verify the file was created
        assertTrue(Files.exists(filePath), "Map file should be created.");

        // Load the map directly using MapReader
        MapReader.readMap(GameMap.getInstance(), "testmap.txt");

        // Debug: Print the state of the GameMap
        System.out.println("Continents in GameMap: " + GameMap.getInstance().getContinents());
        System.out.println("Countries in GameMap: " + GameMap.getInstance().getCountries());

        // Verify that the map was loaded correctly
        assertTrue(GameMap.getInstance().getContinents().containsKey("Asia"), "Continent 'Asia' should be loaded.");
        assertTrue(GameMap.getInstance().getCountries().containsKey("India"), "Country 'India' should be loaded.");
        assertTrue(GameMap.getInstance().getCountries().containsKey("China"), "Country 'China' should be loaded.");

        // Clean up the test file
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }
    }
    /**
     * Tests validating a map with a disconnected continent.
     */
    @Test
    void testValidateMapWithDisconnectedContinent() {
        GameMap gameMap = GameMap.getInstance();
        gameMap.resetGameMap();
        gameMap.addContinent("Asia", 5);
        gameMap.addCountry("India", "Asia");
        gameMap.addCountry("China", "Asia");

        // Verify that the map is invalid because the continent is disconnected
        assertFalse(MapReader.validateMap(gameMap), "Map should be invalid because the continent is disconnected.");
    }
    /**
     * Tests validating a map with an invalid continent value.
     */
    @Test
    void testValidateMapWithInvalidContinentValue() {
        GameMap gameMap = GameMap.getInstance();
        gameMap.resetGameMap();
        gameMap.addContinent("Asia", -5); // Invalid continent value
        gameMap.addCountry("India", "Asia");

        // Verify that the map is invalid because the continent value is invalid
        assertFalse(MapReader.validateMap(gameMap), "Map should be invalid because the continent value is invalid.");
    }
    /**
     * Tests displaying the map as text.
     */
    @Test
    void testDisplayMapAsText() {
        // Reset the GameMap instance
        GameMap gameMap = GameMap.getInstance();
        gameMap.resetGameMap();

        // Add a continent and countries
        gameMap.addContinent("Asia", 5);
        gameMap.addCountry("India", "Asia");
        gameMap.addCountry("China", "Asia");
        gameMap.addNeighbor("India", "China");

        // Redirect System.out to capture the output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);

        // Call the displayMap method
        gameMap.displayMap();

        // Restore System.out
        System.setOut(System.out);

        // Get the output as a string
        String output = outputStream.toString();

        // Verify that the output contains the expected map details
        assertTrue(output.contains("Asia"), "Output should contain the continent 'Asia'.");
        assertTrue(output.contains("India"), "Output should contain the country 'India'.");
        assertTrue(output.contains("China"), "Output should contain the country 'China'.");
        assertTrue(output.contains("India") && output.contains("China"), "Output should show the neighbor relationship between India and China.");
    }

}