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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Benevolent computer player strategy that focuses on defense.
 * @author Sakshi Sudhir Mulik
 * @version 1.0.0
 */
public class BenevolentPlayerStrategy extends PlayerStrategy implements Serializable {

    private static final Random d_Random = new Random();
    private LogEntryBuffer d_Logger = LogEntryBuffer.getInstance();
    private static GameMap d_GameMap;

    @Override
    public String createCommand() {
        d_GameMap = GameMap.getInstance();
        d_Player = d_GameMap.getD_CurrentPlayer(); // Using getD_CurrentPlayer() instead of getCurrentPlayer()
        d_Logger.log("Issuing Orders for Benevolent Player - " + d_Player.getD_Name());

        Country l_WeakestCountry = getWeakestCountry(d_Player);
        if (Objects.isNull(l_WeakestCountry)) {
            return "pass";
        }

        // Deploy armies to weakest country
        deployToWeakest(l_WeakestCountry);

        // Use diplomacy card if available
        if (useDiplomacyCard()) {
            return "pass";
        }

        // Reinforce weakest country from neighbors
        reinforceWeakestFromNeighbors(l_WeakestCountry);

        return "pass";
    }
    /**
     * Gets the weakest country (lowest army count) owned by the player.
     * @param p_Player the player whose countries to check
     * @return the weakest country, or null if none found
     */
    private Country getWeakestCountry(Player p_Player) {
        List<Country> l_CountryList = p_Player.getCapturedCountries();
        if (l_CountryList.isEmpty()) {
            return null;
        }

        Country l_WeakestCountry = l_CountryList.get(0);
        for (Country l_Country : l_CountryList) {
            if (l_Country.getD_Armies() < l_WeakestCountry.getD_Armies()) {
                l_WeakestCountry = l_Country;
            }
        }
        return l_WeakestCountry;
    }
    /**
     * Deploys all available armies to the weakest country.
     * @param p_WeakestCountry the target country to reinforce
     */
    private void deployToWeakest(Country p_WeakestCountry) {
        List<String> l_Commands = new ArrayList<>();
        l_Commands.add("deploy");
        l_Commands.add(p_WeakestCountry.getD_CountryName());
        l_Commands.add(String.valueOf(d_Player.getD_ArmiesToIssue()));

        Order l_Order = new DeployOrder();
        l_Order.setOrderInfo(OrderCreator.GenerateDeployOrderInfo(
                l_Commands.toArray(new String[0]), d_Player));

        IssueOrder.Commands = l_Order.getOrderInfo().getCommand();
        d_Logger.log(String.format("%s issuing new command: %s",
                d_Player.getD_Name(), IssueOrder.Commands));
        d_Player.issueOrder();
    }
    /**
     * Attempts to use a diplomacy card on a random enemy player.
     * @return true if a diplomacy card was used, false otherwise
     */
    private boolean useDiplomacyCard() {
        for (Card l_Card : d_Player.getPlayerCards()) {
            if (l_Card.getCardType() == CardType.DIPLOMACY) {
                Player l_RandomPlayer = getRandomPlayer();
                if (l_RandomPlayer != null) {
                    List<String> l_Commands = new ArrayList<>();
                    l_Commands.add("negotiate");
                    l_Commands.add(l_RandomPlayer.getD_Name());

                    Order l_Order = new NegotiateOrder();
                    l_Order.setOrderInfo(OrderCreator.GenerateNegotiateOrderInfo(
                            l_Commands.toArray(new String[0]), d_Player));

                    IssueOrder.Commands = l_Order.getOrderInfo().getCommand();
                    d_Logger.log(String.format("%s issuing new command: %s",
                            d_Player.getD_Name(), IssueOrder.Commands));
                    d_Player.issueOrder();
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * Reinforces the weakest country by moving armies from neighboring friendly countries.
     * @param p_WeakestCountry the country to reinforce
     */
    private void reinforceWeakestFromNeighbors(Country p_WeakestCountry) {
        for (Country l_Neighbor : p_WeakestCountry.getD_CountryNeighbors()) {
            if (d_Player.equals(l_Neighbor.getPlayer())) {
                List<String> l_Commands = new ArrayList<>();
                l_Commands.add("advance");
                l_Commands.add(l_Neighbor.getD_CountryName());
                l_Commands.add(p_WeakestCountry.getD_CountryName());
                l_Commands.add(String.valueOf(l_Neighbor.getD_Armies()));

                Order l_Order = new AdvanceOrder();
                l_Order.setOrderInfo(OrderCreator.GenerateAdvanceOrderInfo(
                        l_Commands.toArray(new String[0]), d_Player));

                IssueOrder.Commands = l_Order.getOrderInfo().getCommand();
                d_Logger.log(String.format("%s issuing new command: %s",
                        d_Player.getD_Name(), IssueOrder.Commands));
                d_Player.issueOrder();
            }
        }
    }
    /**
     * Gets a random enemy player adjacent to the current player's territories.
     * @return a random enemy player, or null if none found
     */
    private Player getRandomPlayer() {
        List<Country> l_Enemies = d_Player.getCapturedCountries().stream()
                .flatMap(l_Country -> l_Country.getD_CountryNeighbors().stream())
                .filter(l_Country -> !d_Player.equals(l_Country.getPlayer()))
                .toList();

        if (!l_Enemies.isEmpty()) {
            return l_Enemies.get(d_Random.nextInt(l_Enemies.size())).getPlayer();
        }
        return null;
    }
}