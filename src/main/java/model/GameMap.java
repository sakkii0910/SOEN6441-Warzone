package model;

import java.io.*;
import java.util.*;

/**
 * Represents the game map for the Warzone game.
 * Handles loading, saving, validating, editing, and displaying maps.
 * @author Sakshi Mulik
 */
public class GameMap {
    private Map<String, Continent> continents;
    private Map<String, Country> countries;

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
                            continents.put(continentName, new Continent(continentName, bonusValue));
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
                            Country country = new Country(countryName, continent);
                            countries.put(countryName, country);
                            continent.addCountry(country);
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
                                    country.addNeighbor(neighbor);
                                    // Debugging: Print border being added
                                    System.out.println("Added border between " + country.getName() + " and " + neighbor.getName());
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

    /*
    public void addNeighbor(String country1Name, String country2Name) {
        Country country1 = countries.get(country1Name);
        Country country2 = countries.get(country2Name);

        if (country1 == null || country2 == null) {
            System.out.println("One or both countries do not exist.");
            return;
        }

        country1.addNeighbor(country2);
        country2.addNeighbor(country1);
    }
*/
    /**
     * Adding country to the game map
     */
    public boolean addCountry(String continentName, String countryName) {
        Continent continent = getContinent(continentName);
        if (continent == null) {
            continent = new Continent(continentName, 0);
            addContinent(continent);
        }
        Country country = new Country(countryName, continent);
        continent.addCountry(country);
        countries.put(countryName, country);
        return true;
    }

    private Continent getContinent(String continentName) {
        return continents.get(continentName);
    }

    private void addContinent(Continent continent) {
        continents.put(continent.getName(), continent);
    }

    /**
     * Adding border to the map neighboring two countries
     */
    public void addBorder(String country1Name, String country2Name) {
        Country country1 = countries.get(country1Name);
        Country country2 = countries.get(country2Name);

        if (country1 == null || country2 == null) {
            System.out.println("One or both countries do not exist.");
            return;
        }

        country1.getNeighbors().add(country2);
        country2.getNeighbors().add(country1);
    }

    /**
     * Saves the current map to a file.
     */
    public boolean saveMap(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("[Continents]\n");
            for (Continent continent : continents.values()) {
                writer.write(continent.getName() + "=" + continent.getBonusValue() + "\n");
            }

            writer.write("\n[Territories]\n");
            for (Country country : countries.values()) {
                writer.write(country.getName() + "," + country.getContinent().getName());
                for (Country neighbor : country.getNeighbors()) {
                    writer.write("," + neighbor.getName());
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
            if (continent.getCountries().isEmpty()) {
                System.out.println("Validation failed: Continent " + continent.getName() + " has no countries.");
                return false;
            }
        }

        // Validating countries
        for (Country country : countries.values()) {
            if (country.getContinent() == null) {
                System.out.println("Validation failed: Country " + country.getName() + " has no continent.");
                return false;
            }
        }

        // Validating borders
        for (Country country : countries.values()) {
            for (Country neighbor : country.getNeighbors()) {
                if (!countries.containsKey(neighbor.getName())) {
                    System.out.println("Validation failed: Country " + country.getName() + " has invalid neighbor " + neighbor.getName());
                    return false;
                }
            }
        }

        return true;
    }


    /**
     * Displays the map in a readable format.
     */
    public void displayMap() {
        System.out.println("=== Game Map ===");
        for (Continent continent : continents.values()) {
            System.out.println("Continent: " + continent.getName() + " (Bonus: " + continent.getBonusValue() + ")");
            for (Country country : continent.getCountries()) {
                System.out.print(" - " + country.getName() + " -> ");
                for (Country neighbor : country.getNeighbors()) {
                    System.out.print(neighbor.getName() + " ");
                }
                System.out.println();
            }
        }
    }

    public Country getCountry(String countryName) {
        return countries.get(countryName);
    }
}
