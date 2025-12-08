import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ExpenseGUI extends JFrame {
    private JSpinner dateSpinner; 
    private JComboBox<String> cmbKategori; 
    private JTextField txtKategoriLain; 
    private JTextField txtDeskripsi;
    private JTextField txtJumlah;
    private JButton btnTambah;
    private JButton btnHapus;
    private JButton btnClear;
    private JButton btnBack; 
    private JTable tabelExpense;
    private DefaultTableModel modelTabel;
    private JLabel labelTotal;

    private String currentUsername;
    private Runnable onCloseCallback; 
    
    private Color primaryRed = new Color(244, 67, 54);
    private Color darkGray = new Color(120, 120, 120);
    private Color lightGray = new Color(240, 240, 240);

    // constructor dengan callback
    public ExpenseGUI(String username, Runnable onCloseCallback) {
        this.currentUsername = username;
        this.onCloseCallback = onCloseCallback;
        
        try {
            Expense.loadFromFile(currentUsername);
            System.out.println("Expense data for " + currentUsername + " loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Expense data file not found, starting with empty data.");
        }
        
        buatTampilan();
        
        setTitle("EXPENSE");
        setSize(500, 700);  
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); 
        setLocationRelativeTo(null);
        setResizable(false);
        
        
        setContentPane(new GradientColor(new Color(255, 245, 245), new Color(255, 224, 224)));
        buatTampilan(); 
        
        updateTabel();
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleBack(); // tombol back
            }
        });
    }

    private void buatTampilan() {
        setLayout(new BorderLayout(20, 20));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setOpaque(false); 
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Icon Panel
        JPanel iconPanel = new JPanel();
        iconPanel.setOpaque(false);
        iconPanel.setLayout(new BoxLayout(iconPanel, BoxLayout.Y_AXIS));

        JLabel iconExpense = new JLabel("💸", SwingConstants.CENTER);
        iconExpense.setFont(new Font("SansSerif", Font.PLAIN, 80));
        iconExpense.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitle = new JLabel("EXPENSE", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitle.setForeground(primaryRed);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblUsername = new JLabel("Hi, " + currentUsername + "!", SwingConstants.CENTER);
        lblUsername.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblUsername.setAlignmentX(Component.CENTER_ALIGNMENT);

        iconPanel.add(Box.createVerticalGlue());
        iconPanel.add(iconExpense);
        iconPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        iconPanel.add(lblTitle);
        iconPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        iconPanel.add(lblUsername);
        iconPanel.add(Box.createVerticalGlue());

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 6; // kategori lain
        gbc.weightx = 0.2;
        mainPanel.add(iconPanel, gbc);

        gbc.gridheight = 1;
        gbc.weightx = 0.8;
        gbc.gridx = 1;

        // header 
        gbc.gridy = 0;
        JLabel lblHeader = new JLabel("EXPENSE");
        lblHeader.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblHeader.setForeground(Color.WHITE);
        lblHeader.setBackground(new Color(244, 67, 54));
        lblHeader.setOpaque(true);
        lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
        lblHeader.setPreferredSize(new Dimension(300, 45));
        lblHeader.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        makeRounded(lblHeader, 25);
        mainPanel.add(lblHeader, gbc);

        // data spinner (agar tidak tulis manual)
        gbc.gridy = 1;
        dateSpinner = createDateSpinner();
        mainPanel.add(dateSpinner, gbc);

        // dropdown kategori
        gbc.gridy = 2;
        cmbKategori = createKategoriComboBox();
        mainPanel.add(cmbKategori, gbc);

        // kategori lain
        gbc.gridy = 3;
        txtKategoriLain = createRoundedTextField("Type another category...");
        txtKategoriLain.setVisible(false);
        mainPanel.add(txtKategoriLain, gbc);

        gbc.gridy = 4;
        txtDeskripsi = createRoundedTextField("DESCRIPTION (Optional)");
        mainPanel.add(txtDeskripsi, gbc);

        gbc.gridy = 5;
        txtJumlah = createRoundedTextField("AMOUNT (Number without comma)");
        mainPanel.add(txtJumlah, gbc);

        add(mainPanel, BorderLayout.NORTH);

        // bottom Panel
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 30, 30));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnPanel.setOpaque(false);

        btnTambah = new JButton("Add Data");
        btnTambah.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnTambah.setForeground(Color.WHITE);
        btnTambah.setBackground(primaryRed);
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
        btnHapus.setBackground(new Color(33, 33, 33));
        btnHapus.setFocusPainted(false);
        btnHapus.setBorderPainted(false);
        btnHapus.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // button Back
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

        String[] namaKolom = { "No", "Date", "Category", "Description", "Amount" };
        modelTabel = new DefaultTableModel(namaKolom, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelExpense = new JTable(modelTabel);
        tabelExpense.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabelExpense.setRowHeight(30);
        tabelExpense.setSelectionBackground(new Color(255, 205, 210));
        tabelExpense.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        tabelExpense.getTableHeader().setBackground(primaryRed);
        tabelExpense.getTableHeader().setForeground(Color.WHITE);
        tabelExpense.getTableHeader().setPreferredSize(new Dimension(0, 35));

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

        for (int i = 0; i < tabelExpense.getColumnCount(); i++) {
            tabelExpense.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
        
        tabelExpense.getColumnModel().getColumn(0).setPreferredWidth(30);
        tabelExpense.getColumnModel().getColumn(4).setPreferredWidth(120);

        JScrollPane scrollPane = new JScrollPane(tabelExpense);
        scrollPane.setPreferredSize(new Dimension(700, 250));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.setOpaque(false);

        JLabel lblTotalText = new JLabel("Total Expenses: ");
        lblTotalText.setFont(new Font("SansSerif", Font.BOLD, 16));

        labelTotal = new JLabel("Rp 0");
        labelTotal.setFont(new Font("SansSerif", Font.BOLD, 18));
        labelTotal.setForeground(primaryRed);

        totalPanel.add(lblTotalText);
        totalPanel.add(labelTotal);

        bottomPanel.add(btnPanel, BorderLayout.NORTH);
        bottomPanel.add(scrollPane, BorderLayout.CENTER);
        bottomPanel.add(totalPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.CENTER);

        // action Listeners untuk button
        btnTambah.addActionListener(e -> tambahData());
        btnHapus.addActionListener(e -> hapusData());
        btnClear.addActionListener(e -> clearForm());
        btnBack.addActionListener(e -> handleBack()); 
    }

    // untuk tanggal (data spinner)
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

    // kategori
    private JComboBox<String> createKategoriComboBox() {
        String[] kategori = {
            "Food & Beverage",
            "Transportation",
            "Health",
            "Education",
            "Beauty",
            "Others"
        };
        
        JComboBox<String> combo = new JComboBox<>(kategori);
        combo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        combo.setPreferredSize(new Dimension(400, 45));
        combo.setBackground(lightGray);
        
        // untuk kategori lain lain
        combo.addActionListener(e -> {
            if (combo.getSelectedItem().equals("Others")) {
                txtKategoriLain.setVisible(true);
            } else {
                txtKategoriLain.setVisible(false);
                txtKategoriLain.setText("Type another category...");
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

        txt.setFont(new Font("SansSerif", Font.PLAIN, 12));
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
        // ambil tanggal dari spinner
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String tanggal = sdf.format((Date) dateSpinner.getValue());
        
        // kategori
        String kategori = (String) cmbKategori.getSelectedItem();
        if (kategori.equals("Others")) {
            kategori = txtKategoriLain.getText().trim();
            if (kategori.isEmpty() || kategori.equals("Type another category...")) {
                JOptionPane.showMessageDialog(this,
                    "'Others' category must be filled!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        String deskripsi = txtDeskripsi.getText().trim();
        String jumlahText = txtJumlah.getText().trim();

        if (deskripsi.equals("DESCRIPTION (Optional)")) deskripsi = "";
        if (jumlahText.equals("AMOUNT (Number without comma)")) jumlahText = "";
        
        String result = InputExpense.tambahExpense(tanggal, kategori, deskripsi, jumlahText);

        if (InputExpense.isSuccess(result)) {
            updateTabel();
            clearForm();

            JOptionPane.showMessageDialog(this,
                "Data added successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
             JOptionPane.showMessageDialog(this,
                "Error: " + result,
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapusData() {
        int barisTerpilih = tabelExpense.getSelectedRow();

        if (barisTerpilih == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select the data you want to delete!",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int konfirmasi = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this data?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (konfirmasi == JOptionPane.YES_OPTION) {
            boolean success = Expense.removeExpense(barisTerpilih);

            if (success) {
                updateTabel();
                JOptionPane.showMessageDialog(this,
                    "Data deleted successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to delete data!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        dateSpinner.setValue(new Date());
        cmbKategori.setSelectedIndex(0);
        txtKategoriLain.setVisible(false);
        txtDeskripsi.setText("DESCRIPTION (Optional)");
        txtDeskripsi.setForeground(Color.GRAY);
        txtJumlah.setText("AMOUNT (Number without comma)");
        txtJumlah.setForeground(Color.GRAY);
    }

    private void updateTabel() {
        modelTabel.setRowCount(0);
        
        List<Expense> semuaExpense = Expense.getDaftarExpense();

        for (int i = 0; i < semuaExpense.size(); i++) {
            Expense exp = semuaExpense.get(i);

            Object[] baris = {
                    (i + 1),
                    exp.getTanggal(),
                    exp.getKategori(),
                    exp.getDeskripsi(),
                    String.format("Rp %,.0f", exp.getJumlah())
            };

            modelTabel.addRow(baris);
        }

        double total = Expense.getTotalExpense();
        labelTotal.setText(String.format("Rp %,.0f", total));
    }

    // handle tombol back
    private void handleBack() {
        try {
            Expense.saveToFile(currentUsername);
            System.out.println("Expense data saved successfully for " + currentUsername);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, 
                "Failed to save data!", "Save Error", JOptionPane.ERROR_MESSAGE);
        }
        
        // panggil callback untuk kembali ke dashboard
        if (onCloseCallback != null) {
            onCloseCallback.run();
        }
        
        dispose(); 
    }
}