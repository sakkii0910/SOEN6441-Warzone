package model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Country implements Serializable {
    private String d_CountryName;
    private Continent d_CountryContinent;
    private Set<Country> d_CountryNeighbors;

    // Constructor
    public Country(String name, Continent continent) {
        this.d_CountryName = name;
        this.d_CountryContinent = continent;
        this.d_CountryNeighbors = new HashSet<>();
    }

    // Getters
    public String getName() {
        return d_CountryName;
    }

    public Continent getContinent() {
        return d_CountryContinent;
    }

    public Set<Country> getNeighbors() {
        return d_CountryNeighbors;
    }


    public void addNeighbor(Country neighbor) {
        d_CountryNeighbors.add(neighbor);
    }

    public boolean hasNeighbor(Country neighbor) {
        return d_CountryNeighbors.contains(neighbor);
    }
}
