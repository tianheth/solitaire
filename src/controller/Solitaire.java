/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.Scanner;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import model.Card;
import model.CardDeck;
import model.CardList;
import model.CardStack;
import model.Suit;
import model.list.CircularList;
import view.GameCui;
import view.MainFrame;

/**
 *
 * @author cvg2836
 */
public class Solitaire implements ActionListener {
    public static final int LIST_NUM = 7;
    public static final int STACK_NUM = 4;

    public CardDeck deck;
    public CardStack[] stacks;
    public CardList[] lists;

    public Solitaire() {
        stacks = new CardStack[STACK_NUM];
        lists = new CardList[LIST_NUM];
        initSolitaire();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Solitaire game = new Solitaire();
        showGui(game);
        game.startGame();
    }

    public static void showGui(Solitaire game) {
        MainFrame mainFrame = new MainFrame(game);
        mainFrame.setVisible(true);
    }

    public void startGame() {
        // start the game CUI
//        GameCui cui = new GameCui(this);
//        cui.startGame();
    }

    public void initSolitaire() {
        Card[] cardSet = new Card[52];
        for (int i = 0; i < cardSet.length; i++) {
            cardSet[i] = new Card(i + 1);
        }
        
        shuffle(cardSet);
//        cui.showCardSet(cardSet);

        deck = new CardDeck();
        for (int i = 0; i < lists.length; i++) {
            lists[i] = new CardList();
        }
        
        for (int i = 0; i < stacks.length; i++) {
            stacks[i] = new CardStack();
        }

        int c = 0;

        for (int i = 0; i < LIST_NUM; i++) {
            for (int j = 0; j < i + 1; j++) {
                lists[i].add(cardSet[c]);
                c++;
            }
            lists[i].setOpenIndex(lists[i].size() - 1);
        }

        for (int i = c; i < cardSet.length; i++) {
            deck.add(cardSet[i]);
        }
        deck.setCurCard(deck.size()-1);
    }

    /* Rearranges an array of objects in uniformly random order
     * (under the assumption that <tt>Math.random()</tt> generates independent
     * and uniformly distributed numbers between 0 and 1).
     * @param a the array to be shuffled
     * @see StdRandom
     */
    public static void shuffle(Object[] a) {
        int N = a.length;
        for (int i = 0; i < N; i++) {
            // choose index uniformly in [i, N-1]
            int r = i + (int) (Math.random() * (N - i));
            Object swap = a[r];
            a[r] = a[i];
            a[i] = swap;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
