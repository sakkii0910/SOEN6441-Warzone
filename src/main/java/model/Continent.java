package model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Continent implements Serializable {
    private int d_ContinentId;
    private String d_ContinentName;
    private int d_BonusValue;
    private Set<Country> d_ContinentCountries;

    // Constructor
    public Continent(String name, int bonusValue) {
        this.d_ContinentName = name;
        this.d_BonusValue = bonusValue; // Initialize bonusValue properly
        this.d_ContinentCountries = new HashSet<>();
    }

    // Getters
    public String getName() {
        return d_ContinentName;
    }

    public int getBonusValue() {
        return d_BonusValue;
    }

    public Set<Country> getCountries() {
        return d_ContinentCountries;
    }

    // Adding a country to this continent
    public void addCountry(Country country) {
        d_ContinentCountries.add(country);
    }
}
