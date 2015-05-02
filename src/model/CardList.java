package model;

import model.list.CircularList;
import model.list.CircularNode;

/**
 *
 * @author Alan Tian 1302662
 */
public class CardList {

    private CircularList<Card> cards;
    private Card tailCard;
    private int openIndex;

    public CardList() {
        cards = new CircularList<Card>();
        tailCard = null;
        openIndex = -1;
    }

    /**
     * create a card list
     *
     * @param cards the card list inside
     * @param openIndex the open index of the card list
     */
    private CardList(CircularList<Card> cards, int openIndex) {
        this.cards = cards;
        this.tailCard = cards.get(cards.size() - 1);
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
    public CardList cut(Card card) {
        CardList cutList;
        int index = cards.indexOf(card);

        if (index == 0) {
            CircularList<Card> cards2 = new CircularList<>();
            cards2.setTail(cards.getTail());
            cards2.setSize(cards.size());
            cutList = new CardList(cards2, openIndex);
            // initiate the list 1 since it's empty now
            cards = new CircularList<Card>();
            tailCard = null;
            openIndex = -1;
        } else {
            // set the tailCard and openIndex
            CircularNode<Card> remainListTail = cards.getNode(index - 1);
            CircularNode<Card> remainListHead = cards.getHead();

            CircularNode<Card> cutListHead = cards.getNode(index);
            CircularNode<Card> cutListTail = cards.getTail();

            CircularList<Card> cards2 = new CircularList<>();
            cards2.setTail(cutListTail);
            cards2.setSize(cards.size() - index);
            cards2.getTail().setNext(cutListHead);
            cutList = new CardList(cards2, 0);

            remainListTail.setNext(remainListHead);
            cards.setTail(remainListTail);
            cards.setSize(index);
            tailCard = remainListTail.getValue();

            if (openIndex == index) {
                openIndex--;
            }
        }
        return cutList;
    }

    /**
     * Join this list to the tail of the other list, if the rules allow this;
     * have to check the rules before calling this method; the first card of
     * this list is 'ABOVE' the tail card of the other listtargetList
     *
     * @param targetList the list that linked to
     */
    public void link(CardList targetList) {
        CircularNode headNode = targetList.getHeadNode();

        CircularList<Card> targetCards = new CircularList<>();
        targetCards.setTail(cards.getTail());
        targetCards.setSize(cards.size() + targetList.size());

        if (targetList.isEmpty()) {
            targetList.setOpenIndex(0);
        } else {
            targetList.getTailNode().setNext(cards.getHead());
            targetCards.getTail().setNext(headNode);
        }
        targetList.setTailCard(tailCard);
        targetList.setCards(targetCards);
    }

    /**
     * Add c as the new tail card, check the rules before calling this method
     *
     * @param newCard
     */
    public void add(Card newCard) {
        cards.add(newCard);
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
    public Card takeTail() {
        Card takenCard = tailCard;
        if (openIndex == cards.size() - 1) {
            openIndex--;
        }
        cards.removeTail();
        if (cards.size() > 0) {
            tailCard = cards.getTail().getValue();
        } else {
            tailCard = null;
            cards=new CircularList<Card>();
        }
        return takenCard;
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
        CircularNode<Card> finger = cards.getNode(nodeIndex);
        while (nodeIndex < cards.size()) {
            Card card = finger.getValue();
            if (card.getIndex() == cardIndex) {
                return card;
            } else {
                finger = finger.getNext();
                nodeIndex++;
            }
        }
        return null;

    }

    public void setOpenIndex(int i) {
        openIndex = i;
    }

    public void setTailCard(Card tailCard) {
        this.tailCard = tailCard;
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public void setCards(CircularList<Card> cards) {
        this.cards = cards;
    }

    public int size() {
        if (cards == null) {
            return 0;
        } else {
            return cards.size();
        }
    }

    public Card get(int i) {
        return cards.get(i);
    }

    public CircularNode<Card> getTailNode() {
        return cards.getTail();
    }

    public CircularNode<Card> getHeadNode() {
        return cards.getHead();
    }

}
