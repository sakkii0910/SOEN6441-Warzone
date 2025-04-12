package model.strategy.player;

import controller.IssueOrder;

import model.CardType;
import model.Country;
import model.GameMap;

import model.order.AdvanceOrder;
import model.order.BombOrder;
import model.order.DeployOrder;
import model.order.Order;
import model.order.OrderCreator;

import utils.logger.LogEntryBuffer;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Aggressive computer player strategy that focuses on attack.
 *
 * @author Sakshi Sudhir Mulik
 * @version 1.0.0
 */
public class AggressivePlayerStrategy extends PlayerStrategy implements Serializable {

    private final LogEntryBuffer d_Logger = LogEntryBuffer.getInstance();
    private List<Country> d_OrderedCountries;
    private final List<String> d_Commands = new ArrayList<>();
    private boolean calculated = false;

    @Override
    public String createCommand() {
        GameMap l_GameMap = GameMap.getInstance();
        d_Player = l_GameMap.getD_CurrentPlayer();

        if (!calculated) {
            if (!d_Player.getCapturedCountries().isEmpty()) {
                createOrderedCountryList();
                deployToStrongest();
                tryBombOrAttack();
                moveToStrategicPosition();
                calculated = true;
            } else
                return "pass";
        }

        if (!d_Commands.isEmpty()) {
            d_Logger.log("Issuing Orders for Aggressive Player - " + d_Player.getD_Name());
            return d_Commands.remove(0);
        } else {
            calculated = false;
            return "pass";
        }


    }

