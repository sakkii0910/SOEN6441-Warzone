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

public class MapReader {

    private static List<String> d_Continents = new ArrayList<>();
    private static HashMap<Integer, String> d_CountryMap = new HashMap<>();
    /**
     * current line
     */
    private static String d_CurrentLine;
    /**
     * buffer for reading
     */
    private static BufferedReader d_Buffer;

    public static void readMap(GameMap p_GameMap, String p_FileName) throws Exception {
        System.out.println("Reading map file: " + p_FileName);

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
            throw new Exception(e.getMessage());
        }
    }

    public static void readContinents(GameMap p_GameMap) throws Exception {
        while ((d_CurrentLine = d_Buffer.readLine()) != null) {
            if (d_CurrentLine.equals("")) {
                break;
            }
            String[] l_ContinentDetails = d_CurrentLine.split(" ");
            p_GameMap.addContinent(l_ContinentDetails[0], Integer.parseInt(l_ContinentDetails[1]));
            d_Continents.add(l_ContinentDetails[0]);
        }
    }

    public static void readCountries(GameMap p_GameMap) throws Exception {
        while ((d_CurrentLine = d_Buffer.readLine()) != null) {
            if (d_CurrentLine.equals("")) {
                break;
            }
            String[] l_CountryDetails = d_CurrentLine.split(" ");
            p_GameMap.addCountry(l_CountryDetails[1], d_Continents.get(Integer.parseInt(l_CountryDetails[2]) - 1));
            d_CountryMap.put(Integer.parseInt(l_CountryDetails[0]), l_CountryDetails[1]);
        }
    }

    public static void readBorders(GameMap p_GameMap) throws Exception {
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

    public static boolean saveMap(GameMap p_GameMap, String p_FileName) throws Exception {
        System.out.println("Saving map file: " + p_FileName);

        // create a new file or overwrite the existing file
        File l_File = new File("maps/" + p_FileName);
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

        return true;
    }

    public static HashMap<Integer, String> createContinentList(GameMap p_GameMap) {
        HashMap<Integer, String> l_CountryMap = new HashMap<>();
        int counter = 1;
        for (Continent l_Continent : p_GameMap.getContinents().values()) {
            l_CountryMap.put(counter++, l_Continent.getD_ContinentName());
        }
        return l_CountryMap;
    }

    public static HashMap<Integer, String> createCountryList(GameMap p_GameMap) {
        HashMap<Integer, String> l_CountryMap = new HashMap<>();
        int counter = 1;
        for (Country l_Country : p_GameMap.getCountries().values()) {
            l_CountryMap.put(counter++, l_Country.getD_CountryName());
        }
        return l_CountryMap;
    }

}
