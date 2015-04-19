/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author Alan Tian
 */
public class Card implements Comparable<Card> {

    public static final int SPADE = 4;
    public static final int HEART = 3;
    public static final int CLUB = 2;
    public static final int DIAMOND = 1;

    public static final int PREVIOUS = 1;
    public static final int ABOVE = 2;
    public static final int OTHER = -1;

    private static final int INVALID_SUIT = -1;
    public static final int INVALID_CARD_INDEX = -1;

    /**
     * [1..52] order by [A, 2, 3, ..., 10, J, Q, K] with Diamond, Club, Heart, Spade
     */
    private int cardIndex;

    public Card(int index) {
        if (index > 52 || index < 1) {
            throw new IllegalArgumentException("number should within 1 to 13");
        }
        this.cardIndex = index;
    }

    public int getValue() {
        return (cardIndex-1) % 13 +1;
    }

    /**
     * 
     * @return 1:Diamond, 2:Club, 3:Heart, 4:Spade
     */
    public int getSuit() {
        return (cardIndex-1) / 13 + 1;
    }

    public int getColor() {
        return getSuit() % 2;
    }

    public static int mapSuitChar(char suitChar){
        int suit = INVALID_SUIT;
        if (suitChar == 'S'||suitChar == 's')
            suit = SPADE;
        else if (suitChar == 'H'||suitChar == 'h')
            suit = HEART;
        else if (suitChar == 'C'||suitChar == 'c')
            suit = CLUB;
        else if (suitChar == 'D'||suitChar == 'd')
            suit = DIAMOND;
        return suit;
    }
    
    public static int mapCardName(String cardName){
        int index = -1;
        if (cardName.length()!=3)
            return INVALID_CARD_INDEX;
        int suit = mapSuitChar(cardName.charAt(0));
        if (suit == INVALID_SUIT)
            return INVALID_CARD_INDEX;
        if(cardName.charAt(1) == '_')
            index = Integer.valueOf(cardName.substring(2)) + (suit-1)*13;
        else
            index = Integer.valueOf(cardName.substring(1)) + (suit-1)*13;
        return index;
    }

    public char getSuitChar(){
        int suit = getSuit();
        char suitName;
        if(suit == SPADE)
            suitName = 'S'; 
        else if(suit == HEART)
            suitName = 'H'; 
        else if(suit == CLUB)
            suitName = 'C'; 
        else if(suit == DIAMOND)
            suitName = 'D'; 
        else
            suitName = 'N';
        return suitName;
    }
    /**
     * compare the order of cards
     *
     * @param target the card to compare with
     * @return PREVIOUS if they have same suit and target is 1 rank greater
 than this card ABOVE if the target can be put on top of this card (1
 rank greater and different color) OTHER if not the above result
     */
    @Override
    public int compareTo(Card target) {
        if (target.getValue() - getValue() == 1) {
            if (target.getColor() != getColor()) {
                return ABOVE;
            } else if (target.getSuit() == getSuit()) {
                return PREVIOUS;
            }
        }
        return OTHER;
    }

    @Override
    public String toString() {
//            return String.format("%d_%d_%d", cardIndex, getSuit(), getValue());
        if (getValue() < 10) {
            return String.format("%c_%d", getSuitChar(), getValue());
        } else {
            return String.format("%c%d", getSuitChar(), getValue());
        }
    }

    public int getIndex() {
        return cardIndex;
    }

    public Icon getImage(Dimension dim) {
        try {
            BufferedImage img = ImageIO.read(new File("images\\" + getImageFileName()));
            Image dimImg = img.getScaledInstance((int) dim.getWidth(), (int) dim.getHeight(), Image.SCALE_SMOOTH);
            return new ImageIcon(dimImg);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            return null;
        }
    }
    
    public String getImageFileName() {
        String fileName;
        fileName = String.format("%d%02d.gif", getSuit(), getValue());
        return fileName;
    }

}
