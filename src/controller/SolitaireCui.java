package controller;

import view.GameCui;

/**
 *
 * @author Alan Tian 
 */
public class SolitaireCui {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Solitaire game = new Solitaire();
        game.initSolitaire();
        startGame(game);
    }

    public static void startGame(Solitaire game) {
        // start the game CUI
        GameCui cui = new GameCui(game);
        cui.startGame();
    }

}
