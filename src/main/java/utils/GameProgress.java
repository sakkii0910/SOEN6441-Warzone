package utils;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import model.GameMap;
import utils.logger.LogEntryBuffer;

public class GameProgress {

    /**
     * File path
     */
    static String FILEPATH = "savedFiles/";

    /**
     * LogEntry Buffer Instance
     */
    private static LogEntryBuffer d_Logger = LogEntryBuffer.getInstance();

    /**
     * Function to save game progress
     * @return
     */
    public static boolean saveGameProgress(GameMap p_GameMap, String p_name) {
        try {
            FileOutputStream l_Fs = new FileOutputStream(FILEPATH + p_name + ".bin");
            ObjectOutputStream l_Os = new ObjectOutputStream(l_Fs);
            l_Os.writeObject(p_GameMap);
            l_Os.flush();
            l_Fs.close();
            l_Os.close();
            d_Logger.log("Game saved successfully to file ./" + FILEPATH + p_name + ".bin");
            p_GameMap.flushGameMap();
            return true;
        } catch (Exception e) {
            d_Logger.log(e.toString());
            return false;
        }
    }

    /**
     * Function to load game
     */
    public static void loadGameProgress() {

    }

    /**
     * Function to list all files
     */
    public static void showFiles() {

    }
}
