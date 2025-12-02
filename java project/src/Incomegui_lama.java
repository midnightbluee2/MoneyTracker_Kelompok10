import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Incomegui_lama extends JFrame {
    private JSpinner dateSpinner; 
    private JComboBox<String> cmbKategori; 
    private JTextField txtKategoriLain; 
    private JTextField txtDeskripsi;
    private JTextField txtJumlah;
    private JButton btnTambah;
    private JButton btnHapus;
    private JButton btnClear;
    private JButton btnBack; 
    private JTable tabelIncome;
    private DefaultTableModel modelTabel;
    private JLabel labelTotal;

    private IncomeManager manager;
    private String currentUsername;
    private Runnable onCloseCallback; 

    private Color primaryGreen = new Color(76, 175, 80);
    private Color darkGray = new Color(120, 120, 120);
    private Color lightGray = new Color(240, 240, 240);

    // constructor dengan callback
    public Incomegui_lama(String namauser, Runnable onCloseCallback) {
        this.currentUsername = namauser;
        this.onCloseCallback = onCloseCallback;
        manager = new IncomeManager();
        IncomeManager.setInstance(manager);

        try {
            manager.loadData(currentUsername);
            System.out.println("Data Income untuk " + currentUsername + " berhasil dimuat.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("File data Income belum ada, memulai dengan data kosong.");
        }

        buatTampilan();


        setTitle("INCOME");
        setSize(500, 700);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // 
        setLocationRelativeTo(null);
        setResizable(false);

        
        setContentPane(new GradientColor(new Color(240, 255, 240), new Color(200, 230, 200)));
        buatTampilan();
        updateTabel();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleBack();
            }
        });
    }

    private void buatTampilan() {
        setLayout(new BorderLayout(20, 20));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setOpaque(false); // 
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Icon Panel
        JPanel iconPanel = new JPanel();
        iconPanel.setOpaque(false);
        iconPanel.setLayout(new BoxLayout(iconPanel, BoxLayout.Y_AXIS));

        JLabel iconIncome = new JLabel("💰", SwingConstants.CENTER);
        iconIncome.setFont(new Font("SansSerif", Font.PLAIN, 80));
        iconIncome.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblUsername = new JLabel("Hi, " + currentUsername + "!", SwingConstants.CENTER);
        lblUsername.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblUsername.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitle = new JLabel("INCOME", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitle.setForeground(primaryGreen);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        iconPanel.add(Box.createVerticalGlue());
        iconPanel.add(iconIncome);
        iconPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        iconPanel.add(lblTitle);
        iconPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        iconPanel.add(lblUsername);
        iconPanel.add(Box.createVerticalGlue());

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 6;
        gbc.weightx = 0.2;
        mainPanel.add(iconPanel, gbc);

        gbc.gridheight = 1;
        gbc.weightx = 0.8;
        gbc.gridx = 1;

        // Header
        gbc.gridy = 0;
        JLabel lblHeader = new JLabel("INCOME");
        lblHeader.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblHeader.setForeground(Color.WHITE);
        lblHeader.setBackground(darkGray);
        lblHeader.setOpaque(true);
        lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
        lblHeader.setPreferredSize(new Dimension(400, 45));
        lblHeader.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        makeRounded(lblHeader, 25);
        mainPanel.add(lblHeader, gbc);

        // date spinner (bukan isi manual)
        gbc.gridy = 1;
        dateSpinner = createDateSpinner();
        mainPanel.add(dateSpinner, gbc);

        // dropdown kategori
        gbc.gridy = 2;
        cmbKategori = createKategoriComboBox();
        mainPanel.add(cmbKategori, gbc);

        // untuk kategori lain
        gbc.gridy = 3;
        txtKategoriLain = createRoundedTextField("Ketik kategori lain...");
        txtKategoriLain.setVisible(false);
        mainPanel.add(txtKategoriLain, gbc);

        gbc.gridy = 4;
        txtDeskripsi = createRoundedTextField("DESKRIPSI (Optional)");
        mainPanel.add(txtDeskripsi, gbc);

        gbc.gridy = 5;
        txtJumlah = createRoundedTextField("JUMLAH (Angka tanpa koma)");
        mainPanel.add(txtJumlah, gbc);

        add(mainPanel, BorderLayout.NORTH);

        // Bottom Panel
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 30, 30));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnPanel.setOpaque(false);

        btnTambah = new JButton("Add Data");
        btnTambah.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnTambah.setForeground(Color.WHITE);
        btnTambah.setBackground(primaryGreen);
        btnTambah.setFocusPainted(false);
        btnTambah.setBorderPainted(false);
        btnTambah.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnClear = new JButton("Clear");
        btnClear.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnClear.setForeground(Color.WHITE);
        btnClear.setBackground(new Color(255, 152, 0));
        btnClear.setFocusPainted(false);
        btnClear.setBorderPainted(false);
        btnClear.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnHapus = new JButton("Delete");
        btnHapus.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnHapus.setForeground(Color.WHITE);
        btnHapus.setBackground(new Color(244, 67, 54));
        btnHapus.setFocusPainted(false);
        btnHapus.setBorderPainted(false);
        btnHapus.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // ✅ TOMBOL BACK
        btnBack = new JButton("Back");
        btnBack.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnBack.setForeground(Color.WHITE);
        btnBack.setBackground(new Color(96, 125, 139));
        btnBack.setFocusPainted(false);
        btnBack.setBorderPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnPanel.add(btnTambah);
        btnPanel.add(btnClear);
        btnPanel.add(btnHapus);
        btnPanel.add(btnBack);

        String[] namaKolom = { "No", "Tanggal", "Kategori", "Deskripsi", "Jumlah" };
        modelTabel = new DefaultTableModel(namaKolom, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelIncome = new JTable(modelTabel);
        tabelIncome.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabelIncome.setRowHeight(30);
        tabelIncome.setSelectionBackground(new Color(200, 230, 201));
        tabelIncome.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        tabelIncome.getTableHeader().setBackground(primaryGreen);
        tabelIncome.getTableHeader().setForeground(Color.WHITE);
        tabelIncome.getTableHeader().setPreferredSize(new Dimension(0, 35));

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (column == 4) {
                    ((JLabel) c).setHorizontalAlignment(SwingConstants.RIGHT);
                } else if (column == 0) {
                    ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                } else {
                    ((JLabel) c).setHorizontalAlignment(SwingConstants.LEFT);
                }

                if (!isSelected) {
                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.BLACK);
                }

                setOpaque(true);
                return c;
            }
        };

        for (int i = 0; i < tabelIncome.getColumnCount(); i++) {
            tabelIncome.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        tabelIncome.getColumnModel().getColumn(0).setPreferredWidth(30);
        tabelIncome.getColumnModel().getColumn(4).setPreferredWidth(120);

        JScrollPane scrollPane = new JScrollPane(tabelIncome);
        scrollPane.setPreferredSize(new Dimension(700, 250));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.setOpaque(false);

        JLabel lblTotalText = new JLabel("Total Pemasukan: ");
        lblTotalText.setFont(new Font("SansSerif", Font.BOLD, 16));

        labelTotal = new JLabel("Rp 0");
        labelTotal.setFont(new Font("SansSerif", Font.BOLD, 18));
        labelTotal.setForeground(primaryGreen);

        totalPanel.add(lblTotalText);
        totalPanel.add(labelTotal);

        bottomPanel.add(btnPanel, BorderLayout.NORTH);
        bottomPanel.add(scrollPane, BorderLayout.CENTER);
        bottomPanel.add(totalPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.CENTER);

        // Action Listeners
        btnTambah.addActionListener(e -> tambahData());
        btnHapus.addActionListener(e -> hapusData());
        btnClear.addActionListener(e -> clearForm());
        btnBack.addActionListener(e -> handleBack());
    }

    // untuk date spinner
    private JSpinner createDateSpinner() {
        SpinnerDateModel model = new SpinnerDateModel();
        JSpinner spinner = new JSpinner(model);
        
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "dd-MM-yyyy");
        spinner.setEditor(editor);
        
        spinner.setFont(new Font("SansSerif", Font.PLAIN, 14));
        spinner.setPreferredSize(new Dimension(400, 45));
        ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().setBackground(lightGray);
        
        return spinner;
    }

    // untuk kategori
    private JComboBox<String> createKategoriComboBox() {
        String[] kategori = {
            "Gaji",
            "Uang Saku",
            "Investasi",
            "Hadiah",
            "Freelance",
            "Lain-lain"
        };
        
        JComboBox<String> combo = new JComboBox<>(kategori);
        combo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        combo.setPreferredSize(new Dimension(400, 45));
        combo.setBackground(lightGray);
        
        combo.addActionListener(e -> {
            if (combo.getSelectedItem().equals("Lain-lain")) {
                txtKategoriLain.setVisible(true);
            } else {
                txtKategoriLain.setVisible(false);
                txtKategoriLain.setText("Type another category");
                txtKategoriLain.setForeground(Color.GRAY);
            }

            txtKategoriLain.getParent().revalidate();
            txtKategoriLain.getParent().repaint();
        });
        
        return combo;
    }

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

        txt.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txt.setPreferredSize(new Dimension(400, 45));
        txt.setBackground(lightGray);
        txt.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        txt.setOpaque(false);

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

    private void makeRounded(JComponent component, int radius) {
        component.setBorder(new RoundedBorder(radius));
    }

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
        }
    }

    private void tambahData() {
        // data snipper
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String tanggal = sdf.format((Date) dateSpinner.getValue());
        
        // kategori
        String kategori = (String) cmbKategori.getSelectedItem();
        if (kategori.equals("Lain-lain")) {
            kategori = txtKategoriLain.getText().trim();
            if (kategori.isEmpty() || kategori.equals("Ketik kategori lain...")) {
                JOptionPane.showMessageDialog(this,
                    "Kategori 'Lain-lain' harus diisi!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        String deskripsi = txtDeskripsi.getText().trim().equals("DESKRIPSI (Optional)") ? "-"
                : txtDeskripsi.getText().trim();
        String jumlahText = txtJumlah.getText().trim();

        if (jumlahText.equals("JUMLAH (Angka tanpa koma)")) {
            JOptionPane.showMessageDialog(this,
                    "Field Jumlah wajib diisi!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String result = manager.tambahIncome(tanggal, kategori, deskripsi, jumlahText);

        if (!result.equals("SUCCESS")) {
            JOptionPane.showMessageDialog(this,
                    result,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        updateTabel();
        clearForm();

        JOptionPane.showMessageDialog(this,
                "Data berhasil ditambahkan!",
                "Sukses",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void hapusData() {
        int barisTerpilih = tabelIncome.getSelectedRow();

        if (barisTerpilih == -1) {
            JOptionPane.showMessageDialog(this,
                    "Pilih data yang ingin dihapus!",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int konfirmasi = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin menghapus data ini?",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION);

        if (konfirmasi == JOptionPane.YES_OPTION) {
            boolean success = manager.removeIncome(barisTerpilih);
            if (success) {
                updateTabel();
                JOptionPane.showMessageDialog(this,
                        "Data berhasil dihapus!",
                        "Sukses",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Gagal menghapus data!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        dateSpinner.setValue(new Date());
        cmbKategori.setSelectedIndex(0);
        txtKategoriLain.setVisible(false);
        txtDeskripsi.setText("DESKRIPSI (Opsional)");
        txtDeskripsi.setForeground(Color.GRAY);
        txtJumlah.setText("JUMLAH (Angka tanpa koma)");
        txtJumlah.setForeground(Color.GRAY);
    }

    private void updateTabel() {
        modelTabel.setRowCount(0);
        ArrayList<Income> semuaIncome = manager.ambilsemuaincome();

        for (int i = 0; i < semuaIncome.size(); i++) {
            Income inc = semuaIncome.get(i);

            Object[] baris = {
                    (i + 1),
                    inc.getTanggal(),
                    inc.getKategori(),
                    inc.getDeskripsi(),
                    String.format("Rp %,.0f", inc.getJumlah())
            };

            modelTabel.addRow(baris);
        }

        double total = manager.totalIncome();
        labelTotal.setText(String.format("Rp %,.0f", total));
    }

    public IncomeManager getManager() {
        return manager;
    }

    // Untuk back
    private void handleBack() {
        try {
            manager.saveData(currentUsername);
            System.out.println("Data Income berhasil disimpan untuk " + currentUsername);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Gagal menyimpan data!", "Error Simpan", JOptionPane.ERROR_MESSAGE);
        }
        
        if (onCloseCallback != null) {
            onCloseCallback.run();
        }
        
        dispose();
    }
}