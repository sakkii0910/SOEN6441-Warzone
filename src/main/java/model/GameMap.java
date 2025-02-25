package model;

import java.io.*;
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

    /**
     * Constructor initializes map components.
     */
    public GameMap() {
        continents = new HashMap<>();
        countries = new HashMap<>();
    }

    /**
     * Loads a .map file and parses its content.
     */
    public boolean loadMap(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isContinentSection = false;
            boolean isCountrySection = false;
            boolean isBorderSection = false;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith(";")) continue;

                // Debugging: Printing each line to ensure it's being read correctly
                System.out.println("Reading line: " + line);

                if (line.equalsIgnoreCase("[Continents]")) {
                    isContinentSection = true;
                    isCountrySection = false;
                    isBorderSection = false;
                    continue;
                } else if (line.equalsIgnoreCase("[Territories]")) {
                    isContinentSection = false;
                    isCountrySection = true;
                    isBorderSection = false;
                    continue;
                } else if (line.equalsIgnoreCase("[Borders]")) {
                    isContinentSection = false;
                    isCountrySection = false;
                    isBorderSection = true;
                    continue;
                }

                if (isContinentSection) {
                    String[] parts = line.split("=");
                    if (parts.length == 2) {
                        String continentName = parts[0].trim();
                        try {
                            int bonusValue = Integer.parseInt(parts[1].trim());
                            // Debugging: Print continent being loaded
                            System.out.println("Loaded continent: " + continentName + " with bonus: " + bonusValue);
                            Continent continent = new Continent();
                            continent.setD_ContinentArmies(bonusValue);
                            continent.setD_ContinentName(continentName);
                            continents.put(continentName, continent);
                        } catch (NumberFormatException e) {
                            System.out.println("Error: Invalid bonus value for continent " + continentName);
                        }
                    } else {
                        System.out.println("Warning: Invalid continent format: " + line);
                    }
                } else if (isCountrySection) {
                    String[] parts = line.split(",");
                    if (parts.length >= 3) {
                        String countryName = parts[0].trim();
                        String continentName = parts[1].trim();
                        if (continents.containsKey(continentName)) {
                            Continent continent = continents.get(continentName);
                            Country country = new Country();
                            country.setD_CountryName(countryName);
                            country.setD_CountryContinent(continent);
                            countries.put(countryName, country);
                            continent.getD_ContinentCountries().add(country);
                            // Debugging: Print country being loaded
                            System.out.println("Loaded country: " + countryName + " in continent: " + continentName);
                        } else {
                            System.out.println("Warning: Continent " + continentName + " not found for country " + countryName);
                        }
                    } else {
                        System.out.println("Warning: Invalid country format: " + line);
                    }
                } else if (isBorderSection) {
                    String[] parts = line.split(" ");
                    if (parts.length >= 2) {
                        Country country = countries.get(parts[0].trim());
                        if (country != null) {
                            for (int i = 1; i < parts.length; i++) {
                                Country neighbor = countries.get(parts[i].trim());
                                if (neighbor != null) {
                                    country.getD_CountryNeighbors().add(neighbor);
                                    // Debugging: Print border being added
                                    System.out.println("Added border between " + country.getD_CountryName() + " and " + neighbor.getD_CountryName());
                                } else {
                                    System.out.println("Warning: Neighbor country " + parts[i] + " does not exist.");
                                }
                            }
                        } else {
                            System.out.println("Warning: Country " + parts[0] + " does not exist.");
                        }
                    } else {
                        System.out.println("Warning: Invalid border format: " + line);
                    }
                }
            }

            // Logging the number of continents and countries loaded
            System.out.println("Loaded continents: " + continents.size());
            System.out.println("Loaded countries: " + countries.size());

            return true;
        } catch (IOException e) {
            System.err.println("Error loading map: " + e.getMessage());
            return false;
        }
    }

    /**
     * Saves the current map to a file.
     */
    public boolean saveMap(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("[Continents]\n");
            for (Continent continent : continents.values()) {
                writer.write(continent.getD_ContinentName() + "=" + continent.getD_ContinentArmies() + "\n");
            }

            writer.write("\n[Territories]\n");
            for (Country country : countries.values()) {
                writer.write(country.getD_CountryName() + "," + country.getD_CountryContinent().getD_ContinentName());
                for (Country neighbor : country.getD_CountryNeighbors()) {
                    writer.write("," + neighbor.getD_CountryName());
                }
                writer.write("\n");
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error saving map: " + e.getMessage());
            return false;
        }
    }

    /**
     * Validating the map structure.
     */
    public boolean validateMap() {
        // Validating continents
        for (Continent continent : continents.values()) {
            if (continent.getD_ContinentCountries().isEmpty()) {
                System.out.println("Validation failed: Continent " + continent.getD_ContinentName() + " has no countries.");
                return false;
            }
        }

        // Validating countries
        for (Country country : countries.values()) {
            if (country.getD_CountryContinent() == null) {
                System.out.println("Validation failed: Country " + country.getD_CountryName() + " has no continent.");
                return false;
            }
        }

        // Validating borders
        for (Country country : countries.values()) {
            for (Country neighbor : country.getD_CountryNeighbors()) {
                if (!countries.containsKey(neighbor.getD_CountryName())) {
                    System.out.println("Validation failed: Country " + country.getD_CountryName() + " has invalid neighbor " + neighbor.getD_CountryName());
                    return false;
                }
            }
        }

        return true;
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

    // Method to display map data
    public void showMap() {
        // Showing Countries in the Continent and their details
        System.out.println("\nThe countries in this Map and their details are : \n");
        
        // Define table format
        String l_Table = "|%-20s|%-20s|%-100s|%n";

        System.out.format("---------------------------------------------------------------------------------------------------------%n");
        System.out.format("     Country     | Continent |   Neighbours                                      |%n");
        System.out.format("---------------------------------------------------------------------------------------------------------%n");

        // Iterate over continents using the instance variable directly
        for (Map.Entry<String, Continent> l_ContinentEntry : continents.entrySet()) {
            Continent l_Continent = l_ContinentEntry.getValue();

            // Iterate over countries within the continent
            for (Country l_Country : l_Continent.getD_ContinentCountries()) {
                System.out.format(
                    l_Table,
                    l_Country.getD_CountryName(),
                    l_Continent.getD_ContinentName(),
                    l_Country.createANeighborList(l_Country.getD_CountryNeighbors())
                );
            }
        }

        System.out.format("---------------------------------------------------------------------------------------------------------%n");
    }
}
