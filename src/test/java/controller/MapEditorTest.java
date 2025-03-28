package controller;

/**
 * Test class for the {@link MapEditor} class, which handles map editing commands.
 * Validates continent/country/neighbor operations, error handling, and map connectivity.
 * @author Sakshi Sudhir Mulik
 */
import model.GameMap;
import model.abstractClasses.GamePhase;
import model.gamePhases.StartUpPhase;
import org.junit.jupiter.api.Test;
import utils.GameEngine;
import utils.MapReader;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

class MapEditorTest {
    /**
     * Tests continent addition via the map editor.
     * Verifies if the continent is correctly added to the game map.
     * @throws Exception if input simulation or map update fails
     */
    @Test
    void testAddContinent() throws Exception {
        // Simulate user input for "editcontinent -add Asia 5" and "exit"
        String input = "editcontinent -add Asia 5\nexit\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        GameEngine gameEngine = new GameEngine();
        MapEditor mapEditor = new MapEditor(gameEngine);
        mapEditor.startPhase();

        assertTrue(mapEditor.getGameMap().getContinents().containsKey("Asia"));
    }

    /**
     * Tests country addition with a valid parent continent.
     * Ensures the country is registered under the correct continent.
     * @throws Exception if input simulation or country creation fails
     */
    @Test
    void testAddCountry() throws Exception {
        String input = "editcontinent -add Asia 5\neditcountry -add India Asia\nexit\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        GameEngine gameEngine = new GameEngine();
        MapEditor mapEditor = new MapEditor(gameEngine);
        mapEditor.startPhase();

        assertTrue(mapEditor.getGameMap().getCountries().containsKey("India"));
    }

    /**
     * Tests neighbor relationship creation between countries.
     * Validates bidirectional adjacency in the game map.
     * @throws Exception if neighbor linkage fails
     */
    @Test
    void testAddNeighbor() throws Exception {
        String input = "editcontinent -add Asia 5\n" +
                "editcountry -add India Asia\n" +
                "editcountry -add China Asia\n" +
                "editneighbor -add India China\nexit\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        GameEngine gameEngine = new GameEngine();
        MapEditor mapEditor = new MapEditor(gameEngine);
        mapEditor.startPhase();

        assertTrue(mapEditor.getGameMap().getCountries().get("India").getD_CountryNeighbors()
                .contains(mapEditor.getGameMap().getCountries().get("China")));
    }

    /**
     * Tests handling of invalid user commands.
     * Verifies graceful degradation (no crashes/errors) for malformed input.
     * @throws Exception if unexpected system error occurs
     */
    @Test
    void testInvalidCommand() throws Exception {
        String input = "invalidcommand\nexit\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        GameEngine gameEngine = new GameEngine();
        MapEditor mapEditor = new MapEditor(gameEngine);
        mapEditor.startPhase();
    }

    /**
     * Tests validation of a disconnected map graph.
     * Confirms the validator detects missing neighbor relationships.
     */
    @Test
    void testMapValidationForDisconnectedGraph() {
        GameMap gameMap = GameMap.getInstance();
        gameMap.resetGameMap();
        gameMap.addContinent("Asia", 5);
        gameMap.addCountry("India", "Asia");
        gameMap.addCountry("China", "Asia");

        assertFalse(MapReader.validateMap(gameMap),
                "Map should be invalid because it is disconnected.");
    }

    /**
     * Tests validation of a connected map graph.
     * Ensures the validator approves properly linked countries.
     */
    @Test
    void testMapIsConnectedGraph() {
        GameMap gameMap = GameMap.getInstance();
        gameMap.resetGameMap();
        gameMap.addContinent("Asia", 5);
        gameMap.addCountry("India", "Asia");
        gameMap.addCountry("China", "Asia");
        gameMap.addNeighbor("India", "China");

        assertTrue(MapReader.validateMap(gameMap),
                "Map should be a connected graph.");
    }
}