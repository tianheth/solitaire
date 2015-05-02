package controller;

import model.Card;
import model.CardDeck;
import model.CardList;
import model.CardStack;
import view.MainFrame;

/**
 *
 * @author Alan Tian 1302662
 *
 * contribution: finished alone by Alan Tian
 * extensions:
 * 1 there is an animation when game win
 *
 * 2 the GUI is simular to a common windows solitaire game:
 * 2.1 drag and drop operations on card from deck to list or from list to list
 * 2.2 double click on current card will execute deck to stack
 * 2.3 double click on current card will execute list to stack
 * 2.4 cards have front and back images
 * 
 * 3 data structures:
 * 3.1 singly linked list for stack
 * 3.2 circularly linked list for deck
 * 3.3 circularly linked list for card list
 *
 * 4 CUI version is defined in a separate class SolitaireCui, with project configuration 'CUI'
 * 
 */
public class Solitaire {

    public static final int LIST_NUM = 7;
    public static final int STACK_NUM = 4;

    public CardDeck deck;
    public CardStack[] stacks;
    public CardList[] lists;

    public Solitaire() {
        stacks = new CardStack[STACK_NUM];
        lists = new CardList[LIST_NUM];
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Solitaire game = new Solitaire();
        game.initSolitaire();
        showGui(game);
    }

    public static void showGui(Solitaire game) {
        MainFrame mainFrame = new MainFrame(game);
        mainFrame.setVisible(true);
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
     *
     * @return true for success, false for failed (wrong move)
     */
    public boolean deckTo() {
        Card card = deck.getCurCard();
        int suit = card.getSuit();
        CardStack stack = stacks[suit - 1];

        if (stack.isAddable(card)) {
            stack.add(card);
            deck.takeCard();
            return true;
        } else {
            return false;
        }
    }

    /**
     * take the current card from deck to the target list
     *
     * @param listIndex target card list
     * @return true if success, otherwise false
     */
    public boolean deckTo(int listIndex) {
        Card card = deck.getCurCard();
        CardList list = lists[listIndex];
        Card tail = list.getTailCard();
        if (tail == null || card.compareTo(tail) == Card.ABOVE) {
            list.add(deck.takeCard());
            return true;
        } else {
            return false;
        }
    }

    /**
     * link the card into the end of the list
     *
     * @param cardIndex card to be cut from the old list
     * @param listIndex target list index
     * @return true for success, false for failed
     */
    public boolean link(int cardIndex, int listIndex) {
        CardList list;
        for (int l = 0; l < Solitaire.LIST_NUM; l++) {
            list = lists[l];
            // looking among the open cards
            Card card = list.findCard(cardIndex);

            if (card != null) {
                CardList targetList = lists[listIndex];
                Card tailCard = targetList.getTailCard();
                // check if linkable 
                if (tailCard == null || card.compareTo(tailCard) == Card.ABOVE) {
                    CardList newList = list.cut(card);
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

    /**
     * send a card from list to stack. card is located by it's card index
     * through all lists.
     *
     * @param cardIndex card index of the card to be send
     * @return true if send success, otherwise false
     */
    public boolean send(int cardIndex) {
        // locate list
        for (int listIndex = 0; listIndex < LIST_NUM; listIndex++) {
            if (lists[listIndex].isEmpty()) {
                continue;
            }
            Card card = lists[listIndex].getTailCard();

            // find card
            if (card.getIndex() == cardIndex) {
                CardStack stack = stacks[card.getSuit() - 1];
                if (stack.isAddable(card)) {
                    stack.add(card);
                    lists[listIndex].takeTail();
                    return true;
                } else {
                    return false;
                }
            }
        }

        return false;
    }

    /**
     * check if the list index is between 0 to 6
     *
     * @param index list index to be checked
     * @return true for valid index, otherwise false
     */
    public static boolean validListIndex(int index) {
        return (index >= 0) && (index < LIST_NUM);
    }

    /**
     * check if the game is win. if all cards in list had been opened, then game
     * is automatically win
     *
     * @return true if the game is win, otherwise false
     */
    public boolean isGameWin() {
        for (int l = 0; l < LIST_NUM; l++) {
            if (lists[l].getOpenIndex() > 0) {
                return false;
            }
        }
        return true;
    }

    public boolean isStackFull() {
        for (int s = 0; s < stacks.length; s++) {
            if (stacks[s].size() != Card.SUIT_SIZE) {
                return false;
            }
        }
        return true;
    }

    public boolean isDeckEmpty() {
        return deck.isEmpty();
    }

    public Card findSendableCard() {
        Card nextCard = null;
        for (int l = 0; l < LIST_NUM; l++) {
            nextCard = lists[l].getTailCard();
            CardStack stack = stacks[nextCard.getSuit() - 1];
            if (stack.isAddable(nextCard)) {
                return nextCard;
            }
        }
        return nextCard;
    }

}
