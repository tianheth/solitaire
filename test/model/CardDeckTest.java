/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import org.junit.After;
import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Alan Tian
 */
public class CardDeckTest {
    CardList list;
    CardDeck deck;
    
    @Before
    public void setUp() {
        list = new CardList();
        deck = new CardDeck();

        deck.add(new Card(Card.mapCardName("D_1")));
        deck.add(new Card(Card.mapCardName("D_2")));
        deck.add(new Card(Card.mapCardName("D_3")));
        deck.add(new Card(Card.mapCardName("D_4")));
//        deck.setCurCard(deck.get(deck.size()-1));
        deck.setCurCard(deck.size()-1);
        
        list.add(new Card(Card.mapCardName("S_1")));
        list.add(new Card(Card.mapCardName("S_2")));
        list.add(new Card(Card.mapCardName("S_3")));
        list.add(new Card(Card.mapCardName("S_4")));
    }
    
    @After
    public void tearDown() {
        list = null;
        deck = null;
    }

    @Test
    public void testCardDeckDrawCard() {
        assertEquals(4, deck.size());
        assertEquals("D_4", deck.getCurCard().toString());

        Card card = deck.drawCard();
        assertEquals("D_3", card.toString());

        deck.drawCard();
        deck.drawCard();
        card= deck.drawCard();
        assertEquals("D_4", card.toString());
        card= deck.drawCard();
        assertEquals("D_3", card.toString());
    }

    @Test
    public void testCardDeck() {
        assertEquals(4, deck.size());
        Card card = deck.takeCard();
        assertEquals("D_4", card.toString());
        assertEquals("D_3", deck.getCurCard().toString());
        deck.drawCard();
        card = deck.takeCard();
        assertEquals("D_2", card.toString());
        card= deck.drawCard();
        assertEquals("D_1", card.toString());
        card = deck.takeCard();
        assertEquals("D_1", card.toString());
        assertEquals("D_3", deck.getCurCard().toString());
    }
    @Test
    public void testCardDeckTakeCardToEmpty() {
        assertEquals(4, deck.size());
        deck.takeCard();
        deck.takeCard();
        deck.takeCard();
        Card card = deck.takeCard();
        assertEquals("D_1", card.toString());
        assertTrue(deck.isEmpty());
        assertEquals(null, deck.getCurCard());
    }
}
