package controller;

import model.*;
import model.abstractClasses.GameController;
import model.gamePhases.ExecuteOrderPhase;
import model.order.Order;
import utils.GameEngine;
import utils.logger.LogEntryBuffer;

import java.util.*;

/**
 * The type Issue order.
 * @author Taha Mirza
 * @author Poorav Panchal
 * @author  Shariq Anwar
 */
public class IssueOrder extends GameController {

    private final HashMap<String, Player> d_Players;

    /**
     * Static variable to hold commands
     */
    public static String Commands = null;

    /**
     * Logger instance
     */
    private LogEntryBuffer d_Logger = LogEntryBuffer.getInstance();

    /**
     * Instantiates a new Issue order.
     *
     * @param p_GameEngine the p game engine
     */
    public IssueOrder(GameEngine p_GameEngine) {
        super(p_GameEngine);
        d_GameMap = GameMap.getInstance();
        d_Players = d_GameMap.getPlayers();
        d_NextPhase = new ExecuteOrderPhase(this.d_GameEngine);
    }

    public void startPhase() throws Exception {
        d_Logger.log("\n\n--------------- ISSUE ORDER PHASE ---------------");

        if (d_GameMap.getD_CurrentPlayer() == null) {
            d_GameMap.setD_CurrentPlayer(d_Players.entrySet().iterator().next().getValue());
        }

        while (!(numberOfSkippedPlayers() == d_Players.size())) {
            for (Player l_Player : d_Players.values()) {
                d_Logger.log("\n\nCurrent Player Turn: " + l_Player.getD_Name());
                d_Logger.log("-----------------------------------------------------------------------------------------");

                if (l_Player.isD_TurnCompleted()) {
                    d_Logger.log("Player " + l_Player.getD_Name() + " has completed his turns.");
                    continue;
                }

                d_GameMap.setD_CurrentPlayer(l_Player);

                boolean l_IssueCommand = false;
                while (!l_IssueCommand) {
                    showStatus(l_Player);
                    Commands = l_Player.readFromPlayer();
                    if (Objects.isNull(Commands)) {
                        Commands = "";
                    }
                    if (!Commands.isEmpty()) {
                        l_IssueCommand = validateCommand(Commands, l_Player);
                    }
                    if (Commands.equals("pass")) {
                        break;
                    }
                }
                if (!Commands.equals("pass")) {
                    d_Logger.log(l_Player.getD_Name() + " has issued this order :- " + Commands);
                    l_Player.issueOrder();
                    d_Logger.log("The order has been added to the list of orders.");
                    d_Logger.log("=============================================================================");
                }
            }
        }

        for(Player l_Player : d_Players.values()) {
            l_Player.setD_TurnCompleted(false);
        }

        this.d_GameEngine.setGamePhase(this.d_NextPhase);
        this.d_GameMap.setGamePhase(this.d_NextPhase);
    }

    /**
     * Number of skipped players int.
     *
     * @return the int
     */
    int numberOfSkippedPlayers() {
        int count = 0;
        for (Player l_Player : d_Players.values()) {
            if (l_Player.isD_TurnCompleted()) {
                count++;
            }
        }
        return count;
    }

    /**
     * A function to validate the commands
     *
     * @param p_CommandArr The string entered by the user
     * @param p_Player     the player object
     * @return true if the command is correct else false
     */
    public boolean validateCommand(String p_CommandArr, Player p_Player) {
        List<String> l_Commands = Arrays.asList("deploy", "advance", "bomb", "blockade", "airlift", "negotiate");
        String[] l_CommandArr = p_CommandArr.split(" ");
        if (p_CommandArr.toLowerCase().contains("pass")) {
            p_Player.setD_TurnCompleted(true);
            return false;
        }
        if (!l_Commands.contains(l_CommandArr[0].toLowerCase())) {
            d_Logger.log("The command syntax is invalid." + p_CommandArr);
            return false;
        }
        if (!checkCommandLength(l_CommandArr[0], l_CommandArr.length)) {
            d_Logger.log("The command syntax is invalid." + p_CommandArr);
            return false;
        }
        switch (l_CommandArr[0].toLowerCase()) {
            case "deploy":
                try {
                    Integer.parseInt(l_CommandArr[2]);
                } catch (NumberFormatException l_Exception) {
                    d_Logger.log("The number format is invalid");
                    return false;
                }
                if(Integer.parseInt(l_CommandArr[2]) < 0){
                    d_Logger.log("The number format is invalid");
                    return false;
                }
                break;
            case "advance":
                try {
                    Integer.parseInt(l_CommandArr[3]);
                } catch (NumberFormatException l_Exception) {
                    d_Logger.log("The number format is invalid");
                    return false;
                }
                break;
            default:
                break;

        }
        return true;
    }

