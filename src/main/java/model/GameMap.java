package model;

import java.util.*;

/**
 * Represents the game map for the Warzone game.
 * Handles loading, saving, validating, editing, and displaying maps.
 *
 * @author Sakshi Mulik
 */
public class GameMap {
    private HashMap<String, Continent> continents;
    private HashMap<String, Country> countries;
    private static GameMap d_GameMap;

    /**
     * Constructor initializes map components.
     */
    private GameMap() {
        continents = new HashMap<>();
        countries = new HashMap<>();
    }

    public static GameMap getInstance() {
        if (Objects.isNull(d_GameMap)) {
            d_GameMap = new GameMap();
        }
        return d_GameMap;
    }

    public void resetGameMap() {
        GameMap.getInstance().continents.clear();
        GameMap.getInstance().countries.clear();
    }

    public void addNeighbor(String p_CountryName, String p_NeighborCountryName) throws IllegalArgumentException {
        Country l_CountryOne = countries.get(p_CountryName);
        Country l_NeighbourCountry = countries.get(p_NeighborCountryName);
        if (Objects.isNull(l_CountryOne) || Objects.isNull(l_NeighbourCountry)) {
            throw new IllegalArgumentException("Country " + p_CountryName + " or " + p_NeighborCountryName + " does not exist.");
        }

        l_CountryOne.getD_CountryNeighbors().add(l_NeighbourCountry);
        l_NeighbourCountry.getD_CountryNeighbors().add(l_CountryOne);

        System.out.println("Added neighbour for: " + l_CountryOne.getD_CountryName());
    }

    public void removeNeighbor(String p_CountryName, String p_NeighborCountryName) throws IllegalArgumentException {
        Country l_CountryOne = countries.get(p_CountryName);
        Country l_NeighbourCountry = countries.get(p_NeighborCountryName);
        if (Objects.isNull(l_CountryOne) || Objects.isNull(l_NeighbourCountry)) {
            throw new IllegalArgumentException("Country " + p_CountryName + " or " + p_NeighborCountryName + " does not exist.");
        } else if (!l_CountryOne.getD_CountryNeighbors().contains(l_NeighbourCountry)) {
            throw new IllegalArgumentException("Country " + p_NeighborCountryName + " is not a neighbor of " + l_CountryOne.getD_CountryName());
        } else {
            l_CountryOne.getD_CountryNeighbors().remove(l_NeighbourCountry);
            l_NeighbourCountry.getD_CountryNeighbors().remove(l_CountryOne);
            System.out.println("Removed neighbour for: " + l_CountryOne.getD_CountryName());
        }

    }

    /**
     * Adding country to the game map
     */
    public void addCountry(String p_CountryName, String p_ContinentName) throws IllegalArgumentException {
        if (countries.containsKey(p_CountryName)) {
            throw new IllegalArgumentException("Country " + p_CountryName + " already exists.");
        }
        if (!continents.containsKey(p_ContinentName)) {
            throw new IllegalArgumentException("Continent " + p_ContinentName + " does not exist.");
        }

        Country l_Country = new Country();
        l_Country.setD_CountryName(p_CountryName);
        l_Country.setD_CountryContinent(continents.get(p_ContinentName));
        countries.put(p_CountryName, l_Country);
        continents.get(p_ContinentName).getD_ContinentCountries().add(l_Country);
        System.out.println("Added country: " + l_Country.getD_CountryName());
    }

    public void removeCountry(String p_CountryName) throws IllegalArgumentException {
        Country l_Country = this.getCountry(p_CountryName);
        if (Objects.isNull(l_Country)) {
            throw new IllegalArgumentException("Country " + p_CountryName + " does not exist.");
        }
        continents.get(l_Country.getD_CountryContinent().getD_ContinentName()).getD_ContinentCountries().remove(l_Country);
        countries.remove(l_Country.getD_CountryName());
        System.out.println("Country " + p_CountryName + " removed.");
    }

    public void addContinent(String p_continentName, int p_continentValue) throws IllegalArgumentException {
        if (continents.containsKey(p_continentName)) {
            throw new IllegalArgumentException("Continent " + p_continentName + " already exists.");
        }

        Continent l_Continent = new Continent();
        l_Continent.setD_ContinentName(p_continentName);
        l_Continent.setD_ContinentArmies(p_continentValue);
        continents.put(l_Continent.getD_ContinentName(), l_Continent);
        System.out.println("Added continent: " + l_Continent.getD_ContinentName());
    }

    public void removeContinent(String p_continentName) throws IllegalArgumentException {
        if (continents.containsKey(p_continentName)) {
            Set<Country> l_Countries = continents.get(p_continentName).getD_ContinentCountries();
            for (Country l_Country : l_Countries) {
                countries.remove(l_Country.getD_CountryName());
            }
            continents.remove(p_continentName);
        } else {
            throw new IllegalArgumentException("Continent " + p_continentName + " does not exist.");
        }
        System.out.println("Continent " + p_continentName + " removed.");
    }

    /**
     * Displays the map in a readable format.
     */
    public void displayMap() {
        System.out.println("=======================================================================");
        System.out.println("                              GAME MAP                                  ");
        System.out.println("=======================================================================");
        System.out.printf("%-15s | %-10s | %-20s | %-30s%n", "Continent", "Armies", "Country", "Neighbors");
        System.out.println("-----------------------------------------------------------------------");

        for (Map.Entry<String, Continent> l_ContinentEntry : continents.entrySet()) {
            String l_ContinentName = l_ContinentEntry.getKey();
            Continent l_Continent = l_ContinentEntry.getValue();
            int l_Armies = l_Continent.getD_ContinentArmies();

            System.out.printf("%-15s | %-10d | %-20s | %-30s%n", l_ContinentName, l_Armies, "", "");

            for (Country l_CountryEntry : l_Continent.getD_ContinentCountries()) {
                String l_CountryName = l_CountryEntry.getD_CountryName();

                String l_Neighbors = l_CountryEntry.getD_CountryNeighbors().isEmpty() ? "None" : String.join(", ",
                        l_CountryEntry.getD_CountryNeighbors().stream().map(Country::getD_CountryName).toList());

                System.out.printf("%-15s | %-10s | %-20s | %-30s%n", "", "", l_CountryName, l_Neighbors);
            }
            System.out.println("-----------------------------------------------------------------------");
        }
        System.out.println("=======================================================================");
    }


    public Country getCountry(String countryName) {
        return countries.get(countryName);
    }

    public HashMap<String, Continent> getContinents() {
        return continents;
    }

    public HashMap<String, Country> getCountries() {
        return countries;
    }
}
