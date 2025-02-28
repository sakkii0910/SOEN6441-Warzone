package model;

import model.abstractClasses.GamePhase;

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
    private GamePhase d_GamePhase;

    private HashMap<String, Player> d_players = new HashMap<>();

    /**
     * Constructor initializes map components.
     */
    private GameMap() {
        continents = new HashMap<>();
        countries = new HashMap<>();
    }

    public GamePhase getGamePhase() {
        return d_GamePhase;
    }

    public void setGamePhase(GamePhase d_GamePhase) {
        this.d_GamePhase = d_GamePhase;
    }

    public static GameMap getInstance() {
        if (Objects.isNull(d_GameMap)) {
            d_GameMap = new GameMap();
        }
        return d_GameMap;
    }

    /**
     * Get the list of players
     */
    public HashMap<String, Player> getPlayers() {
        return d_players;
    }

    /**
     * Get a single player
     *
     * @param p_Id Unique Player name
     */
    public Player getPlayer(String p_Id) {
        return d_players.get(p_Id);
    }


    public void resetGameMap() {
        GameMap.getInstance().continents.clear();
        GameMap.getInstance().countries.clear();
        GameMap.getInstance().d_players.clear();
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

    // Adds player to the game map.     
    // @param p_PlayerName Player name    
    public void addPlayer(String p_playerName) {
        if (this.getPlayers().containsKey(p_playerName)) {
            System.out.println("\nPlayer with this name already exists\n");
            return;
        }
        Player l_Player = new Player();
        l_Player.setD_Name(p_playerName);
        this.getPlayers().put(p_playerName, l_Player);
        System.out.println("Successfully added Player: " + p_playerName);
    }

    // Removes player from game map.
    // @param p_PlayerName Player name
    public void removePlayer(String p_PlayerName) {
        Player l_Player = this.getPlayer(p_PlayerName);
        if (Objects.isNull(l_Player)) {
            System.out.println("\nPlayer with this name does not exist\n" + p_PlayerName);
            return;
        }
        this.getPlayers().remove(l_Player.getD_Name());
        System.out.println("Successfully deleted the player: " + p_PlayerName);
    }

    // Assign country to player
    public void assignCountries() {
        List<Player> l_Players = new ArrayList<>(d_GameMap.getPlayers().values());
        List<Country> l_CountryList = new ArrayList<>(d_GameMap.getCountries().values());

        // Shuffle country list
        Collections.shuffle(l_CountryList);

        int l_PlayerCount = l_Players.size();
        for (int i = 0; i < l_CountryList.size(); i++) {
            // select player sequentially
            Player l_Player = l_Players.get(i % l_PlayerCount);
            Country l_Country = l_CountryList.get(i);
            // add country to player's captured countries
            l_Player.getCapturedCountries().add(l_Country);
            // assign player to country
            l_Country.setPlayer(l_Player);
            System.out.println(l_Country.getD_CountryName() + " has been assigned to " + l_Player.getD_Name());
        }
    }
}
