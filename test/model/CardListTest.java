/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Alan Tian
 */
public class CardListTest {
    CardList list;
    
    @Before
    public void setUp() {
        list = new CardList();

        list.add(new Card(Card.mapCardName("S_1")));
        list.add(new Card(Card.mapCardName("S_2")));
        list.add(new Card(Card.mapCardName("S_3")));
        list.add(new Card(Card.mapCardName("S_4")));
    }
    
    @After
    public void tearDown() {
        list = null;
    }

    @Test
    public void testCardListCut0() {
        assertEquals(4, list.size());
        CardList newList = list.cut(0);
        
        assertEquals(0, list.size());
        assertEquals(null, list.getTailNode());

        assertEquals(4, newList.size());
        assertEquals("S_4", newList.getTailCard().toString());
        assertEquals("S_1", newList.get(0).toString());
    }
    
    @Test
    public void testCardListCutMiddle() {
        assertEquals(4, list.size());
        CardList newList = list.cut(2);
        
        assertEquals(2, list.size());
        assertEquals("S_2", list.getTailNode().next().value().toString());
        assertEquals("S_2", list.getTailCard().toString());
        assertEquals("S_1", list.get(0).toString());

        assertEquals(2, newList.size());
        assertEquals("S_4", newList.getTailCard().toString());
        assertEquals("S_4", newList.getTailNode().next().value().toString());
        assertEquals("S_3", newList.get(0).toString());
    }

    @Test
    public void testCardListCutLast() {
        assertEquals(4, list.size());
        list.setOpenIndex(list.size()-1);
        CardList newList = list.cut(list.size()-1);
        
        assertEquals(3, list.size());
        assertEquals("S_3", list.getTailNode().next().value().toString());
        assertEquals("S_3", list.getTailCard().toString());
        assertEquals("S_1", list.get(0).toString());

        assertEquals(1, newList.size());
        assertEquals("S_4", newList.getTailCard().toString());
        assertEquals("S_4", newList.getTailNode().next().value().toString());
        assertEquals("S_4", newList.get(0).toString());
        assertEquals(2, list.getOpenIndex());
    }

    @Test
    public void testCardListMoveTail() {
        assertEquals(4, list.size());
        list.setOpenIndex(list.size()-1);
        assertEquals(3, list.getOpenIndex());

        Card card = list.moveTail();
        
        assertEquals(3, list.size());
        assertEquals("S_3", list.getTailNode().next().value().toString());
        assertEquals("S_3", list.getTailCard().toString());
        assertEquals("S_1", list.get(0).toString());
        assertEquals(2, list.getOpenIndex());
        
        assertEquals("S_4", card.toString());
        list.moveTail();
        list.moveTail();
        card = list.moveTail();
        assertEquals("S_1", card.toString());
        
        card = list.moveTail();
        assertEquals(null, card);
    }

    @Test
    public void testCardListLink_1to1() {
        CardList list1 = new CardList();
        CardList list2 = new CardList();

        list1.add(new Card(Card.mapCardName("D_3")));
        list2.add(new Card(Card.mapCardName("S_2")));
        
        list1.setOpenIndex(0);
        list2.link(list1);
        
        assertEquals("D_3", list1.get(0).toString());
        assertEquals("S_2", list1.getTailCard().toString());
        assertEquals("S_2", list1.getTailNode().next().value().toString());
        assertEquals(2, list1.size());
        assertEquals(0, list1.getOpenIndex());
    }

    @Test
    public void testCardListLink_1to3() {
        CardList list1 = new CardList();
        CardList list2 = new CardList();

        list1.add(new Card(Card.mapCardName("S_8")));
        list1.add(new Card(Card.mapCardName("S_4")));
        list1.add(new Card(Card.mapCardName("D_3")));
        list2.add(new Card(Card.mapCardName("S_2")));
        
        list1.setOpenIndex(1);
        list2.link(list1);
        
        assertEquals("S_8", list1.get(0).toString());
        assertEquals("S_2", list1.getTailCard().toString());
        assertEquals("S_2", list1.getTailNode().next().value().toString());
        assertEquals(4, list1.size());
        assertEquals(1, list1.getOpenIndex());
    }
}
