import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class GradientColor extends JPanel{
    private Color colorTop;
    private Color colorBottom;

    public GradientColor(Color colorTop, Color colorBottom){
        this.colorTop =  colorTop;
        this.colorBottom = colorBottom;
        this.setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        GradientPaint gradient = new GradientPaint(0, 0, colorTop, getWidth(), getHeight(),colorBottom);

        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());

    }

    public void setColors(Color top, Color bottom) {
        this.colorTop = top;
        this.colorBottom = bottom;
        repaint(); // Gambar ulang panelnya
    }
    
}
