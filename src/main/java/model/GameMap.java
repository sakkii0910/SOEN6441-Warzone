package model;

import model.abstractClasses.GamePhase;
import model.strategy.player.PlayerStrategy;
import utils.logger.LogEntryBuffer;

import java.util.*;

/**
 * Represents the game map for the Warzone game.
 * @author  Sakshi Sudhir Mulik
 * @author Taha Mirza
 * @author Shariq Anwar
 */
public class GameMap {
    private final HashMap<String, Continent> continents;
    private final HashMap<String, Country> countries;
    private static GameMap d_GameMap;

    private final HashMap<String, Player> d_players;

    /**
     * Logger instance
     */
    private LogEntryBuffer d_Logger = LogEntryBuffer.getInstance();

    /**
     * Current Player
     */
    private Player d_CurrentPlayer;



    /**
     * Constructor initializes map components.
     */
    private GameMap() {
        continents = new HashMap<>();
        countries = new HashMap<>();
        d_players = new HashMap<>();
    }


    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static GameMap getInstance() {
        if (Objects.isNull(d_GameMap)) {
            d_GameMap = new GameMap();
        }
        return d_GameMap;
    }

    /**
     * Gets country by name.
     *
     * @param d_CountryName the country name
     * @return the country by name
     */
    public Country getCountryByName(String d_CountryName) {
        return countries.get(d_CountryName);
    }

    /**
     * Get the list of players
     *
     * @return the players
     */
    public HashMap<String, Player> getPlayers() {
        return d_players;
    }

    /**
     * Get a single player
     *
     * @param p_Id Unique Player name
     * @return the player
     */
    public Player getPlayer(String p_Id) {
        return d_players.get(p_Id);
    }


    /**
     * Reset game map.
     */
    public void resetGameMap() {
        GameMap.getInstance().continents.clear();
        GameMap.getInstance().countries.clear();
    }

    /**
     * Checks if the game map is empty.
     *
     * @return true if the game map has no continents and no countries, false otherwise.
     */
    public boolean isGameMapEmpty() {
        GameMap gameMap = GameMap.getInstance();
        return gameMap.continents.isEmpty() && gameMap.countries.isEmpty();
    }

    /**
     * Add neighbor.
     *
     * @param p_CountryName         the country name
     * @param p_NeighborCountryName the neighbor country name
     * @throws IllegalArgumentException the illegal argument exception
     */
    public void addNeighbor(String p_CountryName, String p_NeighborCountryName) throws IllegalArgumentException {
        Country l_CountryOne = countries.get(p_CountryName);
        Country l_NeighbourCountry = countries.get(p_NeighborCountryName);
        if (Objects.isNull(l_CountryOne) || Objects.isNull(l_NeighbourCountry)) {
            throw new IllegalArgumentException("Country " + p_CountryName + " or " + p_NeighborCountryName + " does not exist.");
        }

        l_CountryOne.getD_CountryNeighbors().add(l_NeighbourCountry);
        l_NeighbourCountry.getD_CountryNeighbors().add(l_CountryOne);

        d_Logger.log("Added neighbour for: " + l_CountryOne.getD_CountryName());
    }

