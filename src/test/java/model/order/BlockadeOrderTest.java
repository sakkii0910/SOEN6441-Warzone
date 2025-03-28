package model.order;

import model.Card;
import model.CardType;
import model.Country;
import model.GameMap;
import model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.logger.LogEntryBuffer;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link BlockadeOrder} functionality.
 * Validates blockade card usage, ownership checks, and order execution effects.
 * @author Sakshi Sudhir Mulik
 */
public class BlockadeOrderTest {
    private Player d_Player;
    private Player d_NeutralPlayer;
    private Country d_Country;
    private GameMap d_GameMap;

    /**
     * Initializes test environment with players, countries, and a fresh game map.
     * Ensures clean state for each test case.
     */
    @BeforeEach
    public void setUp() {
        d_GameMap = GameMap.getInstance();
        d_GameMap.resetGameMap();

        d_Player = new Player(null);
        d_Player.setD_Name("TestPlayer");

        d_NeutralPlayer = new Player(null);
        d_NeutralPlayer.setD_Name("NeutralPlayer");

        d_Country = new Country();
        d_Country.setD_CountryName("TestCountry");
        d_Country.setPlayer(d_Player);
        d_Country.setD_Armies(10);
        d_Player.getCapturedCountries().add(d_Country);
        d_GameMap.getCountries().put("TestCountry", d_Country);
    }

    /**
     * Tests blockade attempt without required card.
     * Verifies order fails and game state remains unchanged.
     */
    @Test
    public void testBlockadeWithoutCard() {
        OrderInfo l_OrderInfo = new OrderInfo();
        l_OrderInfo.setPlayer(d_Player);
        l_OrderInfo.setTargetCountry(d_Country);
        l_OrderInfo.setNeutralPlayer(d_NeutralPlayer);

        BlockadeOrder l_Order = new BlockadeOrder();
        l_Order.setOrderInfo(l_OrderInfo);

        assertFalse(l_Order.validateCommand());
        assertFalse(l_Order.execute());
        assertEquals(10, d_Country.getD_Armies());
        assertEquals(d_Player, d_Country.getPlayer());
    }

    /**
     * Tests blockade on country not owned by player.
     * Verifies validation fails and neutral country remains unaffected.
     */
    @Test
    public void testBlockadeOnUnownedCountry() {
        Country l_UnownedCountry = new Country();
        l_UnownedCountry.setD_CountryName("UnownedCountry");
        l_UnownedCountry.setD_Armies(5);
        d_GameMap.getCountries().put("UnownedCountry", l_UnownedCountry);

        d_Player.addCard(new Card(CardType.BLOCKADE));

        OrderInfo l_OrderInfo = new OrderInfo();
        l_OrderInfo.setPlayer(d_Player);
        l_OrderInfo.setTargetCountry(l_UnownedCountry);
        l_OrderInfo.setNeutralPlayer(d_NeutralPlayer);

        BlockadeOrder l_Order = new BlockadeOrder();
        l_Order.setOrderInfo(l_OrderInfo);

        assertFalse(l_Order.validateCommand());
        assertFalse(l_Order.execute());
        assertEquals(5, l_UnownedCountry.getD_Armies());
        assertNull(l_UnownedCountry.getPlayer());
    }

    /**
     * Tests blockade with null target country.
     * Verifies order validation fails gracefully.
     */
    @Test
    public void testBlockadeWithNullCountry() {
        d_Player.addCard(new Card(CardType.BLOCKADE));

        OrderInfo l_OrderInfo = new OrderInfo();
        l_OrderInfo.setPlayer(d_Player);
        l_OrderInfo.setTargetCountry(null);
        l_OrderInfo.setNeutralPlayer(d_NeutralPlayer);

        BlockadeOrder l_Order = new BlockadeOrder();
        l_Order.setOrderInfo(l_OrderInfo);

        assertFalse(l_Order.validateCommand());
        assertFalse(l_Order.execute());
        assertTrue(d_Player.cardAvailable(CardType.BLOCKADE));
    }

    /**
     * Tests blockade with null issuing player.
     * Verifies order validation rejects invalid player reference.
     */
    @Test
    public void testBlockadeWithNullPlayer() {
        d_Player.addCard(new Card(CardType.BLOCKADE));

        OrderInfo l_OrderInfo = new OrderInfo();
        l_OrderInfo.setPlayer(null);
        l_OrderInfo.setTargetCountry(d_Country);
        l_OrderInfo.setNeutralPlayer(d_NeutralPlayer);

        BlockadeOrder l_Order = new BlockadeOrder();
        l_Order.setOrderInfo(l_OrderInfo);

        assertFalse(l_Order.validateCommand());
        assertFalse(l_Order.execute());
        assertEquals(10, d_Country.getD_Armies());
    }

    /**
     * Tests successful blockade execution.
     * Verifies card is consumed upon successful order completion.
     */
    @Test
    public void testBlockadeRemovesCard() {
        d_Player.addCard(new Card(CardType.BLOCKADE));
        assertEquals(1, d_Player.getPlayerCards().size());

        OrderInfo l_OrderInfo = new OrderInfo();
        l_OrderInfo.setPlayer(d_Player);
        l_OrderInfo.setTargetCountry(d_Country);
        l_OrderInfo.setNeutralPlayer(d_NeutralPlayer);

        BlockadeOrder l_Order = new BlockadeOrder();
        l_Order.setOrderInfo(l_OrderInfo);

        assertTrue(l_Order.execute());
        assertEquals(0, d_Player.getPlayerCards().size());
    }

    /**
     * Tests order info getter methods.
     * Verifies order properties are correctly stored and retrieved.
     */
    @Test
    public void testBlockadeOrderInfo() {
        OrderInfo l_OrderInfo = new OrderInfo();
        l_OrderInfo.setPlayer(d_Player);
        l_OrderInfo.setTargetCountry(d_Country);
        l_OrderInfo.setNeutralPlayer(d_NeutralPlayer);

        BlockadeOrder l_Order = new BlockadeOrder();
        l_Order.setOrderInfo(l_OrderInfo);

        assertEquals(d_Player, l_Order.getOrderInfo().getPlayer());
        assertEquals(d_Country, l_Order.getOrderInfo().getTargetCountry());
        assertEquals(d_NeutralPlayer, l_Order.getOrderInfo().getNeutralPlayer());
    }
}