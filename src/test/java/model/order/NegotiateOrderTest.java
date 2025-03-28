package model.order;

import model.Card;
import model.CardType;
import model.GameMap;
import model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.logger.LogEntryBuffer;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link NegotiateOrder} functionality.
 * Verifies diplomacy card usage, player negotiation validation, and execution.
 *
 * @author Sakshi Sudhir Mulik
 */
public class NegotiateOrderTest {
    private Player d_Player1;
    private Player d_Player2;
    private GameMap d_GameMap;
    private LogEntryBuffer d_Logger;

    /**
     * Initializes test environment before each test case.
     * Creates two players and resets the game map state.
     */
    @BeforeEach
    public void setUp() {
        d_GameMap = GameMap.getInstance();
        d_GameMap.resetGameMap();
        d_Logger = LogEntryBuffer.getInstance();

        d_Player1 = new Player(null);
        d_Player1.setD_Name("Player1");

        d_Player2 = new Player(null);
        d_Player2.setD_Name("Player2");

        d_GameMap.getPlayers().put("Player1", d_Player1);
        d_GameMap.getPlayers().put("Player2", d_Player2);
    }

    /**
     * Tests successful negotiation between two players.
     * Verifies card consumption and mutual neutral status.
     * @throws Exception if order execution fails
     */
    @Test
    public void testValidNegotiateOrder() {
        d_Player1.addCard(new Card(CardType.DIPLOMACY));

        OrderInfo l_OrderInfo = new OrderInfo();
        l_OrderInfo.setPlayer(d_Player1);
        l_OrderInfo.setNeutralPlayer(d_Player2);
        l_OrderInfo.setCommand("negotiate Player2");

        NegotiateOrder l_NegotiateOrder = new NegotiateOrder();
        l_NegotiateOrder.setOrderInfo(l_OrderInfo);

        assertTrue(l_NegotiateOrder.validateCommand());
        assertTrue(l_NegotiateOrder.execute());

        assertTrue(d_Player1.getNeutralPlayers().contains(d_Player2));
        assertTrue(d_Player2.getNeutralPlayers().contains(d_Player1));
        assertFalse(d_Player1.cardAvailable(CardType.DIPLOMACY));
    }

    /**
     * Tests negotiation attempt without diplomacy card.
     * Verifies validation fails and no neutral status is created.
     */
    @Test
    public void testNegotiateWithoutCard() {
        OrderInfo l_OrderInfo = new OrderInfo();
        l_OrderInfo.setPlayer(d_Player1);
        l_OrderInfo.setNeutralPlayer(d_Player2);
        l_OrderInfo.setCommand("negotiate Player2");

        NegotiateOrder l_NegotiateOrder = new NegotiateOrder();
        l_NegotiateOrder.setOrderInfo(l_OrderInfo);

        assertFalse(l_NegotiateOrder.validateCommand());
        assertFalse(l_NegotiateOrder.execute());

        assertFalse(d_Player1.getNeutralPlayers().contains(d_Player2));
        assertFalse(d_Player2.getNeutralPlayers().contains(d_Player1));
    }

    /**
     * Tests negotiation with non-existent player.
     * Verifies validation fails for unknown player.
     */
    @Test
    public void testNegotiateWithNonexistentPlayer() {
        d_Player1.addCard(new Card(CardType.DIPLOMACY));

        Player l_NonExistentPlayer = new Player(null);
        l_NonExistentPlayer.setD_Name("NonExistent");

        OrderInfo l_OrderInfo = new OrderInfo();
        l_OrderInfo.setPlayer(d_Player1);
        l_OrderInfo.setNeutralPlayer(l_NonExistentPlayer);
        l_OrderInfo.setCommand("negotiate NonExistent");

        NegotiateOrder l_NegotiateOrder = new NegotiateOrder();
        l_NegotiateOrder.setOrderInfo(l_OrderInfo);

        assertFalse(l_NegotiateOrder.validateCommand());
        assertFalse(l_NegotiateOrder.execute());
    }

