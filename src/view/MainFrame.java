package view;

import controller.Solitaire;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import model.*;

/**
 *
 * @author Alan Tian 1302662
 */
public class MainFrame extends JFrame implements ActionListener, MouseListener {

    // game panel
    private JMenuItem miRestart = new JMenuItem("Restart");
    // desk panel
    private JLayeredPane pnlDesk = new JLayeredPane();
    private JLabel lblDeck = new JLabel();
    private CardLabel lblDeckCur;
    private CardLabel lblDeckPrev;
    private JLabel[] lblStack = new JLabel[4];

    // 
    public static Dimension dimCard;
    public static Dimension dimSpan;
    public static Dimension dimFrame;

    private Solitaire game;
    public static final String EMPTY_CARD = "images\\empty.gif";
    public static final String BACK_CARD = "images\\back.png";

    private void initDeskPanel() {
        pnlDesk.setLayout(null);
        showGame();
    }

    public MainFrame(Solitaire game) {
        this.game = game;

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Solitaire Game");

        Dimension dimScreen = Toolkit.getDefaultToolkit().getScreenSize();
        dimFrame = new Dimension(dimScreen.width * 2 / 3, dimScreen.height * 2 / 3);
        this.setSize(dimFrame);

        this.setLocation((dimScreen.width - dimFrame.width) / 2,
                (dimScreen.height - dimFrame.height) / 2);

        dimCard = new Dimension(dimFrame.width * 5 / (6 * 7 + 3), dimFrame.height * 5 / (1 + 5 + 2 + 5 + 7));
        dimSpan = new Dimension(dimCard.width / 5, dimCard.height / 5);

        initDeskPanel();
        this.add(pnlDesk);

        JMenu menuGame = new JMenu("Restart Game");
        menuGame.add(miRestart);
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menuGame);
        setJMenuBar(menuBar);

        miRestart.addActionListener(this);
        lblDeck.addMouseListener(this);
    }

    public void showGame() {
        pnlDesk.removeAll();
        showDeck();
        showStacks();
        showCardLists();
        repaint();
    }

    private void showDeck() {
        if (game.deck.isEmpty()) {
            return;
        }
        Card deckCurCard = game.deck.getCurCard();
        lblDeckCur = new CardLabel(deckCurCard, this, game);

        Point posDeck = new Point(dimSpan.width, dimSpan.height);
        Rectangle recDeck = new Rectangle(posDeck.x, posDeck.y, dimCard.width, dimCard.height);
        lblDeck.setBounds(recDeck);
        pnlDesk.add(lblDeck);
        recDeck.x += dimCard.width + dimSpan.width;
        lblDeckCur.setBounds(recDeck);
        lblDeckCur.setVisible(!game.deck.isEmpty());
        if (!game.deck.isLast()) {
            Card deckPrevCard = game.deck.getPrevCard();
            if (deckPrevCard != null) {
                lblDeckPrev = new CardLabel(deckPrevCard, this, game);
                lblDeckPrev.setBounds(recDeck);
                lblDeckPrev.setVisible(true);
                pnlDesk.add(lblDeckPrev, new Integer(0));
            }
        }
        pnlDesk.add(lblDeckCur, new Integer(1));

        String deckCardName;
        if (game.deck.isEmpty() || game.deck.isFirst()) {
            deckCardName = EMPTY_CARD;
        } else {
            deckCardName = BACK_CARD;
        }
        lblDeck.setIcon(getImageIcon(deckCardName, dimCard));
    }

    private void showStacks() {
        Point posStack = new Point(dimCard.width * 3 + dimSpan.width * 4, dimSpan.height);
        Rectangle recStack = new Rectangle(posStack.x, posStack.y, dimCard.width, dimCard.height);
        for (int i = 0; i < 4; i++) {
            lblStack[i] = new JLabel();
            lblStack[i].setBounds(recStack);
            recStack.x += dimCard.width + dimSpan.width;
            pnlDesk.add(lblStack[i]);
        }

        for (int s = 0; s < game.stacks.length; s++) {
            CardStack stack = game.stacks[s];
            if (stack.isEmpty()) {
                lblStack[s].setIcon(getImageIcon(EMPTY_CARD, dimCard));
            } else {
                lblStack[s].setIcon(stack.peek().getImage(dimCard));
            }
        }
    }

    public static Icon getImageIcon(String fileName, Dimension dim) {
        try {
            BufferedImage img = ImageIO.read(new File(fileName));
            Image dimImg = img.getScaledInstance((int) dim.getWidth(), (int) dim.getHeight(), Image.SCALE_SMOOTH);
            return new ImageIcon(dimImg);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            return null;
        }
    }

    private void showCardLists() {
        int maxListSize = 0;
        int y = dimSpan.height * 2 + dimCard.height;
        Rectangle rec = new Rectangle(dimSpan.width, y, dimCard.width, dimCard.height);
        for (int l = 0; l < game.lists.length; l++) {
            CardList list = game.lists[l];
            CardLabel pre = null;
            rec.y = y;
            for (int i = 0; i < list.size(); i++) {
                CardLabel lblCard = new CardLabel(list.get(i), this, game);
                lblCard.setVisible(list.getOpenIndex() <= i);
                lblCard.setBounds(rec);
                pnlDesk.add(lblCard, new Integer(i));
                rec.y += dimSpan.height;
                if (i > 0) {
                    pre.setNext(lblCard);
                }
                pre = lblCard;
            }
            rec.x += dimSpan.width + dimCard.width;
            if (maxListSize < list.size()) {
                maxListSize = list.size();
            }
        }
        pnlDesk.setPreferredSize(new Dimension(dimFrame.width, dimCard.height + dimSpan.height * maxListSize - 1));
    }

    public static int mapListArea(Point p) {
        int yPos = dimSpan.height * 2 + dimCard.height / 2;
        if (p.y >= yPos) {
            return (p.x + dimCard.width / 2) / (dimCard.width + dimSpan.width) % 7;
        } else {
            return -1;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == miRestart) {
            game.initSolitaire();
            showGame();
        }
    }

    JLayeredPane getPnlDesk() {
        return pnlDesk;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == lblDeck) {
            if (!game.deck.isEmpty()) {
                game.deck.drawCard();
                showGame();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    public void moveStack() {
        for (int s = 0; s < Solitaire.STACK_NUM; s++) {
            Point loc = lblStack[s].getLocation();
            if (loc.y > dimFrame.height) {
                animationTimer.stop();
                game.initSolitaire();
                showGame();
            } else {
                loc.x -= Math.random() * 20 * s - 5;
                loc.y += 10;
                lblStack[s].setLocation(loc);
            }
        }
    }

    private Timer animationTimer;

    class showGameWinActionListener implements ActionListener {

        MainFrame frame;

        public showGameWinActionListener(MainFrame frame) {
            this.frame = frame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            frame.moveStack();
        }
    }

    void clearDesk() {
        Component[] cs = pnlDesk.getComponents();
        for (Component c : cs) {
            int iList = mapListArea(c.getLocation());
            if (Solitaire.validListIndex(iList)) {
                pnlDesk.remove(c);
            }
        }
        repaint();
    }

    void showGameWin() {
        clearDesk();
        ActionListener al = new showGameWinActionListener(this);
        animationTimer = new Timer(30, al);
        animationTimer.setRepeats(true);
        animationTimer.start();
    }
}
