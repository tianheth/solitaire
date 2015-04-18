/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import controller.Solitaire;
import static controller.Solitaire.LIST_NUM;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import view.GameCui;

/**
 *
 * @author Alan Tian
 */
public class GameTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    
    Solitaire game;
    Card[] cardSet;
    public GameTest() {
        game = new Solitaire();

        cardSet = new Card[52];
        for (int i = 0; i < cardSet.length; i++) {
            cardSet[i] = new Card(i + 1);
        }
    }
    
    @BeforeClass
    public static void setUpClass() {
        
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        System.out.println("setup");
        game.deck = new CardDeck();
        for (int i = 0; i < game.lists.length; i++) {
            game.lists[i] = new CardList();
        }
        
        for (int i = 0; i < game.stacks.length; i++) {
            game.stacks[i] = new CardStack();
        }

        int c = 0;

        for (int i = 0; i < LIST_NUM; i++) {
            for (int j = 0; j < i + 1; j++) {
                game.lists[i].add(cardSet[c]);
                c++;
            }
            game.lists[i].setOpenIndex(game.lists[i].size() - 1);
        }

        for (int i = c; i < cardSet.length; i++) {
            game.deck.add(cardSet[i]);
        }
//        game.deck.setCurCard(cardSet[cardSet.length-1]);
        game.deck.setCurCard(game.deck.size()-1);
    }
    
    @After
    public void tearDown() {
        game.deck = null;
        for (int i = 0; i < game.lists.length; i++) {
            game.lists[i] = null;
        }
        
        for (int i = 0; i < game.stacks.length; i++) {
            game.stacks[i] = null;
        }
    }

    @Test
    public void testCardListCut() {
        CardList list = new CardList();
        list.add(new Card(Card.mapCardName("S_1")));
        list.add(new Card(Card.mapCardName("S_2")));
        list.add(new Card(Card.mapCardName("S_3")));
        list.add(new Card(Card.mapCardName("S_4")));
        list.cut(0);
    }
    
    @Test
    public void testCui() {
        System.out.println("test CUI");

        GameCui cui = new GameCui(game);
        cui.showGame();
        
//        cui.setOutput(new PrintStream(outContent));
        cui.executeCommand("deckto 0");

        for (int i=1; i<=4; i++)
            cui.executeCommand("drawcard");
        
        cui.executeCommand("deckto 1");
        cui.executeCommand("deckto 4");
        
        cui.showGame();

//        cui.executeCommand("send D_4");
//        cui.executeCommand("send D_1");
//        cui.executeCommand("send D_3");
//        cui.executeCommand("send S_9");
//        cui.showGame();
//        
//        cui.executeCommand("link S_9 3");
//        cui.executeCommand("link C_2 2");
//        cui.showGame();
        
        game.lists[1].set(game.lists[1].size()-1, new Card(Card.mapCardName("S_1")));
        cui.executeCommand("link S_1 7");
//        game.lists[1].set(game.lists[1].size()-1, new Card(40));
//        cui.executeCommand("link S_1 7");
        cui.showGame();
//        assertEquals(GameCui.ERR_INVALID_LIST+"\n", outContent);
    }    
}