    /**
     * Tests self-negotiation edge case.
     * Verifies validation passes but results in neutral self-status.
     */
    @Test
    public void testSelfNegotiation() {
        d_Player1.addCard(new Card(CardType.DIPLOMACY));

        OrderInfo l_OrderInfo = new OrderInfo();
        l_OrderInfo.setPlayer(d_Player1);
        l_OrderInfo.setNeutralPlayer(d_Player1);
        l_OrderInfo.setCommand("negotiate Player1");

        NegotiateOrder l_NegotiateOrder = new NegotiateOrder();
        l_NegotiateOrder.setOrderInfo(l_OrderInfo);

        assertTrue(l_NegotiateOrder.validateCommand());
        assertTrue(l_NegotiateOrder.execute());

        assertTrue(d_Player1.getNeutralPlayers().contains(d_Player1));
        assertFalse(d_Player1.cardAvailable(CardType.DIPLOMACY));
    }

    /**
     * Tests multiple negotiations with same player.
     * Verifies card consumption for each negotiation attempt.
     */
    @Test
    public void testMultipleNegotiationsSamePlayer() {
        d_Player1.addCard(new Card(CardType.DIPLOMACY));
        d_Player1.addCard(new Card(CardType.DIPLOMACY));

        OrderInfo l_OrderInfo1 = new OrderInfo();
        l_OrderInfo1.setPlayer(d_Player1);
        l_OrderInfo1.setNeutralPlayer(d_Player2);
        l_OrderInfo1.setCommand("negotiate Player2");

        NegotiateOrder l_NegotiateOrder1 = new NegotiateOrder();
        l_NegotiateOrder1.setOrderInfo(l_OrderInfo1);

        assertTrue(l_NegotiateOrder1.validateCommand());
        assertTrue(l_NegotiateOrder1.execute());

        OrderInfo l_OrderInfo2 = new OrderInfo();
        l_OrderInfo2.setPlayer(d_Player1);
        l_OrderInfo2.setNeutralPlayer(d_Player2);
        l_OrderInfo2.setCommand("negotiate Player2");

        NegotiateOrder l_NegotiateOrder2 = new NegotiateOrder();
        l_NegotiateOrder2.setOrderInfo(l_OrderInfo2);

        assertTrue(l_NegotiateOrder2.validateCommand());
        assertTrue(l_NegotiateOrder2.execute());

        assertEquals(0, d_Player1.getPlayerCards().size());
    }

    /**
     * Tests order command printing functionality.
     * Verifies no exceptions during print execution.
     */
    @Test
    public void testNegotiateOrderPrint() {
        d_Player1.addCard(new Card(CardType.DIPLOMACY));

        OrderInfo l_OrderInfo = new OrderInfo();
        l_OrderInfo.setPlayer(d_Player1);
        l_OrderInfo.setNeutralPlayer(d_Player2);
        l_OrderInfo.setCommand("negotiate Player2");

        NegotiateOrder l_NegotiateOrder = new NegotiateOrder();
        l_NegotiateOrder.setOrderInfo(l_OrderInfo);

        l_NegotiateOrder.printOrderCommand();
    }

    /**
     * Tests negotiation with null player.
     * Verifies validation fails for null target player.
     */
    @Test
    public void testNegotiateWithNullPlayer() {
        d_Player1.addCard(new Card(CardType.DIPLOMACY));

        OrderInfo l_OrderInfo = new OrderInfo();
        l_OrderInfo.setPlayer(d_Player1);
        l_OrderInfo.setNeutralPlayer(null);
        l_OrderInfo.setCommand("negotiate null");

        NegotiateOrder l_NegotiateOrder = new NegotiateOrder();
        l_NegotiateOrder.setOrderInfo(l_OrderInfo);

        assertFalse(l_NegotiateOrder.validateCommand());
        assertFalse(l_NegotiateOrder.execute());
    }
}