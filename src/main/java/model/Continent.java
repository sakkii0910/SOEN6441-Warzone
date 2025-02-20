package model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Continent implements Serializable {
    private int d_ContinentId;

    private String d_ContinentName;

    private String d_ContinentColor;

    private int d_AwardedArmies;

    private Set<Country> d_ContinentCountries;

    public int getContinentId() {
        return d_ContinentId;
    }

    public void setContinentId(int p_ContinentId) {
        this.d_ContinentId = p_ContinentId;
    }

    public String getContinentName() {
        return d_ContinentName;
    }

    public void setContinentName(String p_ContinentName) {
        this.d_ContinentName = p_ContinentName;
    }

    public String getContinentColor() {
        return d_ContinentColor;
    }

    public void setContinentColor(String p_ContinentColor) {
        this.d_ContinentColor = p_ContinentColor;
    }

    public int getAwardedArmies() {
        return d_AwardedArmies;
    }

    public void setAwardedArmies(int p_AwardedArmies) {
        this.d_AwardedArmies = p_AwardedArmies;
    }

    public Set<Country> getContinentCountries() {
        if (d_ContinentCountries == null) {
            d_ContinentCountries = new HashSet<>();
        }
        return d_ContinentCountries;
    }
}
