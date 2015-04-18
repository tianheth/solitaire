/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.Solitaire;
import static controller.Solitaire.LIST_NUM;
import java.io.PrintStream;
import java.util.Scanner;
import model.Card;
import model.CardDeck;
import model.CardList;
import model.CardStack;

/**
 *
 * @author Alan Tian
 */
public class GameCui {

    public static final String ERR_INVALID_LIST = "invalid list.";
    public static final String ERR_INVALID_MOVE = "invalid move.";
    public static final String ERR_INVALID_CMD = "invalid command.";
    public static final String ERR_INVALID_CARD = "invalid card.";

    Solitaire game;
    Scanner input = new Scanner(System.in);
    PrintStream output = new PrintStream(System.out);

    public void setOutput(PrintStream output) {
        this.output = output;
    }

    public GameCui(Solitaire game) {
        this.game = game;
        input.useDelimiter("\n");
    }

    public void startGame() {
        output.println("  ---  Welcome to Solitaire Game  ---");
        String cmd = "";
        while (cmd.compareTo("quit") != 0) {
            showGame();
            output.print("Your next move: ");
            cmd = input.next().toLowerCase();
//            output.println(cmd);
            if (executeCommand(cmd)) {
                output.println("success");
            } else {
                output.println("failed");
            }
        }
    }

    public boolean executeCommand(String command) {
        boolean result = false;
        String[] cmd = command.split(" ");
        if (cmd[0].compareTo("drawcard") == 0) {

            result = drawCard();
        } else if (cmd[0].compareTo("deckto") == 0) {
            deckTo(cmd);
        } else if (cmd[0].compareTo("link") == 0) {
            link(cmd);
        } else if (cmd[0].compareTo("send") == 0) {
            send(cmd);
        } else if (cmd[0].compareTo("restart") == 0) {
            game.initSolitaire();
        } else if (cmd[0].compareTo("quit") != 0) {
            output.println(ERR_INVALID_CMD);
        }

        return true;
    }

    public void showGame() {
        showCardListAll();
        showCardDeckAll();

        showDeck();
        showStack();
        showList();
    }

    private void showDeck() {
        CardDeck deck = game.deck;
        if (deck.isEmpty()) {
            output.println("Card Deck: Empty");
        } else {
            output.println("Card Deck: Not Empty  Open Card: " + deck.getCurCard().toString());
        }
    }

    private void showStack() {
        CardStack stack;
        String stackCards = "";
        for (int i = 0; i < 4; i++) {
            stack = game.stacks[i];
            if (stack.peek() != null) {
                stackCards += stack.peek().toString() + " ";
            } else {
                stackCards += "none ";
            }
        }
        output.println("Card Stack: " + stackCards);
    }

    private void showList() {
        output.println("Card Lists: ");
        CardList list;
        for (int i = 0; i < Solitaire.LIST_NUM; i++) {
            String listCards = "";
            list = game.lists[i];
            for (int j = 0; j < list.size(); j++) {
                if (j >= list.getOpenIndex()) {
                    listCards += list.get(j).toString() + " ";
                } else {
                    listCards += "Back ";
                }
            }
            output.println((i + 1) + ": " + listCards);
        }
    }

    public void showCardListAll() {
        output.println("Card Lists: ");
        CardList list;
        for (int i = 0; i < Solitaire.LIST_NUM; i++) {
            String listCards = "";
            list = game.lists[i];
            for (int j = 0; j < list.size(); j++) {
                listCards += list.get(j).toString() + " ";
            }
            output.println((i + 1) + ": " + listCards);
        }
        output.println();
    }

    public void showCardSet(Card[] cardSet) {
        output.println("\ncard set");
        for (int i = 0; i < 4; i++) {
            String cards13 = "";
            for (int j = 0; j < 13; j++) {
                cards13 += cardSet[i * 13 + j] + " ";
            }
            output.println(cards13);
        }
        output.println();
    }

    public void showCardDeckAll() {
        String deckCards = "";
        CardDeck deck = game.deck;
        for (int i = 0; i < deck.size(); i++) {
            deckCards += deck.get(i).toString() + " ";
        }
        output.println("deck: " + deckCards);
        output.println();
    }

    private boolean drawCard() {
        if (game.deck.isEmpty()) {
            return false;
        }
        game.deck.drawCard();
        return true;
    }

