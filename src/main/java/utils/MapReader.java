package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.Continent;
import model.Country;
import model.GameMap;
import utils.logger.LogEntryBuffer;

/**
 * A class to read, validate and save the map file
 * @author Shariq Anwar
 */
public class MapReader {

    /**
     * List of continents
     */
    private static List<String> d_Continents = new ArrayList<>();
    
    /**
     * Map of countries (key: country ID, value: country name)
     */
    private static HashMap<Integer, String> d_CountryMap = new HashMap<>();
    
    /**
     * current line - global
     */
    private static String d_CurrentLine;
    
    /**
     * buffer for reading - global
     */
    private static BufferedReader d_Buffer;

    /**
     * Logger instance
     */
    private static LogEntryBuffer d_Logger = LogEntryBuffer.getInstance();

    /**
     * Reads a map file and creates a game map object.
     *
     * @param p_GameMap  The game map object to populate.
     * @param p_FileName The name of the map file to read.
     * @throws Exception If an error occurs while reading the map file.
     */
    public void readMap(GameMap p_GameMap, String p_FileName) throws Exception {
        d_Logger.log("Reading map file: " + p_FileName);

        try {
            p_GameMap.resetGameMap();

            File l_File = new File("maps/" + p_FileName);
            FileReader l_FileReader = new FileReader(l_File);

            d_Buffer = new BufferedReader(l_FileReader);
            
            while ((d_CurrentLine = d_Buffer.readLine()) != null) {
                if (d_CurrentLine.equals("[continents]")) {
                    readContinents(p_GameMap);
                } else if (d_CurrentLine.equals("[countries]")) {
                    readCountries(p_GameMap);
                } else if (d_CurrentLine.equals("[borders]")) {
                    readBorders(p_GameMap);
                }
            }
            d_Buffer.close();
        } catch (Exception e) {
            throw new Exception("Error reading map file: " + e.getMessage());
        }
    }

    /**
     * Reads the continents from the map file.
     *
     * @param p_GameMap The game map object to populate.
     * @throws Exception If an error occurs while reading the continents.
     */
    public void readContinents(GameMap p_GameMap) throws Exception {
        while ((d_CurrentLine = d_Buffer.readLine()) != null) {
            if (d_CurrentLine.equals("")) {
                break;
            }
            String[] l_ContinentDetails = d_CurrentLine.split(" ");
            p_GameMap.addContinent(l_ContinentDetails[0], Integer.parseInt(l_ContinentDetails[1]));
            d_Continents.add(l_ContinentDetails[0]);
        }
    }

    /**
     * Reads the countries from the map file.
     *
     * @param p_GameMap The game map object to populate.
     * @throws Exception If an error occurs while reading the countries.
     */
    public void readCountries(GameMap p_GameMap) throws Exception {
        while ((d_CurrentLine = d_Buffer.readLine()) != null) {
            if (d_CurrentLine.equals("")) {
                break;
            }
            String[] l_CountryDetails = d_CurrentLine.split(" ");
            p_GameMap.addCountry(l_CountryDetails[1], d_Continents.get(Integer.parseInt(l_CountryDetails[2]) - 1));
            d_CountryMap.put(Integer.parseInt(l_CountryDetails[0]), l_CountryDetails[1]);
        }
    }

    /**
     * Reads the borders from the map file.
     *
     * @param p_GameMap The game map object to populate.
     * @throws Exception If an error occurs while reading the borders.
     */
    public void readBorders(GameMap p_GameMap) throws Exception {
        while ((d_CurrentLine = d_Buffer.readLine()) != null) {
            if (d_CurrentLine.equals("")) {
                break;
            }
            String[] l_BorderDetails = d_CurrentLine.split(" ");
            for (int i = 1; i < l_BorderDetails.length; i++) {
                p_GameMap.addNeighbor(d_CountryMap.get(Integer.parseInt(l_BorderDetails[0])),
                        d_CountryMap.get(Integer.parseInt(l_BorderDetails[i])));
            }
        }
    }

