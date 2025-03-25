package utils.logger;

import utils.Observable;
import utils.Observer;

import java.io.Serializable;

public class LogEntryBuffer implements Observable, Serializable {

    private static LogEntryBuffer Logger;

    private LogEntryBuffer() {}

    public void notifyObservers(String p_s) {}

    public void addObserver(Observer p_Observer) {}

    public void clearObservers() {}
}