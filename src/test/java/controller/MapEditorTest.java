package controller;
/**
 * Test class for the MapEditor class, which handles map editing commands.
 */
import model.GameMap;
import model.abstractClasses.GamePhase;
import model.gamePhases.StartUpPhase;
import org.junit.jupiter.api.Test;
import utils.MapReader;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

class MapEditorTest {
    /**
     * Tests adding a continent using the map editor.
     */
    @Test
    void testAddContinent() throws Exception {
        // Simulate user input for "editcontinent -add Asia 5" and "exit"
        String input = "editcontinent -add Asia 5\nexit\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream); // Redirect System.in to our simulated input

        MapEditor mapEditor = new MapEditor();
        GamePhase gamePhase = new StartUpPhase();
        mapEditor.startPhase(gamePhase); // This will now read from our simulated input

        // Verify that the continent was added
        assertTrue(mapEditor.getGameMap().getContinents().containsKey("Asia"));
    }
    /**
     * Tests adding a country using the map editor.
     */
    @Test
    void testAddCountry() throws Exception {
        // Simulate user input for "editcontinent -add Asia 5", "editcountry -add India Asia", and "exit"
        String input = "editcontinent -add Asia 5\neditcountry -add India Asia\nexit\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream); // Redirect System.in to our simulated input

        MapEditor mapEditor = new MapEditor();
        GamePhase gamePhase = new StartUpPhase();
        mapEditor.startPhase(gamePhase); // This will now read from our simulated input

        // Verify that the country was added
        assertTrue(mapEditor.getGameMap().getCountries().containsKey("India"));
    }
    /**
     * Tests adding a neighbor relationship using the map editor.
     */
    @Test
    void testAddNeighbor() throws Exception {
        // Simulate user input for "editcontinent -add Asia 5", "editcountry -add India Asia",
        // "editcountry -add China Asia", "editneighbor -add India China", and "exit"
        String input = "editcontinent -add Asia 5\n" +
                "editcountry -add India Asia\n" +
                "editcountry -add China Asia\n" +
                "editneighbor -add India China\nexit\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream); // Redirect System.in to our simulated input

        MapEditor mapEditor = new MapEditor();
        GamePhase gamePhase = new StartUpPhase();
        mapEditor.startPhase(gamePhase); // This will now read from our simulated input

        // Verify that the neighbor was added
        assertTrue(mapEditor.getGameMap().getCountries().get("India").getD_CountryNeighbors()
                .contains(mapEditor.getGameMap().getCountries().get("China")));
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

        // Verify that the system handles invalid commands gracefully
        // (e.g., prints an error message or ignores the command)
    }
    /**
     * Tests validating a map with a disconnected graph.
     */
    @Test
    void testMapValidationForDisconnectedGraph() {
        GameMap gameMap = GameMap.getInstance();
        gameMap.resetGameMap();
        gameMap.addContinent("Asia", 5);
        gameMap.addCountry("India", "Asia");
        gameMap.addCountry("China", "Asia");

        // Verify that the map is invalid because it is disconnected
        assertFalse(MapReader.validateMap(gameMap), "Map should be invalid because it is disconnected.");
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

        // Verify that the map is a connected graph
        assertTrue(MapReader.validateMap(gameMap), "Map should be a connected graph.");
    }
}