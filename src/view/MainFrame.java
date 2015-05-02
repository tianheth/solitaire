package view;

import controller.Solitaire;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;
import model.*;

/**
 *
 * @author Alan Tian 1302662
 */
public class MainFrame extends JFrame implements ActionListener, MouseListener {

    // game panel
    public static final String NEW_GAME = "New Game";

    private JPanel pnlGame = new JPanel();
    private JButton btnNewGame = new JButton(NEW_GAME);
    private JTextArea txtGameInfor = new JTextArea();
    // desk panel
    private JLayeredPane pnlDesk = new JLayeredPane();
    private JLabel lblDeck = new JLabel();
    private CardLabel lblDeckCur;
    private CardLabel lblDeckPrev;
//    private CardLabel lblDeckPre = new CardLabel();
    private JLabel[] lblStack = new JLabel[4];

    // 
    public static Color COLOR_HOMETEAM = new Color(255, 100, 100);
    public static Color COLOR_GUESTTEAM = new Color(100, 100, 255);
    public static Dimension DIM_BUTTON = new Dimension(100, 40);
//    public static Dimension DIM_CARD = new Dimension(142, 192);
//    public static final int CARD_SPAN = 40;
    public static Font FONT_MONO = new java.awt.Font("Courier New", 0, 11);

    public static Dimension dimCard;
    public static Dimension dimSpan;
    public static Dimension dimFrame;

    private Solitaire game;
    public static final String EMPTY_CARD = "images\\empty.gif";
    public static final String BACK_CARD = "images\\back.png";

    private void initGamePanel(Dimension d) {
        pnlGame.setBorder(BorderFactory.createTitledBorder("Game Information"));
        pnlGame.setPreferredSize(d);

        btnNewGame.setPreferredSize(DIM_BUTTON);
        txtGameInfor.setPreferredSize(new Dimension(d.width - 30, d.height / 5));

        pnlGame.add(btnNewGame);
        pnlGame.add(txtGameInfor);
    }

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
        dimFrame = new Dimension(dimScreen.width * 2 / 3 * 6 / 5, dimScreen.height * 2 / 3);
        this.setSize(dimFrame);

        this.setLocation((dimScreen.width - dimFrame.width) / 2,
                (dimScreen.height - dimFrame.height) / 2);

        dimCard = new Dimension(dimFrame.width * 25 / 6 / (6 * 7 + 3), dimFrame.height * 5 / (1 + 5 + 2 + 5 + 7));
        dimSpan = new Dimension(dimCard.width / 5, dimCard.height / 5);

        initDeskPanel();
        initGamePanel(new Dimension(dimFrame.width / 5, dimFrame.height));

        this.add(pnlGame, BorderLayout.EAST);
        this.add(pnlDesk);

        addListener();
    }

    private void addListener() {
        btnNewGame.addActionListener(this);
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
        Card deckPrevCard = game.deck.getPrevCard();
        if (deckPrevCard != null) {
            lblDeckPrev = new CardLabel(deckPrevCard, this, game);
            lblDeckPrev.setBounds(recDeck);
            lblDeckPrev.setVisible(true);
            pnlDesk.add(lblDeckPrev, new Integer(0));
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
//                pnlLowerDesk.add(lblCard, new Integer(i));
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
        pnlDesk.setPreferredSize(new Dimension(dimFrame.width * 4 / 5, dimCard.height + dimSpan.height * maxListSize - 1));
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
        if (e.getActionCommand().equalsIgnoreCase(NEW_GAME)) {
            game.initSolitaire();
//            showGameWin();
            showGame();
//            clearDesk();
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
