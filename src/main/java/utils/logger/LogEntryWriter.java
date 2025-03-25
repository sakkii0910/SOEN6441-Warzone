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

    public void update(String p_s) {}

    public void clearLogs() {}
}
