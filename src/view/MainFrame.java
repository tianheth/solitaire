package view;

import controller.Solitaire;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
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
import javax.swing.border.TitledBorder;
import model.*;

/**
 *
 * @author Alan Tian <alan.tian at aut.ac.nz>
 */
public class MainFrame extends JFrame implements ActionListener, MouseListener, MouseMotionListener{

    // game panel
    public static final String NEW_GAME = "New Game";

    private JPanel pnlGame = new JPanel();
    private JButton btnNewGame = new JButton(NEW_GAME);
    private JTextArea txtGameInfor = new JTextArea();
    // desk panel
    private JPanel pnlDesk = new JPanel();
    private JPanel pnlUpperDesk = new JPanel();
    private JPanel pnlLowerDesk = new JPanel();
    private JLabel lblUserAction = new JLabel("User Play: ");
    private JLayeredPane[] pnlCardList = new JLayeredPane[7];
    private JLabel lblDeck = new JLabel();
    private CardLabel lblDeckCur = new CardLabel();
//    private CardLabel lblDeckPre = new CardLabel();
    private JLabel[] lblStack = new JLabel[4];
    private ImagePanel pnlUserDesk;

    // 
    private static Color COLOR_HOMETEAM = new Color(255, 100, 100);
    private static Color COLOR_GUESTTEAM = new Color(100, 100, 255);
    private static Dimension DIM_BUTTON = new Dimension(100, 40);
    private static Font FONT_MONO = new java.awt.Font("Courier New", 0, 11);
    private static Dimension DIM_CARD = new Dimension(142, 192);

    private Solitaire game;

    private void initGamePanel() {
        pnlGame.setBorder(BorderFactory.createTitledBorder("Game Information"));
        pnlGame.setPreferredSize(new Dimension(250, 600));

        btnNewGame.setPreferredSize(DIM_BUTTON);
        txtGameInfor.setPreferredSize(new Dimension(200, 200));

        pnlGame.add(btnNewGame);
        pnlGame.add(txtGameInfor);
    }

    private void initDeskPanel() {
        Image imgDesk = Toolkit.getDefaultToolkit().createImage("images\\summit.png");
        pnlUserDesk = new ImagePanel(new BorderLayout(), imgDesk);

        for (int i = 0; i < 7; i++) {
            pnlCardList[i] = new JLayeredPane();
            pnlLowerDesk.add(pnlCardList[i], BorderLayout.WEST);
        }
        lblDeck.setPreferredSize(DIM_CARD);
        pnlUpperDesk.add(lblDeck, BorderLayout.WEST);
        pnlUpperDesk.add(lblDeckCur, BorderLayout.WEST);
        for (int i = 0; i < 4; i++) {
            lblStack[i] = new JLabel();
            pnlUpperDesk.add(lblStack[i], BorderLayout.WEST);
            lblStack[i].setPreferredSize(DIM_CARD);
        }
        lblDeck.setPreferredSize(DIM_CARD);
        lblDeckCur.setPreferredSize(DIM_CARD);

        lblUserAction.setPreferredSize(new Dimension(200, 200));
        lblUserAction.setAlignmentY(Component.BOTTOM_ALIGNMENT);

        pnlLowerDesk.setBorder(BorderFactory.createTitledBorder(null, "Card Lists", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, new Color(255, 255, 255)));
        pnlUpperDesk.setBorder(BorderFactory.createTitledBorder(null, "Card Deck and Stacks", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, new Color(255, 255, 255)));

        pnlUserDesk.add(pnlUpperDesk, BorderLayout.NORTH);
        pnlUserDesk.add(pnlLowerDesk, BorderLayout.CENTER);

        pnlDesk.add(pnlUserDesk);

        showGame();
    }

    public MainFrame(Solitaire game) {
        this.game = game;

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Solitaire Game");

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int frameWidth = dim.width * 2 / 3;
        int frameHeight = dim.height * 2 / 3;
        this.setSize(frameWidth, frameHeight);

        this.setLocation((dim.width - frameWidth) / 2,
                (dim.height - frameHeight) / 2);

        initDeskPanel();
        initGamePanel();

        this.add(pnlGame, BorderLayout.EAST);
        this.add(pnlDesk, BorderLayout.CENTER);

        addListener();
    }

    private void addListener() {
        btnNewGame.addActionListener(this);
        lblDeck.addMouseListener(this);
    }

    private void showGame() {
        showDeck();
        showStacks();
        showCardLists();
    }

    private void showDeck() {
        String deckCardName;
        if (game.deck.isEmpty() || game.deck.isFirst()) {
            deckCardName = "images\\empty.gif";
        } else {
            deckCardName = "images\\back.png";
        }
        lblDeck.setIcon(getImageIcon(deckCardName, DIM_CARD));

        if (game.deck.isEmpty()) {
            lblDeckCur.setIcon(getImageIcon("images\\empty.gif", DIM_CARD));
        } else {
            lblDeckCur.setIcon(game.deck.getCurCard().getImage(DIM_CARD));
        }
    }

    private void showStacks() {
        for (int s = 0; s < game.stacks.length; s++) {
            CardStack stack = game.stacks[s];
            if (stack.isEmpty()) {
                lblStack[s].setIcon(getImageIcon("images\\empty.gif", DIM_CARD));
            } else {
                lblStack[s].setIcon(stack.peek().getImage(DIM_CARD));
            }
        }
    }

    public Icon getImageIcon(String fileName, Dimension dim) {
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
        for (int l = 0; l < game.lists.length; l++) {
            pnlCardList[l].removeAll();
            Rectangle rec = new Rectangle(10, 10, DIM_CARD.width, DIM_CARD.height);
            CardList list = game.lists[l];
            for (int i = 0; i < list.size(); i++) {
                CardLabel lblCardImg = new CardLabel(list.get(i), DIM_CARD);
                lblCardImg.setBounds(rec);
                pnlCardList[l].add(lblCardImg, new Integer(i));
                rec.y += 40;
            }
            if (maxListSize < list.size()) {
                maxListSize = list.size();
            }
        }
        for (int l = 0; l < game.lists.length; l++) {
            pnlCardList[l].setPreferredSize(new Dimension(DIM_CARD.width + 10, DIM_CARD.height + 40 * maxListSize - 1));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand().equalsIgnoreCase(NEW_GAME)) {
            game.initSolitaire();
            showGame();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == lblDeck) {
            game.deck.drawCard();
            showDeck();
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
        if (e.getSource() == lblDeck) {
            lblDeck.setBackground(COLOR_HOMETEAM);
        }
        else if(e.getSource() == lblDeckCur){
            lblDeck.setBackground(COLOR_HOMETEAM);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (e.getSource() == lblDeck) {
            lblDeck.setBackground(COLOR_GUESTTEAM);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

}
