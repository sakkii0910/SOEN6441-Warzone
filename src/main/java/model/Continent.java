package model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Continent class contains the information about the continent
 */
public class Continent implements Serializable {
    private int d_ContinentId;
    private String d_ContinentName;
    private Set<Country> d_ContinentCountries;
    private String d_ContinentColor;
    private int d_ContinentArmies;

    /**
     * Gets continent id.
     *
     * @return the continent id
     */
    public int getD_ContinentId() {
        return d_ContinentId;
    }

    /**
     * Sets continent id.
     *
     * @param d_ContinentId the continent id
     */
    public void setD_ContinentId(int d_ContinentId) {
        this.d_ContinentId = d_ContinentId;
    }

    /**
     * Gets continent name.
     *
     * @return the continent name
     */
    public String getD_ContinentName() {
        return d_ContinentName;
    }

    /**
     * Sets continent name.
     *
     * @param d_ContinentName the continent name
     */
    public void setD_ContinentName(String d_ContinentName) {
        this.d_ContinentName = d_ContinentName;
    }

    /**
     * Gets continent countries.
     *
     * @return the continent countries
     */
    public Set<Country> getD_ContinentCountries() {
        if (d_ContinentCountries == null) {
            d_ContinentCountries = new HashSet<>();
        }
        return d_ContinentCountries;
    }

    /**
     * Sets continent countries.
     *
     * @param d_ContinentCountries the continent countries
     */
    public void setD_ContinentCountries(Set<Country> d_ContinentCountries) {
        this.d_ContinentCountries = d_ContinentCountries;
    }

    /**
     * Gets continent color.
     *
     * @return the continent color
     */
    public String getD_ContinentColor() {
        return d_ContinentColor;
    }

    /**
     * Sets continent color.
     *
     * @param d_ContinentColor the continent color
     */
    public void setD_ContinentColor(String d_ContinentColor) {
        this.d_ContinentColor = d_ContinentColor;
    }

    /**
     * Gets continent armies.
     *
     * @return the continent armies
     */
    public int getD_ContinentArmies() {
        return d_ContinentArmies;
    }

    /**
     * Sets continent armies.
     *
     * @param d_ContinentArmies the continent armies
     */
    public void setD_ContinentArmies(int d_ContinentArmies) {
        this.d_ContinentArmies = d_ContinentArmies;
    }
}
