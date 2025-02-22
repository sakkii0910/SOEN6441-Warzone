package model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Continent implements Serializable {
    private int d_ContinentId;
    private String d_ContinentName;
    private Set<Country> d_ContinentCountries;
    private String d_ContinentColor;
    private int d_ContinentArmies;

    public int getD_ContinentId() {
        return d_ContinentId;
    }

    public void setD_ContinentId(int d_ContinentId) {
        this.d_ContinentId = d_ContinentId;
    }

    public String getD_ContinentName() {
        return d_ContinentName;
    }

    public void setD_ContinentName(String d_ContinentName) {
        this.d_ContinentName = d_ContinentName;
    }

    public Set<Country> getD_ContinentCountries() {
        if (d_ContinentCountries == null) {
            d_ContinentCountries = new HashSet<>();
        }
        return d_ContinentCountries;
    }

    public void setD_ContinentCountries(Set<Country> d_ContinentCountries) {
        this.d_ContinentCountries = d_ContinentCountries;
    }

    public String getD_ContinentColor() {
        return d_ContinentColor;
    }

    public void setD_ContinentColor(String d_ContinentColor) {
        this.d_ContinentColor = d_ContinentColor;
    }

    public int getD_ContinentArmies() {
        return d_ContinentArmies;
    }

    public void setD_ContinentArmies(int d_ContinentArmies) {
        this.d_ContinentArmies = d_ContinentArmies;
    }
}
