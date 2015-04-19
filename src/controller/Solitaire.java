/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import model.Card;
import model.CardDeck;
import model.CardList;
import model.CardStack;
import view.MainFrame;

/**
 *
 * @author cvg2836
 */
public class Solitaire extends java.util.Observable {

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
        deck.setCurCard(deck.size() - 1);
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

    /**
     * take the current card from deck to the suitable stack
     * @return true for success, false for failed (wrong move)
     */
    public boolean deckTo() {
        Card card = deck.getCurCard();
        int suit = card.getSuit();
        CardStack stack = stacks[suit - 1];

        if (stack.isAddable(card)) {
            stack.add(card);
            deck.takeCard();
            notify();
            return true;
        } else {
            return false;
        }
    }
    
    public boolean deckTo(int listIndex){
        Card card = deck.getCurCard();
        CardList list = lists[listIndex];
        if (card.compareTo(list.getTailCard()) == Card.ABOVE) {
            list.add(deck.takeCard());
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * link the card into the end of the list
     * @param cardIndex card to be cut from the old list
     * @param listIndex target list index
     * @return true for success, false for failed
     */
    public boolean link(int cardIndex, int listIndex){
        CardList list;
        for (int l = 0; l < Solitaire.LIST_NUM; l++) {
            list = lists[l];
            // looking among the open cards
            Card card = list.findCard(cardIndex);

            if (card != null) {
                CardList targetList = lists[listIndex];
                Card tailCard = targetList.getTailCard();
                // check if linkable 
                if (card.compareTo(tailCard) == Card.ABOVE) {
                    CardList newList = list.cut(list.indexOf(card));
                    newList.link(targetList);
                    return true;
                } else {
                    return false;
                }
            }
        }
        // didn't find card
        return false;
    }
    
    public boolean send(int cardIndex){
        // locate list
        for (int listIndex = 0; listIndex < LIST_NUM; listIndex++) {
            if (lists[listIndex].isEmpty()) {
                continue;
            }
            Card card = lists[listIndex].getTailCard();
            if (card.getIndex() == cardIndex) {
                CardStack stack = stacks[card.getSuit() - 1];
                if(stack.isAddable(card)){
                    stack.add(card);
                    lists[listIndex].moveTail();
                    notify();
                    return true;
                }
            }
        }
        
        return true;
    }
}
