package model;
/**
 * Test class for the Country class, which represents a country in the game map.
 */

import org.junit.jupiter.api.Test;
import utils.MapReader;

import static org.junit.jupiter.api.Assertions.*;

class CountryTest {
    /**
     * Tests creating a country and assigning it to a continent.
     */
    @Test
    void testCountryCreation() {
        Continent continent = new Continent();
        continent.setD_ContinentName("Asia");
        Country country = new Country();
        country.setD_CountryName("India");
        country.setD_CountryContinent(continent);
        assertEquals("India", country.getD_CountryName());
        assertEquals(continent, country.getD_CountryContinent());
    }
    /**
     * Tests adding a neighbor to a country.
     */
    @Test
    void testAddNeighbor() {
        Continent continent = new Continent();
        continent.setD_ContinentName("Asia");
        Country country1 = new Country();
        country1.setD_CountryName("India");
        Country country2 = new Country();
        country2.setD_CountryName("China");
        country1.getD_CountryNeighbors().add(country2);
        assertTrue(country1.getD_CountryNeighbors().contains(country2));
    }
    /**
     * Tests removing a neighbor from a country.
     */
    @Test
    void testRemoveNeighbor() {
        Continent continent = new Continent();
        continent.setD_ContinentName("Asia");
        Country country1 = new Country();
        country1.setD_CountryName("India");
        Country country2 = new Country();
        country2.setD_CountryName("China");
        country1.getD_CountryNeighbors().add(country2);
        country1.getD_CountryNeighbors().remove(country2);
        assertFalse(country1.getD_CountryNeighbors().contains(country2));
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