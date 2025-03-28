package utils;

/**
 * Interface for implementation of Observable
 * @author Poorav Panchal
 */
public interface Observable {

    /**
     * Function to send message to Observer
     * @param p_s the message
     */
    public void notifyObservers(String p_s);

    /**
     * Function to add an observer
     * @param p_Observer the observer
     */
    public void addObserver(Observer p_Observer);

    /**
     * Function to clear all observers
     */
    public void clearObservers();
}
