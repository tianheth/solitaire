/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.Solitaire;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;
import model.Card;

/**
 *
 * @author Alan Tian
 */
public class CardLabel extends JLabel implements MouseListener, MouseMotionListener {

    private Card card;
    private CardLabel next = null;
    private JLayeredPane pane;
    private Point firstClick = new Point();
    private int origLayer = 0;
    private Point origLocation;
    private Solitaire game;
    private boolean isVisible = false;

    public CardLabel(Card card, JLayeredPane pane, Solitaire game) {
        super("", JLabel.CENTER);
        this.card = card;
        setPreferredSize(MainFrame.DIM_CARD);
//        if(card)
        addMouseListener(this);
        this.pane = pane;
        this.game = game;
    }
    
    public void setVisible(boolean isVisible){
        if(isVisible)
            setIcon(card.getImage(MainFrame.DIM_CARD));
        else
            setIcon(MainFrame.getImageIcon(MainFrame.BACK_CARD, MainFrame.DIM_CARD));
//            setIcon(card.getImage(MainFrame.DIM_CARD));
        this.isVisible = isVisible;
    }

    public CardLabel() {
        super("", JLabel.CENTER);
        this.card = null;
//        setPreferredSize(dim);
//        setIcon(card.getImage(dim));
//        addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getClickCount() == 2)
            game.send(card.getIndex());
//        System.out.println(card.toString());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        addMouseMotionListener(this);
        firstClick = e.getPoint();
        origLocation = getLocation();
        origLayer = pane.getLayer(this);
//        System.out.println("x:" + origLocation.x + ", y:" + origLocation.y);
//        System.out.println("draged:"+card.toString()+"->position:"+pane.getPosition(this)+", layer:"+pane.getLayer(this));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        removeMouseMotionListener(this);
        //check drop locaton
        int iList = mapList(getLocation());
        CardLabel tail = findListTail(iList);
        
//        if (link(iList)) {
//            tail.setNext(this);
//        }
        
        if (link(iList)) {
            Point location = new Point(tail.getLocation().x, tail.getLocation().y + MainFrame.CARD_SPAN);
            setCardsLocation(location, pane.getLayer(tail));
            tail.setNext(this);
        } else {
            setCardsLocation(origLocation, origLayer);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
//        System.out.println("entered:"+card.toString());
    }

    @Override
    public void mouseExited(MouseEvent e) {
//        System.out.println("exit:"+card.toString());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(!isVisible)
            return;
        Point moudseLocation = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), this.pane);
        Point location = new Point(moudseLocation.x - firstClick.x, moudseLocation.y - firstClick.y);
//        System.out.println("draged:"+card.toString()+"->x:"+p.x+", y:"+p.y);
        int layer = pane.highestLayer() + 1;
        setCardsLocation(location, layer);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    public void setNext(CardLabel next) {
        this.next = next;
    }

    public CardLabel getNext() {
        return next;
    }

    private void setCardsLocation(Point location, int layer) {
        setLocation(location);
        pane.setLayer(this, layer);
        pane.setPosition(this, 0);

        CardLabel aux = next;
        while (aux != null) {
            location.y += MainFrame.CARD_SPAN;
            aux.setLocation(location);
            layer++;
            pane.setLayer(aux, layer);
            pane.setPosition(aux, 0);
            aux = aux.getNext();
        }
    }

    private int mapList(Point location) {
        int iList = location.x / MainFrame.DIM_CARD.width % 7;
//        System.out.println(iList);
        return iList;
    }

    private boolean link(int iList) {
        return game.link(card.getIndex(), iList);
    }

    private CardLabel findListTail(int iList) {
        Component[] c = pane.getComponents();
        Card tail = game.lists[iList].getTailCard();
        for (int i = 0; i < c.length; i++) {
            CardLabel cardLabel = (CardLabel) c[i];
            if (cardLabel.card == tail) {
                return cardLabel;
            }
        }
        return null;
    }

}
