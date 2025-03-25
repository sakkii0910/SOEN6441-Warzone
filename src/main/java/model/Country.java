package model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * The type Country.
 */
public class Country implements Serializable {
    private int d_CountryId;
    private String d_CountryName;
    private Continent d_CountryContinent;
    private Set<Country> d_CountryNeighbors;
    private int d_CountryXCoordinate;
    private int d_CountryYCoordinate;
    private Player d_Player;
    private int d_Armies;

    /**
     * Gets country id.
     *
     * @return the country id
     */
    public int getD_CountryId() {
        return d_CountryId;
    }

    /**
     * Sets country id.
     *
     * @param d_CountryId the country id
     */
    public void setD_CountryId(int d_CountryId) {
        this.d_CountryId = d_CountryId;
    }

    /**
     * Gets country name.
     *
     * @return the country name
     */
    public String getD_CountryName() {
        return d_CountryName;
    }

    /**
     * Sets country name.
     *
     * @param d_CountryName the country name
     */
    public void setD_CountryName(String d_CountryName) {
        this.d_CountryName = d_CountryName;
    }

    /**
     * Gets country continent.
     *
     * @return the country continent
     */
    public Continent getD_CountryContinent() {
        return d_CountryContinent;
    }

    /**
     * Sets country continent.
     *
     * @param d_CountryContinent the country continent
     */
    public void setD_CountryContinent(Continent d_CountryContinent) {
        this.d_CountryContinent = d_CountryContinent;
    }

    /**
     * Gets country neighbors.
     *
     * @return the country neighbors
     */
    public Set<Country> getD_CountryNeighbors() {
        if (d_CountryNeighbors == null) {
            d_CountryNeighbors = new HashSet<>();
        }
        return d_CountryNeighbors;
    }

    /**
     * Sets country neighbors.
     *
     * @param d_CountryNeighbors the country neighbors
     */
    public void setD_CountryNeighbors(Set<Country> d_CountryNeighbors) {
        this.d_CountryNeighbors = d_CountryNeighbors;
    }

    /**
     * Create a neighbor list string.
     *
     * @param p_Neighbors the neighbors
     * @return the string
     */
    public String createANeighborList(Set<Country> p_Neighbors) {
        String l_result = "";
        for (Country l_Neighbor : p_Neighbors) {
            l_result += l_Neighbor.getD_CountryName() + "-";
        }
        return !l_result.isEmpty() ? l_result.substring(0, l_result.length() - 1) : "";
    }

    /**
     * Gets country x coordinate.
     *
     * @return the country x coordinate
     */
    public int getD_CountryXCoordinate() {
        return d_CountryXCoordinate;
    }

    /**
     * Sets country x coordinate.
     *
     * @param d_CountryXCoordinate the country x coordinate
     */
    public void setD_CountryXCoordinate(int d_CountryXCoordinate) {
        this.d_CountryXCoordinate = d_CountryXCoordinate;
    }

    /**
     * Gets country y coordinate.
     *
     * @return the country y coordinate
     */
    public int getD_CountryYCoordinate() {
        return d_CountryYCoordinate;
    }

    /**
     * Sets country y coordinate.
     *
     * @param d_CountryYCoordinate the country y coordinate
     */
    public void setD_CountryYCoordinate(int d_CountryYCoordinate) {
        this.d_CountryYCoordinate = d_CountryYCoordinate;
    }

    /**
     * Gets player.
     *
     * @return the player
     */
    public Player getPlayer() {
        return d_Player;
    }

    /**
     * Sets player.
     *
     * @param p_Player the player
     */
    public void setPlayer(Player p_Player) {
        this.d_Player = p_Player;
    }

    /**
     * Add/Deploy armies.
     *
     * @param dNumArmies the num armies
     */
    public void setD_Armies(int dNumArmies) {
        d_Armies += dNumArmies;
    }

    /**
     * Gets armies deployed in a country.
     *
     * @return the armies
     */
    public int getD_Armies() {
        return d_Armies;
    }
}
