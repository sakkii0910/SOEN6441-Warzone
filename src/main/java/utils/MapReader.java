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
}
