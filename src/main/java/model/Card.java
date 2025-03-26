package model;

/**
 * The type Card.
 */
public class Card {
    private CardType d_CardType;

    /**
     * Instantiates a new Card.
     *
     * @param p_CardType the p card type
     */
    public Card(CardType p_CardType) {
        d_CardType = p_CardType;
    }

    /**
     * Gets card type.
     *
     * @return the card type
     */
    public CardType getCardType() {
        return d_CardType;
    }

    /**
     * Sets card type.
     *
     * @param p_CardType the p card type
     */
    public void setCardType(CardType p_CardType) {
        d_CardType = p_CardType;
    }
}
