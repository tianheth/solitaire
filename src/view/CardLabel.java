/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JLabel;
import model.Card;

/**
 *
 * @author Alan Tian
 */
public class CardLabel extends JLabel implements MouseListener, MouseMotionListener{
    private Card card;
    public CardLabel(Card card, Dimension dim){
        super("", JLabel.CENTER);
        this.card = card;
        setPreferredSize(dim);
        setIcon(card.getImage(dim));
        addMouseListener(this);
    }

        public CardLabel(){
        super("", JLabel.CENTER);
        this.card = null;
//        setPreferredSize(dim);
//        setIcon(card.getImage(dim));
//        addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println(card.toString());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        addMouseMotionListener(this);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        removeMouseMotionListener(this);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        System.out.println("entered:"+card.toString());
    }

    @Override
    public void mouseExited(MouseEvent e) {
        System.out.println("exit:"+card.toString());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        System.out.println("draged:"+card.toString());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}
