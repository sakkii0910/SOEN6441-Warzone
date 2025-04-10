package model.order;

/**
 * Represents an abstract order in the game.
 * All specific order types should extend this class.
 *  * @author Pruthvirajsinh Dodiya
 */
public abstract class Order {
    /**
     * A data member to store the order type
     */
    private String d_Type;
    /**
     * An object of OrderInfo
     */
    private OrderInfo d_OrderInfo;

    /**
     * A function to get order information
     *
     * @return the order information in an object
     */
    public OrderInfo getOrderInfo() {
        return d_OrderInfo;
    }

    /**
     * A function to the set Order information based on the order
     *
     * @param p_OrderInfo Order Information contained in an object of type OrderInfo
     */
    public void setOrderInfo(OrderInfo p_OrderInfo) {
        this.d_OrderInfo = p_OrderInfo;
    }

    /**
     * A function to return the type of order
     *
     * @return String which indicates the type of order
     */
    public String getType() {
        return d_Type;
    }

    /**
     * A function to set the type of order
     *
     * @param p_Type String which indicates the type of order
     */
    public void setType(String p_Type) {
        this.d_Type = p_Type;
    }

    /**
     * A function to be overridden  by the Child class
     *
     * @return false as there is not order to be executed
     */
    public abstract boolean execute();

    /**
     * A function to validate each command.
     *
     * @return true if command is valid else false
     */
    public abstract boolean validateCommand();

    /**
     * Print the command that is executed successfully
     */
    public abstract void printOrderCommand();

}