    /**
     * deckto l: take the open card from deck to a list l. deckto: if no
     * argument, then take the card to stack.
     *
     * @param cmd
     */
    private boolean deckTo(String[] cmd) {
        if (cmd.length == 1) {
            Card card = game.deck.getCurCard();
            for (int s = 0; s < game.stacks.length; s++) {
                CardStack stack = game.stacks[s];
                if(stack.isEmpty())
                    continue;
                Card stackCard = stack.peek();
                
                if (stackCard.compareTo(card) == Card.PREVIOUS) {
                    game.stacks[s].add(card);
                    game.deck.takeCard();
                    return true;
                }
            }
            // none stack is available for this card
            return false;
        }

        if (cmd.length != 2) {
            output.println(ERR_INVALID_CMD);
            return false;
        }

        int listIndex = Integer.valueOf(cmd[1]);
        if (!validListIndex(listIndex)) {
            return false;
        }
        if (game.deck.getCurCard().compareTo(game.lists[listIndex-1].getTailCard()) == Card.ABOVE) {
            game.lists[listIndex-1].add(game.deck.takeCard());
            return true;
        } else {
            output.println(ERR_INVALID_MOVE);
            return false;
        }
    }

    /**
     * link c x: Suppose c is a revealed card in a card list, and 1 ≤x≤ 7 This
     * command moves all cards below and including c in the same list to the xth
     * list For example the command 'Link Spade9 6' moves all card below and
     * including Spade9 to the 6th card list
     *
     * @param cmd
     */
    private boolean link(String[] cmd) {
        if (cmd.length != 3) {
            output.println(ERR_INVALID_CMD);
            return false;
        }

        int listIndex = Integer.valueOf(cmd[2]);
        if (!validListIndex(listIndex)) {
            return false;
        }

        int cardIndex = Card.mapCardName(cmd[1]);
        if (cardIndex == Card.INVALID_CARD_INDEX) {
            output.println(ERR_INVALID_CARD);
            return false;
        }

        CardList list;
        for (int l = 0; l < Solitaire.LIST_NUM; l++) {
            list = game.lists[l];
            // looking for card
            Card card = list.findCard(cardIndex);

            if (card != null) {
                CardList targetList = game.lists[listIndex - 1];
                Card tailCard = targetList.getTailCard();
                // check if linkable 
                if (card.compareTo(tailCard) == Card.ABOVE) {
                    CardList newList = list.cut(list.indexOf(card));
                    newList.link(targetList);
                    return true;
                } else {
                    output.println(ERR_INVALID_MOVE);
                    return false;
                }
            }
        }
        output.println(ERR_INVALID_CARD);
        return false;
    }

    /**
     * send c: Suppose c is a tail card of a card list, or open card of deck,
     * this command moves the card c to the stack that corresponds to its suit
     *
     * @param cmd
     */
    private boolean send(String[] cmd) {
        if (cmd.length != 2) {
            output.println(ERR_INVALID_CMD);
            return false;
        }

        int cardIndex = Card.mapCardName(cmd[1]);
        if (cardIndex == Card.INVALID_CARD_INDEX) {
            output.println(ERR_INVALID_CARD);
            return false;
        }

        // locate list
        for (int listIndex = 0; listIndex < LIST_NUM; listIndex++) {
            if (game.lists[listIndex].isEmpty()) {
                continue;
            }
            Card card = game.lists[listIndex].getTailCard();
            if (card.getIndex() == cardIndex) {
                // find stack
                CardStack stack = game.stacks[card.getSuit() - 1];
                if (stack.isEmpty()) {
                    if (card.getValue() != 1) {
                        output.println(ERR_INVALID_MOVE);
                        return false;
                    }
                } else if (stack.peek().compareTo(card) != Card.PREVIOUS) {
                    output.println(ERR_INVALID_MOVE);
                    return false;
                }
                stack.add(card);
                game.lists[listIndex].moveTail();
                return true;
            }
        }
        output.println(ERR_INVALID_CARD);
        return false;
    }

    private boolean validListIndex(int index) {
        boolean valid = (1 <= index) && (index <= Solitaire.LIST_NUM);
        if (!valid) {
            output.println(ERR_INVALID_LIST);
        }
        return valid;
    }

    public int locateListByTail(int tailCardIndex) {
        for (int l = 0; l < LIST_NUM; l++) {
            if (game.lists[l].getTailCard().getIndex() == tailCardIndex) {
                return l;
            }
        }
        output.println(ERR_INVALID_CARD);
        return -1;
    }

}