    /**
     * Saves a game map object to a map file.
     *
     * @param p_GameMap  The game map object to save.
     * @param p_FileName The name of the map file to save.
     * @return True if the map was saved successfully, false otherwise.
     * @throws Exception If an error occurs while saving the map file.
     */
    public boolean saveMap(GameMap p_GameMap, String p_FileName) throws Exception {
        d_Logger.log("Saving map file: " + p_FileName);

        try {
            // Create a new file or overwrite the existing file
            File l_File = new File("maps/" + p_FileName + ".domination");
            l_File.createNewFile();

            BufferedWriter l_BufferedWriter = new BufferedWriter(new FileWriter(l_File));
            String d_Content = "";

            d_Content += "[map]\n";
            d_Content += "author=Team 5\n";
            d_Content += "mapname=" + p_FileName + "\n";

            d_Content += "\n[continents]\n";
            HashMap<Integer, String> l_Continents = createContinentList(p_GameMap);
            for (Continent continent : p_GameMap.getContinents().values()) {
                d_Content += continent.getD_ContinentName() + " " + continent.getD_ContinentArmies() + "\n";
            }

            d_Content += "\n[countries]\n";
            HashMap<Integer, String> l_Countries = createCountryList(p_GameMap);
            for (Map.Entry<Integer, String> l_Country : l_Countries.entrySet()) {
                for (Map.Entry<Integer, String> l_Continent : l_Continents.entrySet()) {
                    if (l_Continent.getValue().equals(
                            p_GameMap.getCountry(l_Country.getValue()).getD_CountryContinent().getD_ContinentName())) {
                        d_Content += l_Country.getKey() + " " + l_Country.getValue() + " " + l_Continent.getKey() + "\n";
                        break;
                    }
                }
            }

            d_Content += "\n[borders]\n";
            for (Map.Entry<Integer, String> l_Country : l_Countries.entrySet()) {
                d_Content += l_Country.getKey();
                for (Country l_Neighbor : p_GameMap.getCountry(l_Country.getValue()).getD_CountryNeighbors()) {
                    for (Map.Entry<Integer, String> l_NeighborCountry : l_Countries.entrySet()) {
                        if (l_Neighbor.getD_CountryName().equals(l_NeighborCountry.getValue())) {
                            d_Content += " " + l_NeighborCountry.getKey();
                            break;
                        }
                    }
                }
                d_Content += "\n";
            }

            l_BufferedWriter.write(d_Content);
            l_BufferedWriter.close();

            d_Logger.log("Map saved successfully."); // Debug
            return true;
        } catch (Exception e) {
            System.err.println("Error saving map file: " + e.getMessage()); // Debug
            throw new Exception("Error saving map file: " + e.getMessage());
        }
    }

    /**
     * Validates a game map object.
     *
     * @param p_GameMap The game map object to validate.
     * @return True if the game map is valid, false otherwise.
     */
    public static boolean validateMap(GameMap p_GameMap) {
        if (!isContinentEmpty(p_GameMap)) return false;

        if (!isDuplicateContinent(p_GameMap)) return false;

        if (!isDuplicateCountry(p_GameMap)) return false;

        if (!isDuplicateBorder(p_GameMap)) return false;

        if (checkIfNeighbourExists(p_GameMap)) {
            if (!checkContinentsConnected(p_GameMap)) {
                d_Logger.log("Continents are not connected");
                return false;
            }

            if (!checkIfMapIsConnected(p_GameMap.getCountries())) {
                d_Logger.log("Map is not connected");
                return false;
            }
        }
        else {
            d_Logger.log("Neighbour does not exist");
            return false;
        }
        
        return true;
    }

    /**
     * Checks if the continents are not empty and contain countries.
     * @param p_GameMap
     * @return True if the continents and respective countries are present, false otherwise.
     */
    static boolean isContinentEmpty(GameMap p_GameMap) {
        if (p_GameMap.getContinents().isEmpty())
            return false;

        for (Continent l_Continent : p_GameMap.getContinents().values()) {
            if (l_Continent.getD_ContinentCountries().isEmpty())
                return false;
        }

        return true;
    }

    /**
     * Checks if the continents are not duplicated.
     * @param p_GameMap
     * @return True if the continents are not duplicated, false otherwise.
     */
    static boolean isDuplicateContinent(GameMap p_GameMap) {
        Set<String> l_ContinentSet = new HashSet<>();
        for (Continent l_Continent : p_GameMap.getContinents().values()) {
            if (l_ContinentSet.contains(l_Continent.getD_ContinentName()))
                return false;
            l_ContinentSet.add(l_Continent.getD_ContinentName());
        }
        return true;
    }

    /**
     * Checks if the countries are not duplicated.
     * @param p_GameMap
     * @return True if the countries are not duplicated, false otherwise.
     */
    static boolean isDuplicateCountry(GameMap p_GameMap) {
        HashMap<String, Country> l_Countries = p_GameMap.getCountries();
        Set<String> l_CountrySet = l_Countries.keySet();
        if (l_CountrySet.size() != l_Countries.size())
            return false;
        return true;
    }

