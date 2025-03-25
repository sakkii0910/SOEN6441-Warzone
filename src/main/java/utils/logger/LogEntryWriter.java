package utils.logger;

import utils.Observer;
import java.io.*;

/**
 * Class implementing Observer to write to log file
 * @author Poorav Panchal
 */
public class LogEntryWriter implements Observer, Serializable {
    /**
     * File name
     */
    private String l_Filename = "warzone";

    /**
     * Function receives update from Subject and sends it to be written to Log file
     * @param p_s message to be written
     */
    public void update(String p_s) {
        writeLogFile(p_s);
    }

    /**
     * Check if path is a folder
     * @param path
     */
    private void checkDirectory(String path) {
        File dir = new File (path);
        if(!dir.exists() || !dir.isDirectory()) {
            dir.mkdirs();
        }
    }

    /**
     * Function to write log to file
     * @param p_s
     */
    public void writeLogFile(String p_s) {
        PrintWriter l_writer = null;
        try {
            checkDirectory("logFiles");
            l_writer = new PrintWriter(new BufferedWriter(new FileWriter("logFiles/" + l_Filename + ".log", true)));
            l_writer.println(p_s);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            l_writer.close();
        }
    }

    /** 
     * Function clears log before new game start
     */
    public void clearLogs() {
        try {
            checkDirectory("logFiles");
            File l_File = new File("logFiles/" + l_Filename + ".log");
            if(l_File.exists()) {
                l_File.delete();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
