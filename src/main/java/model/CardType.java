package model;

import java.util.Random;

/**
 * The enum Card type.
 *  @author Taha Mirza
 *  @author Pruthvirajsinh Dodiya
 */
public enum CardType {
    /**
     * Bomb card type.
     */
    BOMB,
     /**
     * Blockade card type.
     */
    BLOCKADE,
    /**
    /**
     * Airlift card type.
     */
    AIRLIFT,
    /**
     * Diplomacy card type.
     */
    DIPLOMACY;

    public static CardType getRandomCard() {
        Random d_Random = new Random();
        return values()[d_Random.nextInt(values().length)];
    }
}
