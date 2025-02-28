import model.abstractClasses.GamePhase;
import model.gamePhases.MapEditorPhase;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Game {
    private GameEngine d_GameEngine;

    private GamePhase d_GamePhase;

    private final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        new Game().start();
    }

    public void start() {
        System.out.println();
        System.out.println("=========================================");
        System.out.println("            WARZONE: RISK GAME   ️         ");
        System.out.println("=========================================");
        System.out.println("               MAIN MENU                 ");
        System.out.println("=========================================");
        System.out.println("  [1] Start New Game");
        System.out.println("  [5] Exit");
        System.out.println("=========================================");
        System.out.print("\tSelect an option: ");

        try {
            int option = SCANNER.nextInt();
            d_GameEngine = new GameEngine();
            switch (option) {
                case 1:
                    d_GamePhase = new MapEditorPhase();
                    break;
                case 5:
                    System.out.println("Exiting game. Goodbye!");
                    System.exit(0);
                    break;
                default:
                    throw new InputMismatchException();
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Invalid option. Please select 1 or 5.");
            start();
        }

        d_GameEngine.setGamePhase(d_GamePhase);
        d_GameEngine.start();

    }

}

