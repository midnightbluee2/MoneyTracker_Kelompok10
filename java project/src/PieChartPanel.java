import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class PieChartPanel extends JPanel {
    private Map<String, Double> dataKategori;
    private Map<String, Color> warnaKategori;
    private String title;

    public PieChartPanel(String title) {
        this.title = title;
        this.dataKategori = new HashMap<>();
        this.warnaKategori = new HashMap<>();
        setPreferredSize(new Dimension(300, 200));
        setOpaque(false);
    }

    // update data chart dari list Expense
    public void updateDataFromExpenses(List<Expense> expenses) {
        dataKategori.clear();
        
        // hitung jumlah total per kategori
        for (Expense exp : expenses) {
            String kategori = exp.getKategori();
            double jumlah = exp.getJumlah();
            
            dataKategori.put(kategori, dataKategori.getOrDefault(kategori, 0.0) + jumlah);
        }
        
        assignColors();
        repaint();
    }

    // warna untuk kategori di pie chart
    private void assignColors() {
        Color[] paletWarna = {
            new Color(244, 67, 54),   // Merah
            new Color(233, 30, 99),   // Pink
            new Color(156, 39, 176),  // Ungu
            new Color(103, 58, 183),  // Deep Purple
            new Color(63, 81, 181),   // Indigo
            new Color(33, 150, 243),  // Biru
            new Color(0, 188, 212),   // Cyan
            new Color(0, 150, 136),   // Teal
            new Color(76, 175, 80),   // Hijau
            new Color(255, 152, 0)    // Orange
        };

        int index = 0;
        for (String kategori : dataKategori.keySet()) {
            if (!warnaKategori.containsKey(kategori)) {
                warnaKategori.put(kategori, paletWarna[index % paletWarna.length]);
                index++;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // jika tidak ada data
        if (dataKategori == null || dataKategori.isEmpty()) {
            g2d.setFont(new Font("SansSerif", Font.PLAIN, 12));
            g2d.setColor(Color.GRAY);
            String msg = "No expense data yet";
            FontMetrics fm = g2d.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(msg)) / 2;
            int y = getHeight() / 2;
            g2d.drawString(msg, x, y);
            return;
        }

        // hitung total
        double total = 0;
        for (double nilai : dataKategori.values()) {
            total += nilai;
        }

        if (total == 0) {
            g2d.setFont(new Font("SansSerif", Font.PLAIN, 12));
            g2d.setColor(Color.GRAY);
            String msg = "No expenses yet";
            FontMetrics fm = g2d.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(msg)) / 2;
            int y = getHeight() / 2;
            g2d.drawString(msg, x, y);
            return;
        }

        // gambar pie chart
        int diameter = Math.min(getWidth() - 150, getHeight() - 40);
        int x = 20;
        int y = (getHeight() - diameter) / 2;

        int startAngle = 0;
        for (Map.Entry<String, Double> entry : dataKategori.entrySet()) {
            String kategori = entry.getKey();
            double nilai = entry.getValue();
            int angle = (int) Math.round((nilai / total) * 360);

            g2d.setColor(warnaKategori.get(kategori));
            g2d.fillArc(x, y, diameter, diameter, startAngle, angle);

            startAngle += angle;
        }

        // gambar keterangan kategori
        drawLegend(g2d, diameter + 40, 20);
    }

    private void drawLegend(Graphics2D g2d, int x, int y) {
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 10));
        int offsetY = y;

        for (Map.Entry<String, Double> entry : dataKategori.entrySet()) {
            String kategori = entry.getKey();
            double nilai = entry.getValue();

            // kotak warna
            g2d.setColor(warnaKategori.get(kategori));
            g2d.fillRect(x, offsetY, 10, 10);

            // text
            g2d.setColor(Color.BLACK);
            String text = kategori + ": Rp " + String.format("%,.0f", nilai);
            g2d.drawString(text, x + 15, offsetY + 9);

            offsetY += 15;
        }
    }
}