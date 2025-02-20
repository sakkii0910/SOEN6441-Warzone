package model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Continent class contains the information about the continent
 */
public class Continent implements Serializable {
    /**
     * Continent ID - The id or index of the continent
     */
    private int d_ContinentId;

    /**
     * Continent Name - The name of the continent
     */
    private String d_ContinentName;

    /**
     * Continent Color - The color of the continent
     */
    private String d_ContinentColor;

    /**
     * Awarded Armies - The number of armies awarded to the player if the player owns all the countries in the continent
     */
    private int d_AwardedArmies;

    /**
     * Continent Countries - The countries in the continent
     */
    private Set<Country> d_ContinentCountries;

    
    
    /**
     * Returns continent id or index
     * @return continent id or index
     */
    public int getContinentId() {
        return d_ContinentId;
    }

    /**
     * Sets continent id or index
     * @param p_ContinentId
     */
    public void setContinentId(int p_ContinentId) {
        this.d_ContinentId = p_ContinentId;
    }

    /**
     * Returns continent name
     * @return continent name
     */
    public String getContinentName() {
        return d_ContinentName;
    }

    /**
     * Sets continent name
     * @param p_ContinentName
     */
    public void setContinentName(String p_ContinentName) {
        this.d_ContinentName = p_ContinentName;
    }
    
    /**
     * Returns continent color
     * @return continent color
     */
    public String getContinentColor() {
        return d_ContinentColor;
    }

    /**
     * Sets continent color
     * @param p_ContinentColor
     */
    public void setContinentColor(String p_ContinentColor) {
        this.d_ContinentColor = p_ContinentColor;
    }

    /**
     * Returns control value or the number of armies awarded to the player if the player owns all the countries in the continent
     * @return awarded armies value
     */
    public int getAwardedArmies() {
        return d_AwardedArmies;
    }

    /**
     * Sets control value or the number of armies awarded to the player if the player owns all the countries in the continent
     * @param p_AwardedArmies
     */
    public void setAwardedArmies(int p_AwardedArmies) {
        this.d_AwardedArmies = p_AwardedArmies;
    }

    /**
     * Returns the countries in the continent
     * @return countries in the continent
     */
    public Set<Country> getContinentCountries() {
        if (d_ContinentCountries == null) {
            d_ContinentCountries = new HashSet<>();
        }
        return d_ContinentCountries;
    }
}
