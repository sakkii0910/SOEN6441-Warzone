package model;

/**
 * The type Card.
 */
public class Card {
    private CardType d_CardType;

    /**
     * Instantiates a new Card.
     */
    public Card() {
        d_CardType = CardType.getRandomCard();
    }

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

    /**
     * Compare two Cards if they have same type or not
     *
     * @param p_Obj
     * @return boolean
     */
    @Override
    public boolean equals(Object p_Obj) {
        if (p_Obj == null || this.getClass() != p_Obj.getClass())
            return false;

        Card l_Card = (Card) p_Obj;
        return d_CardType == l_Card.d_CardType;
    }
}
