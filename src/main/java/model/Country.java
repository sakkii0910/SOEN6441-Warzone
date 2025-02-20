package model;

import java.io.Serializable;
import java.util.Set;

public class Country implements Serializable {

    public int d_CountryIndex;

    private int d_CountryId;

    private int d_CountryXCoordinate;

    private int d_CountryYCoordinate;

    private int d_CountryContinent;

    private Set<Country> d_CountryNeighbors;



}
