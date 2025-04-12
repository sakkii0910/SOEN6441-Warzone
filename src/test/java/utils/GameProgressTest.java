package utils;

import model.GameMap;

import org.junit.jupiter.api.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for GameProgress saving and loading functionality
 *
 * @author Sakshi Sudhir Mulik
 */
public class GameProgressTest {
    /**
     * GameMap instance
     */
    private static GameMap d_GameMap;

    /**
     * Test file name
     */
    private static final String TEST_FILE = "test_game";

    /**
     * Setup test environment before each test
     */
    @BeforeEach
    public void setUp() {
        d_GameMap = GameMap.getInstance();

        // Create test continents
        d_GameMap.addContinent("Asia", 5);
        d_GameMap.addContinent("Europe", 3);

        // Create test countries
        d_GameMap.addCountry("India", "Asia");
        d_GameMap.addCountry("China", "Asia");
        d_GameMap.addCountry("France", "Europe");

        // Set up neighbors
        d_GameMap.addNeighbor("India", "China");
        d_GameMap.addNeighbor("China", "India");

        // Add players
        d_GameMap.addPlayer("Player1");
        d_GameMap.addPlayer("Player2");

        // Assign countries to players
        d_GameMap.assignCountries();
    }

    /**
     * Clean up after each test
     */
    @AfterEach
    public void tearDown() {
        d_GameMap.flushGameMap();
        // Delete test file if it exists
        new File(GameProgress.FILEPATH + TEST_FILE).delete();
    }

    /**
     * Test successful game save
     */
    @Test
    public void testSaveGameProgressSuccess() {
        // Ensure directory exists
        new File(GameProgress.FILEPATH).mkdirs();

        // Test saving
        boolean result = GameProgress.saveGameProgress(d_GameMap, TEST_FILE);

        // Verify file was created
        File savedFile = new File(GameProgress.FILEPATH + TEST_FILE + ".bin");
        assertTrue(savedFile.exists());
        assertTrue(result);

        // Clean up
        savedFile.delete();
    }

    /**
     * Test game save with null GameMap
     */
    @Test
    public void testSaveGameProgressNullMap() {
        assertFalse(GameProgress.saveGameProgress(null, TEST_FILE));
    }

    /**
     * Test successful game load
     */
    @Test
    public void testLoadGameProgressSuccess() {
        // 1. Setup - Ensure directory exists
        new File(GameProgress.FILEPATH).mkdirs();

        // 2. Save the game first
        boolean saveResult = GameProgress.saveGameProgress(d_GameMap, TEST_FILE);
        assertTrue(saveResult);

        // 3. Verify the file was created
        File savedFile = new File(GameProgress.FILEPATH + TEST_FILE + ".bin");
        assertTrue(savedFile.exists());

        // 4. Test loading the game
        boolean loadResult = GameProgress.loadGameProgress(TEST_FILE + ".bin");
        assertTrue(loadResult);

        // 5. Verify the loaded game state
        GameMap loadedMap = GameMap.getInstance();
        assertNotNull(loadedMap);
        assertEquals(d_GameMap.getContinents().size(),
                loadedMap.getContinents().size());

        // 6. Cleanup
        savedFile.delete();
    }

    /**
     * Test loading non-existent file
     */
    @Test
    public void testLoadGameProgressFileNotFound() {
        assertFalse(GameProgress.loadGameProgress("nonexistent_file.bin"));
    }

    /**
     * Test loading corrupted file
     */
    @Test
    public void testLoadGameProgressCorruptedFile() throws IOException {
        // Ensure directory exists
        new File(GameProgress.FILEPATH).mkdirs();

        String corruptFileName = "corrupted_test_file.bin";
        File corruptedFile = new File(GameProgress.FILEPATH + corruptFileName);

        // Create corrupted file
        try (FileOutputStream fos = new FileOutputStream(corruptedFile)) {
            fos.write("This is not a valid game file".getBytes());
        }

        // Test loading
        try {
            assertFalse(GameProgress.loadGameProgress(corruptFileName),
                    "Loading corrupted file should fail");
        } finally {
            // Ensure cleanup happens even if test fails
            if (corruptedFile.exists()) {
                corruptedFile.delete();
            }
        }
    }

    /**
     * Test listing saved files when directory exists
     */
    @Test
    public void testShowFilesWithSavedGames() throws IOException {
        // Create a test file
        GameProgress.saveGameProgress(d_GameMap, TEST_FILE);

        // Test that showFiles doesn't throw exceptions
        GameProgress.showFiles();
    }

    /**
     * Test listing saved files when directory doesn't exist
     */
    @Test
    public void testShowFilesNoSavedGames() throws IOException {
        // Ensure directory doesn't exist
        File dir = new File(GameProgress.FILEPATH);
        if (dir.exists()) {
            for (File file : Objects.requireNonNull(dir.listFiles())) {
                file.delete();
            }
            dir.delete();
        }

        // Test that showFiles doesn't throw exceptions
        GameProgress.showFiles();
    }
}