    /**
     * A function to check the length of each command
     *
     * @param p_Command the command to be validated
     * @return true if length is correct else false
     */
    static boolean checkCommandLength(String p_Command, int p_Length) {
        if (p_Command.contains("deploy")) {
            return p_Length == 3;
        } else if (p_Command.contains("bomb") || p_Command.contains("blockade") || p_Command.contains("negotiate") || p_Command.contains("savegame")) {
            return (p_Length == 2);
        } else if (p_Command.contains("airlift") || p_Command.contains("advance")) {
            return (p_Length == 4);
        }
        return false;
    }

    /**
     * A function to show the player the status while issuing the order
     *
     * @param p_Player The current player object
     */
    public void showStatus(Player p_Player) {
        d_Logger.log("List of game loop commands");
        d_Logger.log("To deploy the armies : deploy countryID numarmies");
        d_Logger.log("To advance/attack the armies : advance countrynamefrom countynameto numarmies");
        d_Logger.log("To airlift the armies : airlift sourcecountryID targetcountryID numarmies");
        d_Logger.log("To blockade the armies : blockade countryID");
        d_Logger.log("To negotiate with player : negotiate playerID");
        d_Logger.log("To bomb the country : bomb countryID");
        d_Logger.log("To skip: pass");
        d_Logger.log("-----------------------------------------------------------------------------------------");
        String l_Table = "|%-15s|%-19s|%-22s|%n";
        System.out.format("+--------------+-----------------------+------------------+%n");
        System.out.format("| Current Player   | Initial Assigned  | Left Armies      | %n");
        System.out.format("+---------------+------------------  +---------------------+%n");
        System.out.format(l_Table, p_Player.getD_Name(), p_Player.getReinforcementArmies(), p_Player.getD_ArmiesToIssue());
        System.out.format("+--------------+-----------------------+------------------+%n");

        d_Logger.log("The countries assigned to the player are: ");
        System.out.format("+--------------+-----------------------+------------------+---------+%n");

        System.out.format(
                "|Country name  |Country Armies  | Neighbors                         |%n");
        System.out.format(
                "+--------------+-----------------------+------------------+---------+%n");
        for (Country l_Country : p_Player.getCapturedCountries()) {
            String l_TableCountry = "|%-15s|%-15s|%-35s|%n";
            StringBuilder l_NeighborList = new StringBuilder();
            for (Country l_Neighbor : l_Country.getD_CountryNeighbors()) {
                l_NeighborList.append(l_Neighbor.getD_CountryName()).append("-");
            }
            System.out.format(l_TableCountry, l_Country.getD_CountryName(), l_Country.getD_Armies(), l_Country.createANeighborList(l_Country.getD_CountryNeighbors()));
        }
        System.out.format("+--------------+-----------------------+------------------+---------+\n");

        if (!p_Player.getPlayerCards().isEmpty()) {
            d_Logger.log("The Cards assigned to the Player are: ");
            for (Card l_Card : p_Player.getPlayerCards()) {
                d_Logger.log(l_Card.getCardType().toString());
            }
        }
        if (!p_Player.getOrders().isEmpty()) {
            d_Logger.log("The Orders issued by Player " + p_Player.getD_Name() + " are:");
            for (Order l_Order : p_Player.getOrders()) {
                d_Logger.log(l_Order.getOrderInfo().getCommand());
            }
        }
    }
}
