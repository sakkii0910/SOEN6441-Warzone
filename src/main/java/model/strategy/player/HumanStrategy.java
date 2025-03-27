package model.strategy.player;

import java.io.Serializable;
import java.util.Scanner;

/**
 * Human Strategy class - takes input from user.
 *
 * @author Shariq Anwar
 * @version 1.0.0
 */
public class HumanStrategy extends PlayerStrategy implements Serializable {
    /**
     * Scanner to read commands from user
     */
    private static  final Scanner SCANNER = new Scanner(System.in);

    /**
     * Default constructor
     */
    public HumanStrategy() {}


    /**
     * Function to read and return the next input of user
     *
     * @return next input of the user
     */
    @Override
    public String createCommand() {
        return SCANNER.nextLine();
    }
}
