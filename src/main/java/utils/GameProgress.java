package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

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
            // Write to game file
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
    public static void loadGameProgress(String p_fileName) {
        FileInputStream l_Fs;
        GameMap l_loadedMap;
        try {
            // Read the game file
            l_Fs = new FileInputStream(FILEPATH + p_fileName);
            ObjectInputStream l_Os = new ObjectInputStream(l_Fs);
            l_loadedMap = (GameMap) l_Os.readObject();
            d_Logger.log("Game loaded successfully");
            l_Os.close();
            // Load the game
            GameMap.getInstance().gamePlayBuilder(l_loadedMap);
        } catch (Exception e) {
            d_Logger.log("File could not be loaded. Starting new game.");
            // ADD STARTUP PHASE
        }
    }

    /**
     * Function to list all files
     */
    public static void showFiles() throws IOException {
        if (new File(FILEPATH).exists()) {
            d_Logger.log("\t\t\t Load Game");
            d_Logger.log("\t=======================\n");
            Files.walk(Path.of(FILEPATH))
                    .filter(path -> path.toFile().isFile())
                    .forEach(path -> {
                        d_Logger.log("\t\t " + path.getFileName());
                    });
        } else {
            d_Logger.log("\t\t " + "no saved files found");
        }
    }
}
