package model.strategy.player;

import controller.IssueOrder;
import model.Card;

import model.Country;
import model.GameMap;
import model.Player;
import model.order.*;
import utils.logger.LogEntryBuffer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import java.util.Random;


/**
 * Random computer player strategy that makes random decisions.
 * @author Sakshi Sudhir Mulik
 * @version 1.0.0
 */
public class RandomPlayerStrategy extends PlayerStrategy implements Serializable {

    private static final Random d_Random = new Random();
    private LogEntryBuffer d_Logger = LogEntryBuffer.getInstance();
    private static GameMap d_GameMap;

    @Override
    public String createCommand() {
        d_GameMap = GameMap.getInstance();
        d_Player = d_GameMap.getD_CurrentPlayer();
        d_Logger.log("Issuing Orders for Random Player - " + d_Player.getD_Name());

        // Randomly choose between different order types
        int l_Choice = d_Random.nextInt(10); // 0-9

        // 40% chance for deploy, 30% for advance, 30% for card usage
        if (l_Choice < 4) {
            return tryRandomDeploy();
        } else if (l_Choice < 7) {
            return tryRandomAdvance();
        } else {
            return tryRandomCard();
        }
    }
    /**
     * Attempts to deploy armies to a random conquered country.
     * @return "pass" after attempting deployment
     */
    private String tryRandomDeploy() {
        Country l_RandomCountry = getRandomConqueredCountry(d_Player);
        if (l_RandomCountry != null && d_Player.getD_ArmiesToIssue() > 0) {
            List<String> l_Commands = new ArrayList<>();
            l_Commands.add("deploy");
            l_Commands.add(l_RandomCountry.getD_CountryName());
            l_Commands.add(String.valueOf(d_Random.nextInt(d_Player.getD_ArmiesToIssue()) + 1));

            Order l_Order = new DeployOrder();
            l_Order.setOrderInfo(OrderCreator.GenerateDeployOrderInfo(
                    l_Commands.toArray(new String[0]), d_Player));

            IssueOrder.Commands = l_Order.getOrderInfo().getCommand();
            d_Logger.log(String.format("%s issuing new command: %s",
                    d_Player.getD_Name(), IssueOrder.Commands));
            d_Player.issueOrder();
        }
        return "pass";
    }
    /**
     * Attempts to advance armies from a random country to a random neighbor.
     * @return "pass" after attempting advance
     */
    private String tryRandomAdvance() {
        Country l_FromCountry = getRandomConqueredCountry(d_Player);
        if (l_FromCountry != null && l_FromCountry.getD_Armies() > 0) {
            Country l_ToCountry = getRandomNeighbor(l_FromCountry);
            if (l_ToCountry != null) {
                List<String> l_Commands = new ArrayList<>();
                l_Commands.add("advance");
                l_Commands.add(l_FromCountry.getD_CountryName());
                l_Commands.add(l_ToCountry.getD_CountryName());
                l_Commands.add(String.valueOf(d_Random.nextInt(l_FromCountry.getD_Armies()) + 1));

                Order l_Order = new AdvanceOrder();
                l_Order.setOrderInfo(OrderCreator.GenerateAdvanceOrderInfo(
                        l_Commands.toArray(new String[0]), d_Player));

                IssueOrder.Commands = l_Order.getOrderInfo().getCommand();
                d_Logger.log(String.format("%s issuing new command: %s",
                        d_Player.getD_Name(), IssueOrder.Commands));
                d_Player.issueOrder();
                return "pass";
            }
        }
        return "pass";
    }
    /**
     * Attempts to use a random card from the player's hand.
     * @return "pass" after attempting card usage
     */
    private String tryRandomCard() {
        if (d_Player.getPlayerCards().isEmpty()) {
            return "pass";
        }

        Card l_RandomCard = d_Player.getPlayerCards().get(
                d_Random.nextInt(d_Player.getPlayerCards().size()));

        switch (l_RandomCard.getCardType()) {
            case BOMB:
                return tryBombCard();
            case BLOCKADE:
                return tryBlockadeCard();
            case AIRLIFT:
                return tryAirliftCard();
            case DIPLOMACY:
                return tryDiplomacyCard();
            default:
                return "pass";
        }
    }
    /**
     * Attempts to use a bomb card on a random unconquered country.
     * @return "pass" after attempting bomb
     */
    private String tryBombCard() {
        Country l_Target = getRandomUnconqueredCountry(d_Player);
        if (l_Target != null) {
            List<String> l_Commands = new ArrayList<>();
            l_Commands.add("bomb");
            l_Commands.add(l_Target.getD_CountryName());

            Order l_Order = new BombOrder();
            l_Order.setOrderInfo(OrderCreator.GenerateBombOrderInfo(
                    l_Commands.toArray(new String[0]), d_Player));

            IssueOrder.Commands = l_Order.getOrderInfo().getCommand();
            d_Logger.log(String.format("%s issuing new command: %s",
                    d_Player.getD_Name(), IssueOrder.Commands));
            d_Player.issueOrder();
            return "pass";
        }
        return "pass";
    }
    /**
     * Attempts to use a blockade card on a random conquered country.
     * @return "pass" after attempting blockade
     */
    private String tryBlockadeCard() {
        Country l_Target = getRandomConqueredCountry(d_Player);
        if (l_Target != null) {
            List<String> l_Commands = new ArrayList<>();
            l_Commands.add("blockade");
            l_Commands.add(l_Target.getD_CountryName());

            Order l_Order = new BlockadeOrder();
            l_Order.setOrderInfo(OrderCreator.GenerateBlockadeOrderInfo(
                    l_Commands.toArray(new String[0]), d_Player));

            IssueOrder.Commands = l_Order.getOrderInfo().getCommand();
            d_Logger.log(String.format("%s issuing new command: %s",
                    d_Player.getD_Name(), IssueOrder.Commands));
            d_Player.issueOrder();
            return "pass";
        }
        return "pass";
    }
    /**
     * Attempts to airlift armies between two random conquered countries.
     * @return "pass" after attempting airlift
     */
    private String tryAirliftCard() {
        Country l_From = getRandomConqueredCountry(d_Player);
        Country l_To = getRandomConqueredCountry(d_Player);
        if (l_From != null && l_To != null && !l_From.equals(l_To)) {
            List<String> l_Commands = new ArrayList<>();
            l_Commands.add("airlift");
            l_Commands.add(l_From.getD_CountryName());
            l_Commands.add(l_To.getD_CountryName());
            l_Commands.add(String.valueOf(d_Random.nextInt(l_From.getD_Armies()) + 1));

            Order l_Order = new AirliftOrder();
            l_Order.setOrderInfo(OrderCreator.GenerateAirliftOrderInfo(
                    l_Commands.toArray(new String[0]), d_Player));

            IssueOrder.Commands = l_Order.getOrderInfo().getCommand();
            d_Logger.log(String.format("%s issuing new command: %s",
                    d_Player.getD_Name(), IssueOrder.Commands));
            d_Player.issueOrder();
            return "pass";
        }
        return "pass";
    }
    /**
     * Attempts to use a diplomacy card on a random player.
     * @return "pass" after attempting negotiate
     */
    private String tryDiplomacyCard() {
        Player l_Target = getRandomPlayer();
        if (l_Target != null) {
            List<String> l_Commands = new ArrayList<>();
            l_Commands.add("negotiate");
            l_Commands.add(l_Target.getD_Name());

            Order l_Order = new NegotiateOrder();
            l_Order.setOrderInfo(OrderCreator.GenerateNegotiateOrderInfo(
                    l_Commands.toArray(new String[0]), d_Player));

            IssueOrder.Commands = l_Order.getOrderInfo().getCommand();
            d_Logger.log(String.format("%s issuing new command: %s",
                    d_Player.getD_Name(), IssueOrder.Commands));
            d_Player.issueOrder();
            return "pass";
        }
        return "pass";
    }
    /**
     * Gets a random country owned by the player.
     * @param p_Player the player whose countries to check
     * @return a random conquered country or null
     */
    private Country getRandomConqueredCountry(Player p_Player) {
        List<Country> l_Countries = p_Player.getCapturedCountries();
        if (l_Countries.isEmpty()) {
            return null;
        }
        return l_Countries.get(d_Random.nextInt(l_Countries.size()));
    }
    /**
     * Gets a random country not owned by the player.
     * @param p_Player the player whose enemies to check
     * @return a random unconquered country or null
     */
    private Country getRandomUnconqueredCountry(Player p_Player) {
        List<Country> l_AllCountries = new ArrayList<>(d_GameMap.getCountries().values());
        List<Country> l_Unconquered = l_AllCountries.stream()
                .filter(c -> !p_Player.equals(c.getPlayer()))
                .toList();

        if (l_Unconquered.isEmpty()) {
            return null;
        }
        return l_Unconquered.get(d_Random.nextInt(l_Unconquered.size()));
    }
    /**
     * Gets a random neighboring country of the given country.
     * @param p_Country the country to check neighbors of
     * @return a random neighbor country or null
     */
    private Country getRandomNeighbor(Country p_Country) {
        if (p_Country == null || p_Country.getD_CountryNeighbors().isEmpty()) {
            return null;
        }
        return new ArrayList<>(p_Country.getD_CountryNeighbors())
                .get(d_Random.nextInt(p_Country.getD_CountryNeighbors().size()));
    }
    /**
     * Gets a random player other than the current player.
     * @return a random player or null
     */
    private Player getRandomPlayer() {
        List<Player> l_Players = new ArrayList<>(d_GameMap.getPlayers().values());
        l_Players.remove(d_Player);

        if (l_Players.isEmpty()) {
            return null;
        }
        return l_Players.get(d_Random.nextInt(l_Players.size()));
    }
}