package model;

import java.io.Serializable;
import java.util.Set;

public class Continent implements Serializable {
    private int d_ContinentId;

    private String d_ContinentName;

    private String d_ContinentColor;

    private Set<Country> d_ContinentCountries;

}
