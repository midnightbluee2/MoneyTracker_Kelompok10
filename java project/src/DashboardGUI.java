import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class DashboardGUI extends GradientColor {

    private JPanel sumPanel;
    private JLabel balanceLabel;
    private JLabel incomeLabel;
    private JLabel expenseLabel;
    private String currentUsername;
    
    // ✅ TAMBAHAN: Components untuk Chart dan Recent Transactions
    private PieChartPanel pieChart;
    private RecentTransactionsPanel recentTransPanel;

    // Untuk nanti dipanggil pada summary uang (income, balance, expense)
    private JPanel moneySummary(String title, String amount, Color colorTop, Color colorBottom) {
        GradientColor summary = new GradientColor(colorTop, colorBottom);

        summary.setLayout(new BoxLayout(summary, BoxLayout.Y_AXIS));
        summary.setBorder(new EmptyBorder(5, 5, 5, 5));

        JLabel forTitle = new JLabel(title);
        forTitle.setFont(new Font("SansSerif", Font.BOLD, 12));
        forTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel forAmount = new JLabel(amount);
        forAmount.setFont(new Font("SansSerif", Font.PLAIN, 15));
        forAmount.setAlignmentX(Component.CENTER_ALIGNMENT);

        summary.add(forTitle);
        summary.add(Box.createVerticalStrut(1));
        summary.add(forAmount);

        // Simpan reference untuk update nanti
        if (title.equals("Balance")) {
            balanceLabel = forAmount;
        } else if (title.equals("Income")) {
            incomeLabel = forAmount;
        } else if (title.equals("Expense")) {
            expenseLabel = forAmount;
        }

        return summary;
    }

    // Membuat button untuk input transaksi
    private JButton forButton(String transaction, Color color) {
        JButton button = new JButton(transaction);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(180, 50));
        button.setMaximumSize(new Dimension(180, 50));
        button.setFocusPainted(false);

        return button;
    }

    // ✅ UPDATED: Method untuk update summary DAN chart + tabel
    public void updateSummary() {
        String forBalance = BalanceManager.getBalanceFormatted();
        String forIncome = "Rp " + String.format("%,.0f", IncomeManager.getTotalIncome());
        String forExpense = "Rp " + String.format("%,.0f", Expense.getTotalExpense());

        if (balanceLabel != null) balanceLabel.setText(forBalance);
        if (incomeLabel != null) incomeLabel.setText(forIncome);
        if (expenseLabel != null) expenseLabel.setText(forExpense);
        
        // ✅ Update Pie Chart
        if (pieChart != null) {
            pieChart.updateDataFromExpenses(Expense.getDaftarExpense());
        }
        
        // ✅ Update Recent Transactions
        if (recentTransPanel != null) {
            recentTransPanel.updateTransactions();
        }
    }

    // Dashboard Constructor - Menerima username
    public DashboardGUI(String username) {
        super(new Color(184, 142, 167), new Color(227, 216, 206));
        this.currentUsername = username;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Load data untuk user ini
        try {
            Expense.loadFromFile(currentUsername);
            IncomeManager.loadFromFile(currentUsername);
            System.out.println("Data loaded for user: " + currentUsername);
        } catch (Exception e) {
            System.out.println("Starting with empty data for user: " + currentUsername);
        }

        // Title
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titleLabel = new JLabel("MONEY TRACKER");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 25));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleLabel.setForeground(Color.WHITE);

        JLabel userGreet = new JLabel("Hi, " + currentUsername + "!");
        userGreet.setFont(new Font("SansSerif", Font.PLAIN, 14));

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(-5));
        headerPanel.add(userGreet);

        this.add(headerPanel);
        this.add(Box.createVerticalStrut(10));

        // ✅ UPDATED: Pie Chart (Ganti dari placeholder ke actual chart)
        GradientColor chartPanel = new GradientColor(new Color(222, 209, 198), new Color(242, 243, 244));
        chartPanel.setLayout(new BoxLayout(chartPanel, BoxLayout.Y_AXIS));
        chartPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));
        chartPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 220));
        chartPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titleChart = new JLabel("Expenses by Category");
        titleChart.setFont(new Font("SansSerif", Font.BOLD, 14));
        titleChart.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleChart.setBorder(new EmptyBorder(5, 0, 0, 0));

        // ✅ Buat Pie Chart
        pieChart = new PieChartPanel("Expense Categories");
        pieChart.setAlignmentX(Component.CENTER_ALIGNMENT);
        pieChart.updateDataFromExpenses(Expense.getDaftarExpense());

        chartPanel.add(titleChart);
        chartPanel.add(pieChart);

        this.add(chartPanel);
        this.add(Box.createVerticalStrut(10));

        // Total (Income, Expense, Balance)
        sumPanel = new JPanel();
        sumPanel.setLayout(new GridLayout(1, 3, 10, 0));
        sumPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        sumPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 50));
        sumPanel.setOpaque(false);
        sumPanel.setBackground(new Color(0, 0, 0));
        sumPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Ambil data Balance, Income, Expense
        String forBalance = BalanceManager.getBalanceFormatted();
        String forIncome = "Rp " + String.format("%,.0f", IncomeManager.getTotalIncome());
        String forExpense = "Rp " + String.format("%,.0f", Expense.getTotalExpense());

        Color forTop = new Color(140, 119, 163);
        Color forBottom = new Color(207, 160, 179);

        sumPanel.add(moneySummary("Balance", forBalance, forTop, forBottom));
        sumPanel.add(moneySummary("Income", forIncome, forTop, forBottom));
        sumPanel.add(moneySummary("Expense", forExpense, forTop, forBottom));

        this.add(sumPanel);
        this.add(Box.createVerticalStrut(10));

        // ✅ UPDATED: Recent Transactions (Ganti dari placeholder)
        JPanel transPanel = new JPanel();
        transPanel.setLayout(new BorderLayout());
        transPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));
        transPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 220));
        transPanel.setBackground(new Color(179, 199, 116));
        transPanel.setOpaque(true);
        transPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ✅ Buat Recent Transactions Panel
        recentTransPanel = new RecentTransactionsPanel(5); // Tampilkan 5 transaksi terakhir
        recentTransPanel.setBackground(new Color(179, 199, 116));
        recentTransPanel.updateTransactions();
        
        transPanel.add(recentTransPanel, BorderLayout.CENTER);

        this.add(transPanel);
        this.add(Box.createVerticalStrut(10));

        // Button pindah page
        JPanel transButton = new JPanel();
        transButton.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        transButton.setOpaque(false);
        transButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton incButton = forButton("Income", new Color(163, 181, 101));
        JButton expButton = forButton("Expenses", new Color(199, 39, 83));

        incButton.addActionListener(e -> openIncomeGUI());
        expButton.addActionListener(e -> openExpenseGUI());

        transButton.add(incButton);
        transButton.add(expButton);

        this.add(transButton);
        this.add(Box.createVerticalStrut(10));

        // Button untuk logout
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("SansSerif", Font.BOLD, 11));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setBackground(new Color(244, 67, 54));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setPreferredSize(new Dimension(120, 25));
        logoutBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoutBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> handleLogout());

        this.add(logoutBtn);
        this.add(Box.createVerticalGlue());
    }

    private void openIncomeGUI() {
        javax.swing.JFrame dashboardFrame = (javax.swing.JFrame) javax.swing.SwingUtilities.getWindowAncestor(this);
        dashboardFrame.setVisible(false);

        Incomegui_lama incomeGUI = new Incomegui_lama(currentUsername, () -> {
            try {
                IncomeManager.loadFromFile(currentUsername);
            } catch (Exception ex) {
                System.out.println("Error reloading income data: " + ex.getMessage());
            }
            updateSummary();
            dashboardFrame.setVisible(true);
        });
        
        incomeGUI.setVisible(true);
    }

    private void openExpenseGUI() {
        javax.swing.JFrame dashboardFrame = (javax.swing.JFrame) javax.swing.SwingUtilities.getWindowAncestor(this);
        dashboardFrame.setVisible(false);

        ExpenseGUI expenseGUI = new ExpenseGUI(currentUsername, () -> {
            try {
                Expense.loadFromFile(currentUsername);
            } catch (Exception ex) {
                System.out.println("Error reloading expense data: " + ex.getMessage());
            }
            updateSummary();
            dashboardFrame.setVisible(true);
        });
        expenseGUI.setVisible(true);
    }

    public String getCurrentUsername() {
        return currentUsername;
    }

    private void handleLogout() {
        int konfirmasi = javax.swing.JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Logout Confirmation",
                javax.swing.JOptionPane.YES_NO_OPTION);

        if (konfirmasi == javax.swing.JOptionPane.YES_OPTION) {
            try {
                Expense.saveToFile(currentUsername);
                IncomeManager.saveToFile(currentUsername);
                System.out.println("Data saved for: " + currentUsername);
            } catch (Exception e) {
                System.err.println("Error saving data: " + e.getMessage());
            }

            UserManager.logout();
            javax.swing.SwingUtilities.getWindowAncestor(this).dispose();
            javax.swing.SwingUtilities.invokeLater(() -> new MoneyTrackerLogin());
        }
    }
}