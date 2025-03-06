package model;
/**
 * Test class for the Continent class, which represents a continent in the game map.
 */
import org.junit.jupiter.api.Test;
import utils.MapReader;

import static org.junit.jupiter.api.Assertions.*;


class ContinentTest {
    /**
     * Tests creating a continent and setting its properties.
     */
    @Test
    void testContinentCreation() {
        Continent continent = new Continent();
        continent.setD_ContinentName("Asia");
        continent.setD_ContinentArmies(5);
        assertEquals("Asia", continent.getD_ContinentName());
        assertEquals(5, continent.getD_ContinentArmies());
    }
    /**
     * Tests adding a country to a continent.
     */
    @Test
    void testAddCountry() {
        Continent continent = new Continent();
        continent.setD_ContinentName("Asia");
        Country country = new Country();
        country.setD_CountryName("India");
        continent.getD_ContinentCountries().add(country);
        assertTrue(continent.getD_ContinentCountries().contains(country));
    }
    /**
     * Tests removing a country from a continent.
     */
    @Test
    void testRemoveCountry() {
        Continent continent = new Continent();
        continent.setD_ContinentName("Asia");
        Country country = new Country();
        country.setD_CountryName("India");
        continent.getD_ContinentCountries().add(country);
        continent.getD_ContinentCountries().remove(country);
        assertFalse(continent.getD_ContinentCountries().contains(country));
    }
    /**
     * Tests if the continent is a connected subgraph.
     */
    @Test
    void testContinentIsConnectedSubgraph() {
        Continent continent = new Continent();
        continent.setD_ContinentName("Asia");
        Country country1 = new Country();
        country1.setD_CountryName("India");
        Country country2 = new Country();
        country2.setD_CountryName("China");
        continent.getD_ContinentCountries().add(country1);
        continent.getD_ContinentCountries().add(country2);
        country1.getD_CountryNeighbors().add(country2);
        country2.getD_CountryNeighbors().add(country1);

        // Verify that the continent is a connected subgraph
        assertTrue(MapReader.checkIfContinentConnected(continent), "Continent should be a connected subgraph.");
    }
}