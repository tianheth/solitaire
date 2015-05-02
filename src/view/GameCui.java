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
 * @author Alan Tian 1302662
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
        showOrigGame();
        String cmd = "";
        while (cmd.compareTo("quit") != 0) {
            showGame();
            output.println("\n[DrawCard], [DeckTo <list num>], [Link <card> <list num>], [Send <Card>], [Restart], [Quit]");
            output.print("Your next move: ");
            cmd = input.next().toLowerCase();
            int result = executeCommand(cmd);
            if (result == 1) {
                output.println("success");
                if (game.isGameWin()) {
                    output.println("game win!");
                    game.initSolitaire();
                    showOrigGame();
                }
            } else if (result == -1) {
                output.println("failed");
            }
        }
    }

    public int executeCommand(String command) {
        int result = 0;
        String[] cmd = command.split(" ");
        if (cmd[0].compareTo("drawcard") == 0) {
            if (drawCard()) {
                result = 1;
            } else {
                result = -1;
            }
        } else if (cmd[0].compareTo("deckto") == 0) {
            if (deckTo(cmd)) {
                result = 1;
            } else {
                result = -1;
            }
        } else if (cmd[0].compareTo("link") == 0) {
            if (link(cmd)) {
                result = 1;
            } else {
                result = -1;
            }
        } else if (cmd[0].compareTo("send") == 0) {
            if (send(cmd)) {
                result = 1;
            } else {
                result = -1;
            }
        } else if (cmd[0].compareTo("restart") == 0) {
            game.initSolitaire();
            showOrigGame();
        } else if (cmd[0].compareTo("quit") != 0) {
            output.println(ERR_INVALID_CMD);
        } else {
            output.println("unknown command");
            result = -1;
        }
        return result;
    }

    public void showOrigGame(){
        output.println("\n\n  ---  Welcome to Solitaire Game  ---");
        output.println("  ---  Original Game Infor ---");
        showList(true);
        showCardDeckAll();
    }
    
    public void showGame() {
        output.println("  ---  Current Game Infor ---");
        showDeck();
        showStack();
        showList(false);
    }

    public void showCardDeckAll() {
        String deckCards = "";
        CardDeck deck = game.deck;
        for (int i = 0; i < deck.size(); i++) {
            deckCards += deck.get(i).toString() + " ";
        }
        output.println("deck: \n" + deckCards);
        output.println();
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

    /**
     * show card lists
     *
     * @param showClose show the closed cards as open or not
     */
    private void showList(boolean showClose) {
        output.println("Card Lists: ");
        CardList list;
        for (int i = 0; i < Solitaire.LIST_NUM; i++) {
            String listCards = "";
            list = game.lists[i];
            for (int j = 0; j < list.size(); j++) {
                if (!showClose) {
                    if (j >= list.getOpenIndex()) {
                        listCards += list.get(j).toString() + " ";
                    } else {
                        listCards += "Back ";
                    }
                } else {
                    listCards += list.get(j).toString() + " ";
                }
            }
            output.println((i + 1) + ": " + listCards);
        }
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
            return game.deckTo();
        } else if (cmd.length != 2) {
            output.println(ERR_INVALID_CMD);
            return false;
        } else {
            int listIndex = Integer.valueOf(cmd[1]) - 1;
            if (!validListIndex(listIndex)) {
                return false;
            } else if (!game.deckTo(listIndex)) {
                output.println(ERR_INVALID_MOVE);
                return false;
            } else {
                return true;
            }
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

        int listIndex = Integer.valueOf(cmd[2]) - 1;
        if (!validListIndex(listIndex)) {
            return false;
        }

        int cardIndex = Card.mapCardName(cmd[1]);
        if (cardIndex == Card.INVALID_CARD_INDEX) {
            output.println(ERR_INVALID_CARD);
            return false;
        }

        if (!game.link(cardIndex, listIndex)) {
            output.println(ERR_INVALID_CARD);
            return false;
        } else {
            return true;
        }
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
                game.lists[listIndex].takeTail();
                return true;
            }
        }
        output.println(ERR_INVALID_CARD);
        return false;
    }

    private boolean validListIndex(int index) {
        boolean valid = Solitaire.validListIndex(index);
        if (!valid) {
            output.println(ERR_INVALID_LIST);
        }
        return valid;
    }

}