    /**
     * Checks if the borders are not duplicated.
     * @param p_GameMap
     * @return True if the borders are not duplicated, false otherwise.
     */
    static boolean isDuplicateBorder(GameMap p_GameMap) {
        HashMap<String, Country> l_Countries = p_GameMap.getCountries();
        for (Country l_Country : l_Countries.values()) {
            Set<Country> l_Neighbors = l_Country.getD_CountryNeighbors();
            Set<String> l_NeighborSet = new HashSet<>();
            for (Country l_Neighbor : l_Neighbors) {
                if (l_NeighborSet.contains(l_Neighbor.getD_CountryName()))
                    return false;
                l_NeighborSet.add(l_Neighbor.getD_CountryName());
            }
        }
        return true;
    }

    /**
     * Checks if the neighbors exist i.e the neighbors of a country are present in the map.
     * @param pGameMap
     * @return True if the neighbors exist, false otherwise.
     */
    static boolean checkIfNeighbourExists(GameMap pGameMap) {
        HashMap<String, Country> l_Countries = pGameMap.getCountries();
        for (Country l_Country : l_Countries.values()) {
            Set<Country> l_Neighbors = l_Country.getD_CountryNeighbors();
            for (Country l_Neighbor : l_Neighbors) {
                if (!l_Countries.containsKey(l_Neighbor.getD_CountryName())) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if the map is strongly connected.
     * @param p_Countries
     * @return True if the map is connected, false otherwise.
     */
    static boolean checkIfMapIsConnected(HashMap<String, Country> p_Countries) {
        List<String> l_ListOfCountries = new ArrayList<>();
        for (String l_Name : p_Countries.keySet()) {
            l_ListOfCountries.add(l_Name.toLowerCase());
        }

        int l_NoOfVertices = l_ListOfCountries.size();
        ConnectedGraph l_Graph = new ConnectedGraph(l_NoOfVertices);
        int l_Temp = 0;
        for (Map.Entry<String, Country> l_Country : p_Countries.entrySet()) {
            Set<Country> l_Neighbors = l_Country.getValue().getD_CountryNeighbors();
            for (Country l_Current : l_Neighbors) {
                int l_Index = l_ListOfCountries.indexOf(l_Current.getD_CountryName().toLowerCase());
                if (l_Index != -1) {
                    l_Graph.addEdge(l_Temp, l_Index);
                }
            }
            l_Temp++;
        }
        return l_Graph.checkIfStronglyConnected();
    }

    /**
     * Checks if the continents are connected i.e each continent is a connected subgraph.
     * @param p_GameMap
     * @return True if the continents are connected, false otherwise.
     */
    public static boolean checkContinentsConnected(GameMap p_GameMap) {
        for (Continent l_Continent : p_GameMap.getContinents().values()) {
            if (!checkIfContinentConnected(l_Continent)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Checks if a continent is connected i.e the countries in the continent are connected.
     * @param p_Continent
     * @return True if the continent is connected, false otherwise.
     */
    public static boolean checkIfContinentConnected(Continent p_Continent) {
        HashMap<String, Country> l_CountriesMap = new HashMap<>();
        Set<Country> l_Countries = p_Continent.getD_ContinentCountries();
        for (Country l_Country : l_Countries) {
            l_CountriesMap.put(l_Country.getD_CountryName(), l_Country);
        }
        return checkIfMapIsConnected(l_CountriesMap);
    }

    /**
     * Helper method - Creates a list of continents.
     *
     * @param p_GameMap The game map object.
     * @return A list of continents.
     */
    public static HashMap<Integer, String> createContinentList(GameMap p_GameMap) {
        HashMap<Integer, String> l_CountryMap = new HashMap<>();
        int counter = 1;
        for (Continent l_Continent : p_GameMap.getContinents().values()) {
            l_CountryMap.put(counter++, l_Continent.getD_ContinentName());
        }
        return l_CountryMap;
    }

    /**
     * Helper method - Creates a list of countries.
     *
     * @param p_GameMap The game map object.
     * @return A list of countries.
     */
    public static HashMap<Integer, String> createCountryList(GameMap p_GameMap) {
        HashMap<Integer, String> l_CountryMap = new HashMap<>();
        int counter = 1;
        for (Country l_Country : p_GameMap.getCountries().values()) {
            l_CountryMap.put(counter++, l_Country.getD_CountryName());
        }
        return l_CountryMap;
    }
}
