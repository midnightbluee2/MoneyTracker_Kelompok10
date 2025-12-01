import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class ExpenseGUI extends JFrame {
    // Komponen GUI
    private JTextField txtTanggal;
    private JComboBox<String> cmbKategori;
    private JTextField txtDeskripsi;
    private JTextField txtJumlah;
    private JButton btnTambah;
    private JButton btnClear;
    private JLabel labelTotal;
    
    // Nama file untuk save/load
    private static final String DATA_FILE = "expense.dat";
    
    // Colors
    private Color primaryRed = new Color(244, 67, 54);
    private Color darkGray = new Color(120, 120, 120);
    private Color lightGray = new Color(240, 240, 240);
    
    public ExpenseGUI() {
        // ===== AUTO-LOAD DATA SAAT PROGRAM MULAI =====
        try {
            Expense.loadFromFile();
            System.out.println("✅ Data dimuat: " + Expense.getDaftarExpense().size() + " transaksi");
            System.out.println("💸 Total: Rp " + String.format("%,.0f", Expense.getTotalExpense()));
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("ℹ️ File data belum ada, mulai dengan data kosong");
        }
        
        buatTampilan();
        
        // Update total setelah load data
        updateTotal();
        
        setTitle("EXPENSE - PENGELUARAN");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        
        // ===== AUTO-SAVE SAAT PROGRAM DITUTUP =====
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                try {
                    Expense.saveToFile();
                    System.out.println("💾 Data otomatis tersimpan saat keluar");
                } catch (IOException e) {
                    System.out.println("❌ Error saat menyimpan: " + e.getMessage());
                }
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
        
        // ===== ICON/LOGO PANEL (KIRI) =====
        JPanel iconPanel = new JPanel();
        iconPanel.setBackground(Color.WHITE);
        iconPanel.setLayout(new BoxLayout(iconPanel, BoxLayout.Y_AXIS));
        
        // Icon Expense
        JLabel iconExpense = new JLabel("💸", SwingConstants.CENTER);
        iconExpense.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 120));
        iconExpense.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblTitle = new JLabel("EXPENSE", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(primaryRed);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        iconPanel.add(Box.createVerticalGlue());
        iconPanel.add(iconExpense);
        iconPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        iconPanel.add(lblTitle);
        iconPanel.add(Box.createVerticalGlue());
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 6;
        gbc.weightx = 0.3;
        mainPanel.add(iconPanel, gbc);
        
        // ===== FORM PANEL (KANAN) =====
        gbc.gridheight = 1;
        gbc.weightx = 0.7;
        gbc.gridx = 1;
        
        // Header PENGELUARAN
        gbc.gridy = 0;
        JLabel lblHeader = new JLabel("PENGELUARAN");
        lblHeader.setFont(new Font("Arial", Font.BOLD, 18));
        lblHeader.setForeground(Color.WHITE);
        lblHeader.setBackground(darkGray);
        lblHeader.setOpaque(true);
        lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
        lblHeader.setPreferredSize(new Dimension(400, 50));
        lblHeader.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        makeRounded(lblHeader, 25);
        mainPanel.add(lblHeader, gbc);
        
        // Field TANGGAL (dd-MM-yyyy)
        gbc.gridy = 1;
        JTextField txtTanggalRounded = createRoundedTextField("TANGGAL (dd-MM-yyyy)");
        txtTanggal = txtTanggalRounded;
        mainPanel.add(txtTanggalRounded, gbc);
        
        // ComboBox KATEGORI
        gbc.gridy = 2;
        String[] kategoriList = {"Makanan", "Transportasi", "Hiburan", "Belanja", "Tagihan", "Lainnya"};
        cmbKategori = new JComboBox<>(kategoriList);
        cmbKategori.setFont(new Font("Arial", Font.PLAIN, 14));
        cmbKategori.setPreferredSize(new Dimension(400, 45));
        cmbKategori.setBackground(lightGray);
        cmbKategori.setCursor(new Cursor(Cursor.HAND_CURSOR));
        mainPanel.add(cmbKategori, gbc);
        
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
        
        // ===== PANEL BAWAH: BUTTONS & TOTAL =====
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
        
        JLabel lblTotalText = new JLabel("Total Pengeluaran: ");
        lblTotalText.setFont(new Font("Arial", Font.BOLD, 16));
        
        labelTotal = new JLabel("Rp 0");
        labelTotal.setFont(new Font("Arial", Font.BOLD, 18));
        labelTotal.setForeground(primaryRed);
        
        totalPanel.add(lblTotalText);
        totalPanel.add(labelTotal);
        
        bottomPanel.add(btnPanel, BorderLayout.NORTH);
        bottomPanel.add(totalPanel, BorderLayout.SOUTH);
        
        add(bottomPanel, BorderLayout.SOUTH);
        
        // ===== EVENT HANDLERS =====
        btnTambah.addActionListener(e -> tambahData());
        btnClear.addActionListener(e -> clearForm());
        
        // Hover effects
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
    
    // Helper untuk buat rounded text field
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
    
    // Helper untuk rounded component
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
        String tanggal = txtTanggal.getText().trim();
        String kategori = (String) cmbKategori.getSelectedItem();
        String deskripsi = txtDeskripsi.getText().trim();
        String jumlahText = txtJumlah.getText().trim();
        
        // Check placeholder
        if (tanggal.equals("TANGGAL (dd-MM-yyyy)") || tanggal.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "❌ Tanggal harus diisi!\nFormat: dd-MM-yyyy (contoh: 01-12-2024)", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (deskripsi.equals("DESKRIPSI (opsional, ketik - jika kosong)")) {
            deskripsi = "-";
        }
        
        if (jumlahText.equals("JUMLAH") || jumlahText.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "❌ Jumlah harus diisi!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Gunakan method dari InputExpense untuk validasi
        String result = InputExpense.tambahExpense(tanggal, kategori, deskripsi, jumlahText);
        
        if (InputExpense.isSuccess(result)) {
            // ===== AUTO-SAVE SETIAP TAMBAH DATA =====
            try {
                Expense.saveToFile();
                System.out.println("💾 Data tersimpan otomatis");
            } catch (IOException e) {
                System.out.println("❌ Error saat menyimpan: " + e.getMessage());
            }
            
            updateTotal();
            clearForm();
            
            // Tampilkan info sukses dengan total
            JOptionPane.showMessageDialog(this, 
                "✅ Data berhasil ditambahkan!\n\n" +
                "📊 Total Transaksi: " + Expense.getDaftarExpense().size() + "\n" +
                "💸 Total Pengeluaran: Rp " + String.format("%,.0f", Expense.getTotalExpense()), 
                "Sukses", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Tampilkan error dari validasi
            JOptionPane.showMessageDialog(this, 
                "❌ " + result, 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clearForm() {
        txtTanggal.setText("TANGGAL (dd-MM-yyyy)");
        txtTanggal.setForeground(Color.GRAY);
        cmbKategori.setSelectedIndex(0);
        txtDeskripsi.setText("DESKRIPSI (opsional, ketik - jika kosong)");
        txtDeskripsi.setForeground(Color.GRAY);
        txtJumlah.setText("JUMLAH");
        txtJumlah.setForeground(Color.GRAY);
    }
    
    private void updateTotal() {
        double total = Expense.getTotalExpense();
        labelTotal.setText(String.format("Rp %,.0f", total));
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            ExpenseGUI gui = new ExpenseGUI();
            gui.setVisible(true);
        });
    }
}