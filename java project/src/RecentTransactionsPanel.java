import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Panel untuk menampilkan transaksi terbaru (Income + Expense)
 * VERSI INTEGRASI: Mengambil data langsung dari class Income dan Expense.
 */
public class RecentTransactionsPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private int maxTransactions;

    // --- INNER CLASS TRANSACTION ---
    // Class pembungkus sederhana untuk menyamakan format data Income & Expense
    private static class Transaction {
        String tanggal;
        String tipe; // "Income" atau "Expense"
        String kategori;
        double jumlah;

        public Transaction(String tanggal, String tipe, String kategori, double jumlah) {
            this.tanggal = tanggal;
            this.tipe = tipe;
            this.kategori = kategori;
            this.jumlah = jumlah;
        }
    }

    public RecentTransactionsPanel(int maxTransactions) {
        this.maxTransactions = maxTransactions;
        setupPanel();
    }

    private void setupPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);

        // Title
        JLabel titleLabel = new JLabel("Recent Transactions");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(titleLabel, BorderLayout.NORTH);

        // Table Setup
        String[] columnNames = { "Date", "Type", "Category", "Amount" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setFont(new Font("SansSerif", Font.PLAIN, 11));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 11));
        table.getTableHeader().setBackground(new Color(163, 181, 101));
        table.getTableHeader().setForeground(Color.WHITE);

        // Custom Cell Renderer untuk warna teks (Hijau untuk Income, Merah untuk
        // Expense)
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (!isSelected) {
                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.BLACK);

                    // Cek kolom "Type" (index 1)
                    String tipe = (String) table.getValueAt(row, 1);
                    if ("Income".equals(tipe)) {
                        c.setForeground(new Color(76, 175, 80)); // Hijau
                    } else if ("Expense".equals(tipe)) {
                        c.setForeground(new Color(244, 67, 54)); // Merah
                    }
                }

                // Alignment text
                if (column == 3) { // Kolom Amount rata kanan
                    ((JLabel) c).setHorizontalAlignment(SwingConstants.RIGHT);
                } else {
                    ((JLabel) c).setHorizontalAlignment(SwingConstants.LEFT);
                }

                return c;
            }
        });

        // Atur lebar kolom
        table.getColumnModel().getColumn(0).setPreferredWidth(80); // Date
        table.getColumnModel().getColumn(1).setPreferredWidth(60); // Type
        table.getColumnModel().getColumn(2).setPreferredWidth(100); // Category
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // Amount

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Dipanggil untuk me-refresh isi tabel
     */
    public void updateTransactions() {
        tableModel.setRowCount(0); // Hapus data lama

        // Ambil data gabungan dari Income & Expense
        List<Transaction> transactions = getCombinedTransactions(maxTransactions);

        if (transactions.isEmpty()) {
            Object[] emptyRow = { "No transactions yet", "", "", "" };
            tableModel.addRow(emptyRow);
            return;
        }

        for (Transaction trans : transactions) {
            Object[] row = {
                    trans.tanggal,
                    trans.tipe,
                    trans.kategori,
                    String.format("Rp %,.0f", trans.jumlah)
            };
            tableModel.addRow(row);
        }
    }

    // --- LOGIKA UTAMA: Mengambil dan Menggabungkan Data ---
    private List<Transaction> getCombinedTransactions(int limit) {
        List<Transaction> allTransactions = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // 1. Ambil data dari EXPENSE (Sesuai file Expense.java kamu)
        List<Expense> expenses = Expense.getDaftarExpense();
        if (expenses != null) {
            for (Expense exp : expenses) {
                allTransactions.add(new Transaction(
                        exp.getTanggal(),
                        "Expense",
                        exp.getKategori(),
                        exp.getJumlah()));
            }
        }

        // 2. Ambil data dari INCOME (Sesuai file Income.java kamu)
        // Langsung akses static list dari class Income
        IncomeManager manager = IncomeManager.getInstance();
        if (manager != null) {
            ArrayList<Income> incomes = manager.ambilsemuaincome();
            if (incomes != null) {
                for (Income inc : incomes) {
                    allTransactions.add(new Transaction(
                            inc.getTanggal(),
                            "Income",
                            inc.getKategori(),
                            inc.getJumlah()
                    ));
                }
            }
        }

        // 3. Sorting data berdasarkan Tanggal (Terbaru -> Terlama)
        Collections.sort(allTransactions, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction t1, Transaction t2) {
                try {
                    LocalDate d1 = LocalDate.parse(t1.tanggal, formatter);
                    LocalDate d2 = LocalDate.parse(t2.tanggal, formatter);
                    return d2.compareTo(d1); // Descending (t2 banding t1)
                } catch (Exception e) {
                    return 0; // Abaikan jika format tanggal error
                }
            }
        });

        // 4. Batasi jumlah data (Limit) sesuai parameter
        if (allTransactions.size() > limit) {
            return allTransactions.subList(0, limit);
        }

        return allTransactions;
    }
}