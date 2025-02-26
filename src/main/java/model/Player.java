package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable {
    // Interger ID of the player
    private int d_Id;

    // Name of the player
    private String d_Name;

    // List of captured countries of the player
    private List<Country> d_CapturedCountries = new ArrayList<>();

    // Integer to store number reinforcement armies of player
    private int d_ReinforcementArmies;

    // Get ID of the player
    public int getD_Id() {
        return d_Id;
    }

    // Set ID of the player
    // @param p_Id Player ID
    public void setD_Id(int p_Id) {
        this.d_Id = p_Id;
    }

    // Get the name of the player
    public String getD_Name() {
        return d_Name;
    }

    // Set the name of the player
    public void setD_Name(String p_Name) {
        this.d_Name = p_Name;
    }

    // Get the captured countries of the player
    public List<Country> getCapturedCountries() {
        return d_CapturedCountries;
    }

    // Function to create a list of list of countries assigned to the player
    public String createACaptureList(List<Country> p_Capture) {
        String l_Result = "";
        for (Country l_Capture : p_Capture) {
            l_Result += l_Capture.getD_CountryName() + "-";
        }
        return l_Result.length() > 0 ? l_Result.substring(0, l_Result.length() - 1) : "";
    }

    // Function to get reinforcement countries of each player
    public int getReinforcementArmies() {
        return d_ReinforcementArmies;
    }
}
