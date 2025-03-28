package utils.logger;

import utils.Observable;
import utils.Observer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LogEntryBuffer implements Observable, Serializable {

    /**
     * Static object of LogEntryBuffer
     * @author Poorav Panchal
     */
    private static LogEntryBuffer Logger;

    /**
     * List of all observers
     */
    private List<Observer> d_Observers = new ArrayList<>();

    /**
     * Constructor
     */
    private LogEntryBuffer() {}

    /**
     * Function to get instance of LogEntryBuffer
     * @return Logger
     */
    public static LogEntryBuffer getInstance() {
        if(Objects.isNull(Logger)) {
            Logger = new LogEntryBuffer();
        }
        return Logger;
    }

    /**
     * Function gets information from game and notifies the Observer
     * @param p_s message/information
     */
    public void log(String p_s) {
        notifyObservers(p_s);
    }

    /**
     * Function updated the Observer with the message
     * @param p_s message
     */
    public void notifyObservers(String p_s) {
        d_Observers.forEach(p_Observer -> p_Observer.update(p_s));
    }

    /**
     * Function to add observable to the list
     * @param p_Observer Observer to be added
     */
    public void addObserver(Observer p_Observer) {
        d_Observers.add(p_Observer);
    }

    /**
     * Clear logs
     */
    public void clear() {
        clearObservers();
    }

    /**
     * Function to clear logs of each observer
     */
    public void clearObservers() {
        d_Observers.forEach(Observer::clearLogs);
    }
}