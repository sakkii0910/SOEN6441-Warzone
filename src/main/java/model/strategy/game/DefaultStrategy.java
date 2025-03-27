package model.strategy.game;

import model.*;
import utils.logger.LogEntryBuffer;

/**
 * Class holding the default strategy of the advance order.
 *
 * @author Shariq Anwar
 */
public class DefaultStrategy implements GameStrategy {

    /**
     * Game settings object
     */
    GameSettings SETTINGS = GameSettings.getInstance();

    /**
     * Logger Observable
     */
    private LogEntryBuffer d_Logger = LogEntryBuffer.getInstance();

    /**
     * Method holding the default attack logic
     *
     * @param p_Player The player who initiated attack
     * @param p_From   The country from which the attack is initiated
     * @param p_To     The country on which the attack is going to happen
     * @param p_Armies The number of armies to be moved
     * @return true on successful execution else false
     */
    @Override
    public boolean attack(Player p_Player, Country p_From, Country p_To, int p_Armies) {
        try {
            if(p_Armies <= 0) {
                d_Logger.log("You cannot move/attack with 0 armies");
                return false;
            }
            p_From.depleteArmies(p_Armies);
            if(p_To.getD_Armies() == 0 ) {
                p_To.setD_Armies(p_Armies);
                makeNewKing(p_Player, p_To);
                d_Logger.log(String.format("Attacker : %s (%s) won", p_Player.getD_Name(), p_From.getD_CountryName()));

                //Assign power card to king
                // TODO: later change to random card
                Card l_AssignedCard = new Card(CardType.BOMB);
                p_Player.addCard(l_AssignedCard);
                d_Logger.log("Attacker: " + p_Player.getD_Name() + " received a card: " + l_AssignedCard.getCardType().toString());
                d_Logger.log(String.format("Since won, left out %s (Attacker) armies %s moved to %s.", p_From.getD_CountryName(), p_Armies, p_To.getD_CountryName()));
                return true;
            }
            int l_attackerKills = (int) Math.round(p_Armies * SETTINGS.ATTACKER_PROBABILITY);
            int l_defenderKills = (int) Math.round(p_To.getD_Armies() * SETTINGS.DEFENDER_PROBABILITY);

            int l_armiesLeftAttacker = p_Armies > l_defenderKills ? (p_Armies - l_defenderKills) : 0;
            int l_armiesLeftDefender = p_To.getD_Armies() > l_attackerKills ? p_To.getD_Armies() - l_attackerKills : 0;

            if (l_armiesLeftAttacker > 0 && l_armiesLeftDefender <= 0) {
                p_To.setD_Armies(l_armiesLeftAttacker);
                makeNewKing(p_Player, p_To);
                d_Logger.log(String.format("Attacker : %s (%s) won", p_Player.getD_Name(), p_From.getD_CountryName()));

                //Assign power card to king
                // TODO: later change to random card
                Card l_AssignedCard = new Card(CardType.BOMB);;
                p_Player.addCard(l_AssignedCard);
                d_Logger.log("Attacker: " + p_Player.getD_Name() + " received a card: " + l_AssignedCard.getCardType().toString());
                d_Logger.log(String.format("Since won, left out %s (Attacker) armies %s moved to %s.", p_From.getD_CountryName(), l_armiesLeftAttacker, p_To.getD_CountryName()));
            } else {

                p_From.deployArmies(l_armiesLeftAttacker);
                p_To.setD_Armies(l_armiesLeftDefender);
                d_Logger.log(String.format("Attacker : %s (%s) lost", p_Player.getD_Name(), p_From.getD_CountryName()));
                d_Logger.log(String.format("Remaining armies of %s (Attacker) in attack: %s ", p_From.getD_CountryName(), l_armiesLeftAttacker));
                d_Logger.log(String.format("Remaining armies of %s (Defender) in attack: %s ", p_To.getD_CountryName(), l_armiesLeftDefender));
            }

            return true;
        } catch (Exception p_Exception) {
            return false;
        }
    }
}