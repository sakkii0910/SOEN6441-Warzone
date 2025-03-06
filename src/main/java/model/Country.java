package model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Country implements Serializable {
    private int d_CountryId;
    private String d_CountryName;
    private Continent d_CountryContinent;
    private Set<Country> d_CountryNeighbors;
    private int d_CountryXCoordinate;
    private int d_CountryYCoordinate;
    private Player d_Player;

    public int getD_CountryId() {
        return d_CountryId;
    }

    public void setD_CountryId(int d_CountryId) {
        this.d_CountryId = d_CountryId;
    }

    public String getD_CountryName() {
        return d_CountryName;
    }

    public void setD_CountryName(String d_CountryName) {
        this.d_CountryName = d_CountryName;
    }

    public Continent getD_CountryContinent() {
        return d_CountryContinent;
    }

    public void setD_CountryContinent(Continent d_CountryContinent) {
        this.d_CountryContinent = d_CountryContinent;
    }

    public Set<Country> getD_CountryNeighbors() {
        if (d_CountryNeighbors == null) {
            d_CountryNeighbors = new HashSet<>();
        }
        return d_CountryNeighbors;
    }

    public void setD_CountryNeighbors(Set<Country> d_CountryNeighbors) {
        this.d_CountryNeighbors = d_CountryNeighbors;
    }

    // Create country neighbors name string
    public String createANeighborList(Set<Country> p_Neighbors) {
        String l_result = "";
        for (Country l_Neighbor : p_Neighbors) {
            l_result += l_Neighbor.getD_CountryName() + "-";
        }
        return l_result.length() > 0 ? l_result.substring(0, l_result.length() - 1) : "";
    }

    public int getD_CountryXCoordinate() {
        return d_CountryXCoordinate;
    }

    public void setD_CountryXCoordinate(int d_CountryXCoordinate) {
        this.d_CountryXCoordinate = d_CountryXCoordinate;
    }

    public int getD_CountryYCoordinate() {
        return d_CountryYCoordinate;
    }

    public void setD_CountryYCoordinate(int d_CountryYCoordinate) {
        this.d_CountryYCoordinate = d_CountryYCoordinate;
    }

    // Get the player
    public Player getPlayer() {
        return d_Player;
    }

    // Set the player instance for the game play
    // @param p_Player Player instance
    public void setPlayer(Player p_Player) {
        this.d_Player = p_Player;
    }

    public void addArmies(int dNumArmies) {

    }
}
