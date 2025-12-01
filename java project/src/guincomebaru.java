import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class guincomebaru extends JFrame {
    // Komponen GUI
    private JTextField txtTanggal;
    private JTextField txtKategori;
    private JTextField txtDeskripsi;
    private JTextField txtJumlah;
    private JButton btnTambah;
    private JButton btnClear;
    private JLabel labelTotal;
    
    private IncomeManager manager;
    
    // Nama file untuk load
    private static final String DATA_FILE = "income.dat";
    
    // Warna??
    private Color primaryGreen = new Color(76, 175, 80);
    private Color darkGray = new Color(120, 120, 120);
    private Color lightGray = new Color(240, 240, 240);
    
    public guincomebaru() {
        manager = new IncomeManager();
        
        // AUTO-LOAD DATA SAAT PROGRAM MULAI
        if (manager.fileExists(DATA_FILE)) {
            manager.loadFromFile(DATA_FILE);
            System.out.println("✅ Data dimuat: " + manager.ambilsemuaincome().size() + " transaksi");
            System.out.println("💰 Total: Rp " + String.format("%,.0f", manager.totalincome()));
        } else {
            System.out.println("File data belum ada, mulai dengan data kosong");
        }
        
        buatTampilan();
        
        // Update total setelah load data
        updateTabel();
        
        setTitle("INCOME - PEMASUKAN");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        
        // AUTO-SAVE SAAT PROGRAM DITUTUP
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                manager.saveToFile(DATA_FILE);
                System.out.println("💾 Data otomatis tersimpan saat keluar");
            }
        });
    }
    
    private void buatTampilan() {
        setLayout(new BorderLayout(20, 20));
        
        // ===== PANEL UTAMA =====
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        //icon panel
        JPanel iconPanel = new JPanel();
        iconPanel.setBackground(Color.WHITE);
        iconPanel.setLayout(new BoxLayout(iconPanel, BoxLayout.Y_AXIS));
        
        // Icon Income (pakai emoji besar)
        JLabel iconIncome = new JLabel("💰", SwingConstants.CENTER);
        iconIncome.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 120));
        iconIncome.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblTitle = new JLabel("INCOME", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(primaryGreen);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        iconPanel.add(Box.createVerticalGlue());
        iconPanel.add(iconIncome);
        iconPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        iconPanel.add(lblTitle);
        iconPanel.add(Box.createVerticalGlue());
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 6;
        gbc.weightx = 0.3;
        mainPanel.add(iconPanel, gbc);
        
        // PANEL KANAN
        gbc.gridheight = 1;
        gbc.weightx = 0.7;
        gbc.gridx = 1;
        
        // Header PEMASUKAN
        gbc.gridy = 0;
        JLabel lblHeader = new JLabel("PEMASUKAN");
        lblHeader.setFont(new Font("Arial", Font.BOLD, 18));
        lblHeader.setForeground(Color.WHITE);
        lblHeader.setBackground(darkGray);
        lblHeader.setOpaque(true);
        lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
        lblHeader.setPreferredSize(new Dimension(400, 50));
        lblHeader.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        makeRounded(lblHeader, 25);
        mainPanel.add(lblHeader, gbc);
        
        // Field TANGGAL
        gbc.gridy = 1;
        JTextField txtTanggalRounded = createRoundedTextField("TANGGAL");
        txtTanggal = txtTanggalRounded;
        mainPanel.add(txtTanggalRounded, gbc);
        
        // Field KATEGORI
        gbc.gridy = 2;
        JTextField txtKategoriRounded = createRoundedTextField("KATEGORI");
        txtKategori = txtKategoriRounded;
        mainPanel.add(txtKategoriRounded, gbc);
        
        // Field DESKRIPSI
        gbc.gridy = 3;
        JTextField txtDeskripsiRounded = createRoundedTextField("DESKRIPSI");
        txtDeskripsi = txtDeskripsiRounded;
        mainPanel.add(txtDeskripsiRounded, gbc);
        
        // Field JUMLAH
        gbc.gridy = 4;
        JTextField txtJumlahRounded = createRoundedTextField("JUMLAH");
        txtJumlah = txtJumlahRounded;
        mainPanel.add(txtJumlahRounded, gbc);
        
        // Button ADD
        gbc.gridy = 5;
        btnTambah = new JButton("ADD");
        btnTambah.setFont(new Font("Arial", Font.BOLD, 16));
        btnTambah.setForeground(Color.WHITE);
        btnTambah.setBackground(darkGray);
        btnTambah.setFocusPainted(false);
        btnTambah.setBorderPainted(false);
        btnTambah.setPreferredSize(new Dimension(400, 50));
        btnTambah.setCursor(new Cursor(Cursor.HAND_CURSOR));
        makeRounded(btnTambah, 25);
        mainPanel.add(btnTambah, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
        
        //PANEL BAWAH: BUTTONS
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 30, 30));
        
        // Button Panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setBackground(Color.WHITE);
        
        btnClear = new JButton("Clear Form");
        btnClear.setFont(new Font("Arial", Font.BOLD, 13));
        btnClear.setForeground(Color.WHITE);
        btnClear.setBackground(new Color(255, 152, 0));
        btnClear.setFocusPainted(false);
        btnClear.setBorderPainted(false);
        btnClear.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnPanel.add(btnClear);
        
        // Total Panel
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.setBackground(Color.WHITE);
        
        JLabel lblTotalText = new JLabel("Total Pemasukan: ");
        lblTotalText.setFont(new Font("Arial", Font.BOLD, 16));
        
        labelTotal = new JLabel("Rp 0");
        labelTotal.setFont(new Font("Arial", Font.BOLD, 18));
        labelTotal.setForeground(primaryGreen);
        
        totalPanel.add(lblTotalText);
        totalPanel.add(labelTotal);
        
        bottomPanel.add(btnPanel, BorderLayout.NORTH);
        bottomPanel.add(totalPanel, BorderLayout.SOUTH);
        
        add(bottomPanel, BorderLayout.SOUTH);
        
        // HANDLERS
        btnTambah.addActionListener(e -> tambahData());
        btnClear.addActionListener(e -> clearForm());
        
        btnTambah.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnTambah.setBackground(new Color(90, 90, 90));
            }
            public void mouseExited(MouseEvent e) {
                btnTambah.setBackground(darkGray);
            }
        });
        
        btnClear.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnClear.setBackground(new Color(230, 137, 0));
            }
            public void mouseExited(MouseEvent e) {
                btnClear.setBackground(new Color(255, 152, 0));
            }
        });
    }
    
    // buat rounded text field
    private JTextField createRoundedTextField(String placeholder) {
        JTextField txt = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        
        txt.setFont(new Font("Arial", Font.PLAIN, 14));
        txt.setPreferredSize(new Dimension(400, 45));
        txt.setBackground(lightGray);
        txt.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        txt.setOpaque(false);
        
        // Placeholder effect
        txt.setForeground(Color.GRAY);
        txt.setText(placeholder);
        
        txt.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (txt.getText().equals(placeholder)) {
                    txt.setText("");
                    txt.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (txt.getText().isEmpty()) {
                    txt.setForeground(Color.GRAY);
                    txt.setText(placeholder);
                }
            }
        });
        
        return txt;
    }
    
    // untuk rounded component
    private void makeRounded(JComponent component, int radius) {
        component.setBorder(new RoundedBorder(radius));
    }
    
    // Custom rounded border
    class RoundedBorder implements javax.swing.border.Border {
        private int radius;
        
        RoundedBorder(int radius) {
            this.radius = radius;
        }
        
        public Insets getBorderInsets(Component c) {
            return new Insets(10, 20, 10, 20);
        }
        
        public boolean isBorderOpaque() {
            return false;
        }
        
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
    }
    
    private void tambahData() {
        String Tanggal = txtTanggal.getText().trim();
        String Kategori = txtKategori.getText().trim();
        String Deskripsi = txtDeskripsi.getText().trim();
        String JumlahText = txtJumlah.getText().trim();
        
        // Check placeholder
        if (Tanggal.equals("TANGGAL") || Kategori.equals("KATEGORI") || 
            Deskripsi.equals("DESKRIPSI") || JumlahText.equals("JUMLAH") ||
            Tanggal.isEmpty() || Kategori.isEmpty() || Deskripsi.isEmpty() || JumlahText.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Semua field harus diisi!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        double Jumlah;
        try {
            Jumlah = Double.parseDouble(JumlahText);
            if (Jumlah <= 0) {
                JOptionPane.showMessageDialog(this, 
                    "Jumlah harus lebih dari 0!", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Jumlah harus berupa angka!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Tambah income
        income incomenya = new income(Tanggal, Kategori, Deskripsi, Jumlah);
        manager.tambahincome(incomenya);
        
        // AUTO-SAVE SETIAP TAMBAH DATA
        manager.saveToFile(DATA_FILE);
        System.out.println("Data tersimpan otomatis");
        
        updateTabel();
        clearForm();
        
        // Tampilkan info sukses dengan total
        JOptionPane.showMessageDialog(this, 
            "Data berhasil ditambahkan!\n\n" +
            "Total Transaksi: " + manager.ambilsemuaincome().size() + "\n" +
            "Total Pemasukan: Rp " + String.format("%,.0f", manager.totalincome()), 
            "Berhasil", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void clearForm() {
        txtTanggal.setText("TANGGAL");
        txtTanggal.setForeground(Color.GRAY);
        txtKategori.setText("KATEGORI");
        txtKategori.setForeground(Color.GRAY);
        txtDeskripsi.setText("DESKRIPSI");
        txtDeskripsi.setForeground(Color.GRAY);
        txtJumlah.setText("JUMLAH");
        txtJumlah.setForeground(Color.GRAY);
    }
    
    private void updateTabel() {
        double total = manager.totalincome();
        labelTotal.setText(String.format("Rp %,.0f", total));
    }
    
    public IncomeManager getManager() {
        return manager;
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            guincomebaru gui = new guincomebaru();
            gui.setVisible(true);
        });
    }
}