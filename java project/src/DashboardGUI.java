import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;

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
    
    // untuk pie chart dan recent transactions
    private PieChartPanel pieChart;
    private RecentTransactionsPanel recentTransPanel;

    // dipanggil pada summary uang (income, balance, expense)
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

        if (title.equals("Balance")) {
            balanceLabel = forAmount;
        } else if (title.equals("Income")) {
            incomeLabel = forAmount;
        } else if (title.equals("Expense")) {
            expenseLabel = forAmount;
        }

        return summary;
    }

    // button untuk input transaksi
    private JButton forButton(String transaction, Color color) {
        JButton button = new JButton(transaction);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(140, 50));
        button.setMaximumSize(new Dimension(100, 50));
        button.setFocusPainted(false);

        return button;
    }

    // method update summary dan pie chart
    public void updateSummary() {
        String forBalance = BalanceManager.getBalanceFormatted();
        String forIncome = "Rp " + String.format("%,.0f", IncomeManager.getTotalIncome());
        String forExpense = "Rp " + String.format("%,.0f", Expense.getTotalExpense());

        if (balanceLabel != null) balanceLabel.setText(forBalance);
        if (incomeLabel != null) incomeLabel.setText(forIncome);
        if (expenseLabel != null) expenseLabel.setText(forExpense);
        
        // update pie chart
        if (pieChart != null) {
            pieChart.updateDataFromExpenses(Expense.getDaftarExpense());
        }
        
        // update recent transaction
        if (recentTransPanel != null) {
            recentTransPanel.updateTransactions();
        }
        
    }

    // Membuat bentuk rounded di panel
    private JPanel forRoundedPanelGradient(Color colorTop, Color colorBottom){
        JPanel roundPanel = new JPanel(){
        @Override
        protected void paintComponent(Graphics g){
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            java.awt.GradientPaint gradient = new java.awt.GradientPaint(0, 0, colorTop, getWidth(), getHeight(), colorBottom);
            g2.setPaint(gradient);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            g2.dispose();
            }
        };
        roundPanel.setOpaque(false);
        return roundPanel;
    }
    
    // dashboard Constructor - Menerima username
    public DashboardGUI(String username) {
        super(new Color(184, 142, 167), new Color(227, 216, 206));
        this.currentUsername = username;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

        // load data untuk user ini
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
        userGreet.setFont(new Font("SansSerif", Font.BOLD, 14));

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(-5));
        headerPanel.add(userGreet);

        this.add(headerPanel);
        this.add(Box.createVerticalStrut(10));

        // panel pie chart
        JPanel chartPanel = forRoundedPanelGradient(new Color(222, 209, 198), new Color(242, 243, 244));
        chartPanel.setLayout(new BoxLayout(chartPanel, BoxLayout.Y_AXIS));
        chartPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));
        chartPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 220));
        chartPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titleChart = new JLabel("EXPENSES");
        titleChart.setFont(new Font("SansSerif", Font.BOLD, 14));
        titleChart.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleChart.setBorder(new EmptyBorder(5, 0, 0, 0));

        // menampilkan panel pie chart
        pieChart = new PieChartPanel("Expense Categories"); 
        pieChart.setAlignmentX(Component.CENTER_ALIGNMENT);
        pieChart.updateDataFromExpenses(Expense.getDaftarExpense());

        chartPanel.add(titleChart);
        chartPanel.add(pieChart);

        this.add(chartPanel);
        this.add(Box.createVerticalStrut(10));

        // summary (Income, Expense, Balance)
        sumPanel = new JPanel();
        sumPanel.setLayout(new GridLayout(1, 3, 10, 0));
        sumPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        sumPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 50));
        sumPanel.setOpaque(false);
        sumPanel.setBackground(new Color(0, 0, 0));
        sumPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ambil data Balance, Income, Expense
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

        // panel belakang recent transactions
        JPanel transPanel = forRoundedPanelGradient(new Color(179, 199, 116), new Color(179, 199, 116));
        transPanel.setLayout(new BorderLayout());
        transPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));
        transPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 220));
        transPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        

        // tempat tabel recent transactions
        recentTransPanel = new RecentTransactionsPanel(10); 
        recentTransPanel.setBackground(new Color(179, 199, 116));
        recentTransPanel.updateTransactions();
        
        transPanel.add(recentTransPanel, BorderLayout.CENTER);

        this.add(transPanel);
        this.add(Box.createVerticalStrut(10));

        // button pindah page
        JPanel wrapperButton = forRoundedPanelGradient(new Color(140, 119, 163), new Color(207, 160, 179));
        wrapperButton.setLayout(new BoxLayout(wrapperButton, BoxLayout.Y_AXIS));
        wrapperButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        wrapperButton.setPreferredSize(new Dimension(Integer.MAX_VALUE, 100));
        wrapperButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapperButton.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel addTittle = new JLabel("Quick Add");
        addTittle.setFont(new Font("SansSerif", Font.BOLD, 16));
        addTittle.setForeground(Color.WHITE);
        addTittle.setAlignmentX(Component.LEFT_ALIGNMENT);
        addTittle.setBorder(new EmptyBorder(0,10,0,0));

        JPanel transButton = new JPanel();
        transButton.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        transButton.setOpaque(false);
        transButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        transButton.setMaximumSize(new Dimension(Integer.MAX_VALUE,40));

        JButton incButton = forButton("Income", new Color(163, 181, 101));
        JButton expButton = forButton("Expenses", new Color(199, 39, 83));

        incButton.addActionListener(e -> openIncomeGUI());
        expButton.addActionListener(e -> openExpenseGUI());

        transButton.add(incButton);
        transButton.add(expButton);

        wrapperButton.add(addTittle);
        wrapperButton.add(Box.createVerticalStrut(5));
        wrapperButton.add(transButton);

        this.add(wrapperButton);
        this.add(Box.createVerticalStrut(10));

        // button untuk logout
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