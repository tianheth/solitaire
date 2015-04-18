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
 * @author Alan Tian
 */
public class CardDeck extends CircularList<Card> {
//    private Card curCard;

    private CircularNode<Card> curCardNode;

    /**
     * Open the next card, if this is the tail card, return the first card
     *
     * @return
     */
    public Card drawCard() {
//        int i = indexOf(curCard);
//        if (i == 0) {
//            curCard = get(size() - 1);
//        } else {
//            curCard = get(i - 1);
//        }
        Card curCard = curCardNode.value();
        curCardNode = curCardNode.prev();
        return curCard;
    }

    /**
     * Delete and return the current card (so we can place it in a list or a
     * stack)
     *
     * @return the deleted card
     */
    public Card takeCard() {
        Card card = curCardNode.value();

        CircularNode<Card> node = curCardNode;
        if (curCardNode == tail.next()) {
            curCardNode = curCardNode.prev();
        } else {
            curCardNode = curCardNode.next();
        }
        remove(node);
//        if (card != null) {
//            if (index <= size() - 1) {
//                curCard = getNode(index).value();
//            } else if (index > 0) {
//                curCard = getNode(index - 1).value();
//            } else {
//                curCard = null;
//            }
//        }
        return card;
    }

    public void setCurCard(int cardIndex) {
        curCardNode = getNode(cardIndex);
    }

    public Card getCurCard() {
        return curCardNode.value();
    }

    public boolean isFirst() {
        return curCardNode == tail.next().next();
    }
}
