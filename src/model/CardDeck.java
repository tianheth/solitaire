/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import model.list.CircularList;
import model.list.CircularNode;

/**
 *
 * @author Alan Tian 1302662
 */
public class CardDeck {

    private Card curCard;
    private CircularList<Card> cards;

    /**
     * Open the next card, set it as the current card if current card is the
     * last card, return the first card
     *
     * @return the updated current card
     */
    public CardDeck() {
        cards = new CircularList<>();
    }

    public Card drawCard() {
        int i = cards.indexOf(curCard);
        if (i == 0) {
            i = cards.size() - 1;
        } else {
            i--;
        }
        curCard = cards.get(i);
        return curCard;
    }

    /**
     * Delete and return the current card (so we can place it in a list or a
     * stack)
     *
     * @return the deleted card
     */
    public Card takeCard() {
        Card takenCard = curCard;
//        curCard = drawCard();
        int i = cards.indexOf(curCard);
        int size = cards.size();
        cards.remove(takenCard);
        if (cards.isEmpty()) {
            curCard = null;
        } else {
            if (i == size - 1) {
                i--;
            }
            curCard = cards.get(i);
        }
        return takenCard;
    }

    public void setCurCard(int cardIndex) {
        curCard = cards.get(cardIndex);
    }

    public Card getCurCard() {
        return curCard;
    }

    public Card getPrevCard() {
        if (curCard == null || (cards.size() < 2)) {
            return null;
        } else {
            int i = cards.indexOf(curCard);
            if (i == cards.size() - 1) {
                i = 0;
            } else {
                i = i + 1;
            }
            return cards.get(i);
        }
    }

    public boolean isFirst() {
        return cards.indexOf(curCard) == 0;
    }

    public void add(Card card) {
        cards.add(card);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public int size() {
        return cards.size();
    }

    public Object get(int i) {
        return cards.get(i);
    }

    public boolean isLast() {
        return cards.indexOf(curCard) == cards.size()-1;
    }
}
