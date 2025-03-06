package model;

/**
 * Represents an abstract order in the game.
 * All specific order types should extend this class.
 */
public abstract class Order {
    
    /**
     * Executes the order.
     */
    public abstract void execute();
}
