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
public class CardList extends CircularList<Card> {

    private Card tailCard;
    private int openIndex;

    public CardList() {
        super();
        tailCard = null;
        openIndex = -1;
    }

    /**
     * create a card list with at least 1 card
     *
     * @param tailNode
     * @param nodeCount
     * @param tailCard
     * @param openIndex
     */
    private CardList(CircularNode<Card> tailNode, int nodeCount, int openIndex) {
        super(tailNode, nodeCount);
        this.tailCard = tailNode.next().value();
        this.openIndex = openIndex;
    }

    public Card getTailCard() {
        return tailCard;
    }

    public int getOpenIndex() {
        return openIndex;
    }

    /**
     * Separate the list into two: [0..(i-1)] and [i..count]; Open the card at
     * (i-1) if necessary
     *
     * @param index >=0 and &lt nodeCount
     * @return the second list
     */
    public CardList cut(int index) {
        CardList list2;

        if (index == 0) {
            list2 = new CardList(tail, size(), openIndex);
            // initiate the list 1 since it's empty now
            tail = null;
            nodeCount = 0;
            tailCard = null;
            openIndex = -1;
        } else {
            CircularNode<Card> tailNode_L2 = new CircularNode<Card>(null, null, null);

            CircularNode<Card> tailCard_L2 = tail.next();
            CircularNode<Card> headCard_L1 = tailCard_L2.next();

            CircularNode<Card> headCard_L2 = getNode(index);
            CircularNode<Card> tailCard_L1 = headCard_L2.prev();

            // reset links for list 1
            headCard_L1.setPrev(tailCard_L1);
            tailCard_L1.setNext(headCard_L1);
            tail.setNext(tailCard_L1);

            // reset links for list 2
            headCard_L2.setPrev(tailCard_L2);
            tailCard_L2.setNext(headCard_L2);
            tailNode_L2.setNext(tailCard_L2);

            // set the tailCard and openIndex
            list2 = new CardList(tailNode_L2, size() - index, 0);
            tailCard = tailCard_L1.value();
            tail.setNext(tailCard_L1);
            nodeCount = index;

            if (openIndex == index) {
                openIndex--;
            }
        }
        return list2;
    }

    /**
     * Join this list to the tail of the other list, if the rules allow this;
     * have to check the rules before calling this method; the first card of
     * this list is 'ABOVE' the tail card of the other list
     *
     * @param list1 the list that linked to, this list would be list2
     */
    public void link(CardList list1) {
        if (!list1.isEmpty()) {
            CircularNode<Card> tailCard_L1 = list1.getTailNode().next();
            CircularNode<Card> headCard_L1 = tailCard_L1.next();
            CircularNode<Card> tailCard_L2 = tail.next();
            CircularNode<Card> headCard_L2 = tailCard_L2.next();

            // reset links for the list 1
            tailCard_L1.setNext(headCard_L2);
            headCard_L1.setPrev(tailCard_L2);

            // reset links for the list 2
            tailCard_L2.setNext(headCard_L1);
            headCard_L2.setPrev(tailCard_L1);
        }
        else
            list1.setOpenIndex(0);

        list1.setTailCard((Card) tail.next().value());
        list1.setTailNode(tail);
        list1.setSize(list1.size() + size());
    }

    /**
     * Add c as the new tail card, check the rules before calling this method
     *
     * @param newCard
     */
    @Override
    public void add(Card newCard) {
        add(size(), newCard);
        tailCard = newCard;
        if (openIndex < 0) {
            openIndex = 0;
        }
    }

    /**
     * Delete and return the tail card Set the card beneath it as the new tail
     * card Open the new tail card if necessary.
     *
     * @return the old tail card
     */
    public Card moveTail() {
        if (size() > 1) {
            tailCard = get(size() - 2);
        } else {
            tailCard = null;
        }
        if (openIndex == size() - 1) {
            openIndex--;
        }
        return remove(size() - 1);
    }

    /**
     * find card from list's open cards
     *
     * @param cardIndex the index of a card ([1..52])
     * @return the card in the list, otherwise null
     */
    public Card findCard(int cardIndex) {
        if (isEmpty()) {
            return null;
        }

        int nodeIndex = openIndex;
        CircularNode<Card> finger = getNode(nodeIndex);
        while (nodeIndex < nodeCount) {
            Card card = finger.value();
            if (card.getIndex() == cardIndex) {
                return card;
            } else {
                finger = finger.next();
                nodeIndex++;
            }
        }
        return null;

    }

    public void setOpenIndex(int i) {
        openIndex = i;
    }

    private void setTailCard(Card tailCard) {
        this.tailCard = tailCard;
    }
}
