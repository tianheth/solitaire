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
    private boolean isVisible = false;
    private Dimension dimCard;
    private boolean fromDeck = false;
    
    private Solitaire game;
    private MainFrame mainFrame;

    public CardLabel(Card card, MainFrame frame, Solitaire game) {
        super("", JLabel.CENTER);
        this.card = card;
        this.dimCard = MainFrame.dimCard;
        setPreferredSize(dimCard);
        addMouseListener(this);
        pane = frame.getPnlDesk();
        this.game = game;
        this.mainFrame = frame;
        fromDeck = game.deck.getCurCard() == card;
    }

    public void setVisible(boolean isVisible) {
        if (isVisible) {
            setIcon(card.getImage(dimCard));
        } else {
            setIcon(MainFrame.getImageIcon(MainFrame.BACK_CARD, dimCard));
        }
        this.isVisible = isVisible;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            boolean moved = false;
            if(fromDeck)
                moved = game.deckTo();
            else
                moved = game.send(card.getIndex());
            if(moved)
                mainFrame.showGame();
        }
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

        if (!isVisible) {
            return;
        }

        //check drop locaton
        int iList = MainFrame.mapListArea(getLocation());
        if (!Solitaire.validListIndex(iList)) {
            setCardsLocation(origLocation, origLayer);
            return;
        }
//        CardLabel tail = findListTail(iList);
//        Point location = new Point(tail.getLocation().x, tail.getLocation().y + MainFrame.dimSpan.height);

        boolean linkSuccess = false;
        if (fromDeck) {
            linkSuccess = game.deckTo(iList);
        } else {
            linkSuccess = game.link(card.getIndex(), iList);
        }

        if (linkSuccess) {
            mainFrame.showGame();
//            setCardsLocation(location, pane.getLayer(tail));
//            tail.setNext(this);
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
        if (!isVisible) {
            return;
        }
        Point moudseLocation = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), pane);
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
            location.y += MainFrame.dimSpan.height;
            aux.setLocation(location);
            layer++;
            pane.setLayer(aux, layer);
            pane.setPosition(aux, 0);
            aux = aux.getNext();
        }
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
