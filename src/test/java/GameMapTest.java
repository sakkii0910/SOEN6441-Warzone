//import model.Country;
//import model.GameMap;
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
//
///**
//* Writing the test cases to Load map , save map, validate map ,
//*  add the country in map
//* @author Sakshi Mulik
//* */
//import java.io.File;
//
//public class GameMapTest {
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
//
//}