    /**
     * Creates an ordered list of countries owned by the player, sorted by army count in descending order.
     */
    private void createOrderedCountryList() {
        d_OrderedCountries = d_Player.getCapturedCountries().stream()
                .sorted(Comparator.comparingInt(Country::getD_Armies).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Deploys all available armies to the player's strongest country.
     */
    private void deployToStrongest() {
        Country l_Strongest = d_OrderedCountries.get(0);
        List<String> l_Commands = new ArrayList<>();
        l_Commands.add("deploy");
        l_Commands.add(l_Strongest.getD_CountryName());
        l_Commands.add(String.valueOf(d_Player.getD_ArmiesToIssue()));

        d_Commands.add(String.join(" ", l_Commands));
    }

    /**
     * Attempts to use a bomb card or execute an attack from the strongest country.
     *
     * @return true if an attack or bomb order was executed, false otherwise
     */
    private void tryBombOrAttack() {
        // Try to use bomb card first
        if (hasBombCard()) {
            Country l_Target = findBestBombTarget();
            if (l_Target != null) {
                executeBombOrder(l_Target);
                return;
            }
        }

        // If no bomb available, attack with strongest country
        executeStrongestAttack();
    }

    /**
     * Checks if the player has a bomb card.
     *
     * @return true if player has a bomb card, false otherwise
     */
    private boolean hasBombCard() {
        return d_Player.getPlayerCards().stream()
                .anyMatch(l_Card -> l_Card.getCardType() == CardType.BOMB);
    }

    /**
     * Finds the best enemy country to bomb (adjacent with highest army count).
     *
     * @return the best target country for bombing, or null if none found
     */
    private Country findBestBombTarget() {
        return d_Player.getCapturedCountries().stream()
                .flatMap(l_Country -> l_Country.getD_CountryNeighbors().stream())
                .filter(l_Country -> !d_Player.equals(l_Country.getPlayer()))
                .max(Comparator.comparingInt(Country::getD_Armies))
                .orElse(null);
    }

    /**
     * Executes a bomb order on the specified target country.
     *
     * @param p_Target the target country to bomb
     */
    private void executeBombOrder(Country p_Target) {
        List<String> l_Commands = new ArrayList<>();
        l_Commands.add("bomb");
        l_Commands.add(p_Target.getD_CountryName());

        d_Commands.add(String.join(" ", l_Commands));
//        Order l_Order = new BombOrder();
//        l_Order.setOrderInfo(OrderCreator.GenerateBombOrderInfo(
//                l_Commands.toArray(new String[0]), d_Player));
//
//        IssueOrder.Commands = l_Order.getOrderInfo().getCommand();
//        d_Logger.log(String.format("%s issuing new command: %s",
//                d_Player.getD_Name(), IssueOrder.Commands));
//        d_Player.issueOrder();
    }

    /**
     * Attempts to execute an attack from the strongest country to an adjacent enemy.
     *
     * @return true if an attack was executed, false otherwise
     */
    private void executeStrongestAttack() {
        for (Country l_Country : d_OrderedCountries) {
            Optional<Country> l_Enemy = l_Country.getD_CountryNeighbors().stream()
                    .filter(l_Neighbor -> !d_Player.equals(l_Neighbor.getPlayer()))
                    .findFirst();

            if (l_Enemy.isPresent() && l_Country.getD_Armies() > 0) {
                executeAttackOrder(l_Country, l_Enemy.get());
            }
        }
    }

    /**
     * Executes an attack order from one country to another.
     *
     * @param p_From the attacking country
     * @param p_To   the target country
     */
    private void executeAttackOrder(Country p_From, Country p_To) {
        List<String> l_Commands = new ArrayList<>();
        l_Commands.add("advance");
        l_Commands.add(p_From.getD_CountryName());
        l_Commands.add(p_To.getD_CountryName());
        l_Commands.add(String.valueOf(p_From.getD_Armies()));

        d_Commands.add(String.join(" ", l_Commands));
//        Order l_Order = new AdvanceOrder();
//        l_Order.setOrderInfo(OrderCreator.GenerateAdvanceOrderInfo(
//                l_Commands.toArray(new String[0]), d_Player));
//
//        IssueOrder.Commands = l_Order.getOrderInfo().getCommand();
//        d_Logger.log(String.format("%s issuing new command: %s",
//                d_Player.getD_Name(), IssueOrder.Commands));
//        d_Player.issueOrder();
    }

    /**
     * Moves armies to a strategic neighboring country that borders enemies.
     */
    private void moveToStrategicPosition() {
        Country l_From = d_OrderedCountries.get(0);
        if (l_From.getD_Armies() <= 0) return;

        Country l_To = findStrategicNeighbor(l_From);
        if (l_To != null) {
            executeMoveOrder(l_From, l_To);
        }
    }

    /**
     * Finds the most strategic neighbor (with enemy neighbors and highest army count).
     *
     * @param p_From the country to find neighbors for
     * @return the most strategic neighbor country, or null if none found
     */
    private Country findStrategicNeighbor(Country p_From) {
        return p_From.getD_CountryNeighbors().stream()
                .filter(this::hasEnemyNeighbors)
                .max(Comparator.comparingInt(Country::getD_Armies))
                .orElse(null);
    }

    /**
     * Checks if a country has any enemy neighbors.
     *
     * @param p_Country the country to check
     * @return true if the country has enemy neighbors, false otherwise
     */
    private boolean hasEnemyNeighbors(Country p_Country) {
        return p_Country.getD_CountryNeighbors().stream()
                .anyMatch(l_Neighbor -> !d_Player.equals(l_Neighbor.getPlayer()));
    }

    /**
     * Executes a move order between two friendly countries.
     *
     * @param p_From the source country
     * @param p_To   the destination country
     */
    private void executeMoveOrder(Country p_From, Country p_To) {
        List<String> l_Commands = new ArrayList<>();
        l_Commands.add("advance");
        l_Commands.add(p_From.getD_CountryName());
        l_Commands.add(p_To.getD_CountryName());
        l_Commands.add(String.valueOf(p_From.getD_Armies()));

        d_Commands.add(String.join(" ", l_Commands));
//        Order l_Order = new AdvanceOrder();
//        l_Order.setOrderInfo(OrderCreator.GenerateAdvanceOrderInfo(
//                l_Commands.toArray(new String[0]), d_Player));
//
//        IssueOrder.Commands = l_Order.getOrderInfo().getCommand();
//        d_Logger.log(String.format("%s issuing new command: %s",
//                d_Player.getD_Name(), IssueOrder.Commands));
//        d_Player.issueOrder();
    }
}