package view;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JPanel;

/**
 *
 * @author Alan Tian 1302662
 */
public class ImagePanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private Image imageOrg = null;
    private Image image = null;

    {
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                final int w = ImagePanel.this.getWidth();
                final int h = ImagePanel.this.getHeight();
                image = w > 0 && h > 0 ? imageOrg.getScaledInstance(w, h, Image.SCALE_SMOOTH) : imageOrg;
                ImagePanel.this.repaint();
            }
        });
    }

    public ImagePanel(LayoutManager layout, final Image image) {
        super(layout);
        imageOrg = image;
        this.image = image;
    }

    public void setImage(Image image) {
        this.image = image;
        imageOrg = image;
    }

    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, null);
        }
    }
}