    /**
     * Remove neighbor.
     *
     * @param p_CountryName         the country name
     * @param p_NeighborCountryName the neighbor country name
     * @throws IllegalArgumentException the illegal argument exception
     */
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
            d_Logger.log("Removed neighbour for: " + l_CountryOne.getD_CountryName());
        }
    }

    /**
     * Adding country to the game map
     *
     * @param p_CountryName   the country name
     * @param p_ContinentName the continent name
     * @throws IllegalArgumentException the illegal argument exception
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
        d_Logger.log("Added country: " + l_Country.getD_CountryName());
    }

    /**
     * Remove country.
     *
     * @param p_CountryName the country name
     * @throws IllegalArgumentException the illegal argument exception
     */
    public void removeCountry(String p_CountryName) throws IllegalArgumentException {
        Country l_Country = this.getCountry(p_CountryName);
        if (Objects.isNull(l_Country)) {
            throw new IllegalArgumentException("Country " + p_CountryName + " does not exist.");
        }
        continents.get(l_Country.getD_CountryContinent().getD_ContinentName()).getD_ContinentCountries().remove(l_Country);
        countries.remove(l_Country.getD_CountryName());
        d_Logger.log("Country " + p_CountryName + " removed.");
    }

    /**
     * Add continent.
     *
     * @param p_continentName  the continent name
     * @param p_continentValue the continent value
     * @throws IllegalArgumentException the illegal argument exception
     */
    public void addContinent(String p_continentName, int p_continentValue) throws IllegalArgumentException {
        if (continents.containsKey(p_continentName)) {
            throw new IllegalArgumentException("Continent " + p_continentName + " already exists.");
        }

        Continent l_Continent = new Continent();
        l_Continent.setD_ContinentName(p_continentName);
        l_Continent.setD_ContinentArmies(p_continentValue);
        continents.put(l_Continent.getD_ContinentName(), l_Continent);
        d_Logger.log("Added continent: " + l_Continent.getD_ContinentName());
    }

    /**
     * Remove continent.
     *
     * @param p_continentName the continent name
     * @throws IllegalArgumentException the illegal argument exception
     */
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
        d_Logger.log("Continent " + p_continentName + " removed.");
    }

    /**
     * Displays the map in a readable format.
     */
    public void displayMap() {
        d_Logger.log("=======================================================================");
        d_Logger.log("                              GAME MAP                                  ");
        d_Logger.log("=======================================================================");
        System.out.printf("%-15s | %-10s | %-20s | %-30s%n", "Continent", "Armies", "Country", "Neighbors");
        d_Logger.log("-----------------------------------------------------------------------");

        for (Map.Entry<String, Continent> l_ContinentEntry : continents.entrySet()) {
            String l_ContinentName = l_ContinentEntry.getKey();
            Continent l_Continent = l_ContinentEntry.getValue();
            int l_Armies = l_Continent.getD_ContinentArmies();

            // Print continent row
            System.out.printf("%-15s | %-10d | %-20s | %-30s%n", l_ContinentName, l_Armies, "", "");

            // Print countries and their neighbors
            for (Country l_CountryEntry : l_Continent.getD_ContinentCountries()) {
                String l_CountryName = l_CountryEntry.getD_CountryName();

                // Format neighbors as "Neighbors: Country1 -> Country2"
                String l_Neighbors = l_CountryEntry.getD_CountryNeighbors().isEmpty()
                        ? "None"
                        : "Neighbors: " + String.join(" -> ",
                        l_CountryEntry.getD_CountryNeighbors().stream()
                                .map(Country::getD_CountryName)
                                .toList());

                // Print country row
                System.out.printf("%-15s | %-10s | %-20s | %-30s%n", "", "", l_CountryName, l_Neighbors);
            }
            d_Logger.log("-----------------------------------------------------------------------");
        }
        d_Logger.log("=======================================================================");
    }

    /**
     * Gets country.
     *
     * @param countryName the country name
     * @return the country
     */
    public Country getCountry(String countryName) {
        return countries.get(countryName);
    }

    /**
     * Gets continents.
     *
     * @return the continents
     */
    public HashMap<String, Continent> getContinents() {
        return continents;
    }

    /**
     * Gets countries.
     *
     * @return the countries
     */
    public HashMap<String, Country> getCountries() {
        return countries;
    }

    /**
     * Show map.
     */
    public void showMap() {
        // Showing Countries in the Continent and their details
        d_Logger.log("\nThe countries in this Map and their details are : \n");

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

    /**
     * Add player.
     *
     * @param p_playerName the player name
     */
    public void addPlayer(String p_playerName) {
        if (this.getPlayers().containsKey(p_playerName)) {
            d_Logger.log("Player with this name already exists!!");
            return;
        }
        Player l_Player = new Player(PlayerStrategy.getStrategy("human"));
        l_Player.setD_Name(p_playerName);
        this.getPlayers().put(p_playerName, l_Player);
        d_Logger.log("Successfully added Player: " + p_playerName + ".");
    }

    /**
     * Remove player.
     *
     * @param p_PlayerName the player name
     */
    public void removePlayer(String p_PlayerName) {
        Player l_Player = this.getPlayer(p_PlayerName);
        if (Objects.isNull(l_Player)) {
            d_Logger.log("\nPlayer with this name does not exist\n" + p_PlayerName);
            return;
        }
        this.getPlayers().remove(l_Player.getD_Name());
        d_Logger.log("Successfully deleted the player: " + p_PlayerName);
    }

    /**
     * Assign countries to player.
     */
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
            d_Logger.log(l_Country.getD_CountryName() + " has been assigned to " + l_Player.getD_Name());
        }
    }

    /**
     * Sets game phase.
     *
     * @param dGamePhase the d game phase
     */
    public void setGamePhase(GamePhase dGamePhase) {

    }

    public Player getD_CurrentPlayer() {
        return d_CurrentPlayer;
    }

    public void setD_CurrentPlayer(Player d_CurrentPlayer) {
        this.d_CurrentPlayer = d_CurrentPlayer;
    }

    public void flushGameMap() {
        GameMap.getInstance().continents.clear();
        GameMap.getInstance().countries.clear();
        GameMap.getInstance().d_players.clear();
    }
}
