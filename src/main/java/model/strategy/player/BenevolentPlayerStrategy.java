package model.strategy.player;

import controller.IssueOrder;
import model.Card;
import model.CardType;
import model.Country;
import model.GameMap;
import model.Player;
import model.order.AdvanceOrder;
import model.order.DeployOrder;
import model.order.NegotiateOrder;
import model.order.Order;
import model.order.OrderCreator;

import utils.logger.LogEntryBuffer;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Benevolent computer player strategy that focuses on defense.
 *
 * @author Sakshi Sudhir Mulik
 * @author Taha Mirza
 * @version 1.0.0
 */
public class BenevolentPlayerStrategy extends PlayerStrategy implements Serializable {

    private static final Random d_Random = new Random();
    private final LogEntryBuffer d_Logger = LogEntryBuffer.getInstance();

    private int d_Option = 1;
    private Country d_WeakestCountry;
    private Set<Country> d_NeighborCountries;

    @Override
    public String createCommand() {
        GameMap d_GameMap = GameMap.getInstance();
        d_Player = d_GameMap.getD_CurrentPlayer(); // Using getD_CurrentPlayer() instead of getCurrentPlayer()
        d_Logger.log("Issuing Orders for Benevolent Player - " + d_Player.getD_Name());

        if (d_Option == 1) {
            getWeakestCountry(d_Player);
            if (d_WeakestCountry == null) {
                return "pass";
            }
            d_NeighborCountries = d_WeakestCountry.getD_CountryNeighbors()
                    .stream()
                    .filter(c -> c.getPlayer().equals(d_Player))
                    .collect(Collectors.toSet());
            d_Option = 2;
            return deployToWeakest();
        }
        return reinforceWeakestFromNeighbors();
    }

    /**
     * Gets the weakest country (lowest army count) owned by the player.
     *
     * @param p_Player the player whose countries to check
     */
    private void getWeakestCountry(Player p_Player) {
        List<Country> l_CountryList = p_Player.getCapturedCountries();
        if (l_CountryList.isEmpty()) {
            d_WeakestCountry = null;
            return;
        }

        d_WeakestCountry = l_CountryList.get(0);
        for (Country l_Country : l_CountryList) {
            if (l_Country.getD_Armies() < d_WeakestCountry.getD_Armies()) {
                d_WeakestCountry = l_Country;
            }
        }
    }

    /**
     * Deploys all available armies to the weakest country.
     */
    private String deployToWeakest() {
        List<String> l_Commands = new ArrayList<>();
        l_Commands.add("deploy");
        l_Commands.add(d_WeakestCountry.getD_CountryName());
        l_Commands.add(String.valueOf(d_Player.getD_ArmiesToIssue()));

        return String.join(" ", l_Commands);
    }

    /**
     * Reinforces the weakest country by moving armies from neighboring friendly countries.
     */
    private String reinforceWeakestFromNeighbors() {
        Iterator<Country> iterator = d_NeighborCountries.iterator();

        if (iterator.hasNext()) {
            Country l_Neighbor = iterator.next();
            iterator.remove(); // removes the element from the set

            List<String> l_Commands = new ArrayList<>();
            l_Commands.add("advance");
            l_Commands.add(l_Neighbor.getD_CountryName());
            l_Commands.add(d_WeakestCountry.getD_CountryName());
            l_Commands.add(String.valueOf(l_Neighbor.getD_Armies()));

            return String.join(" ", l_Commands);
        } else {
            d_Option = 1;
            return "pass";
        }
    }

}