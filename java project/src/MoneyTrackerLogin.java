import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class MoneyTrackerLogin extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    private JTextField loginEmail, signupEmail, signupName;
    private JPasswordField loginPassword, signupPassword;

    public MoneyTrackerLogin() {
        setTitle("Money Tracker");
        setSize(500, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createLoginPanel(), "login");
        mainPanel.add(createSignupPanel(), "signup");

        add(mainPanel);
        setVisible(true);
    }

    // panel log in
    private JPanel createLoginPanel() {
        Color colorTop = new Color(184, 142, 167);
        Color colorBottom = new Color(227, 216, 206);

        GradientColor panel = new GradientColor(colorTop, colorBottom);
        panel.setLayout(null);

        JPanel card = createCard(100, 80, new Color(69, 83, 120, 230));

        addLabel(card, "Welcome Back!", 28, Font.BOLD, 50, 30);
        addLabel(card, "Login to continue", 14, Font.PLAIN, 50, 65);

        addLabel(card, "Email", 14, Font.PLAIN, 40, 110);
        loginEmail = addTextField(card, 40, 135, new Color(69, 83, 120, 230));

        addLabel(card, "Password", 14, Font.PLAIN, 40, 185);
        loginPassword = addPasswordField(card, 40, 210, new Color(69, 83, 120, 230));

        JLabel forgot = addLabel(card, "Forgot Password?", 12, Font.PLAIN, 40, 250);
        forgot.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgot.setHorizontalAlignment(SwingConstants.CENTER);
        forgot.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                handleForgotPassword();
            }
        });

        JButton loginBtn = createButton("LOGIN", 70, 300, new Color(255, 192, 203));
        loginBtn.setForeground(new Color(69, 83, 120, 230));
        loginBtn.addActionListener(e -> handleLogin());
        card.add(loginBtn);

        JLabel signup = addLabel(card, "CREATE ACCOUNT", 12, Font.BOLD, 40, 370);
        signup.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signup.setHorizontalAlignment(SwingConstants.CENTER);
        signup.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                cardLayout.show(mainPanel, "signup");
            }
        });

        panel.add(card);
        return panel;
    }

    // panel sign up
    private JPanel createSignupPanel() {
        Color colorTop = new Color(227, 216, 206);
        Color colorBottom = new Color(184, 142, 167);

        GradientColor panel = new GradientColor(colorTop, colorBottom);
        panel.setLayout(null);

        JPanel card = createCard(100, 80, new Color(47, 79, 79, 230));

        addLabel(card, "Create Account", 26, Font.BOLD, 50, 30);

        addLabel(card, "Email", 14, Font.PLAIN, 40, 90);
        signupEmail = addTextField(card, 40, 115, new Color(47, 79, 79));

        addLabel(card, "Full Name", 14, Font.PLAIN, 40, 160);
        signupName = addTextField(card, 40, 185, new Color(47, 79, 79));

        addLabel(card, "Password", 14, Font.PLAIN, 40, 230);
        signupPassword = addPasswordField(card, 40, 255, new Color(47, 79, 79));

        JButton createBtn = createButton("CREATE", 70, 315, new Color(255, 192, 203));
        createBtn.setForeground(new Color(47, 79, 79));
        createBtn.addActionListener(e -> handleCreateAccount());
        card.add(createBtn);

        JLabel login = addLabel(card, "LOGIN", 12, Font.BOLD, 40, 375);
        login.setCursor(new Cursor(Cursor.HAND_CURSOR));
        login.setHorizontalAlignment(SwingConstants.CENTER);
        login.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                cardLayout.show(mainPanel, "login");
            }
        });

        panel.add(card);
        return panel;
    }

    // manajemen bagian log in
    private void handleLogin() {
        String email = loginEmail.getText().trim();
        String password = new String(loginPassword.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            showMessage("Please fill in all fields!", JOptionPane.WARNING_MESSAGE);
            return;
        }

    
        if (!UserManager.accountExists(email)) {
            showMessage("Account not found!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!UserManager.validateLogin(email, password)) {
            showMessage("Incorrect password!", JOptionPane.ERROR_MESSAGE);
            loginPassword.setText("");
            return;
        }

        // login berhasil
        String fullName = UserManager.getFullName(email);
        openDashboard(fullName);
    }

    // manajemen bagian create account (membuat akun)
    private void handleCreateAccount() {
        String email = signupEmail.getText().trim();
        String fullName = signupName.getText().trim();
        String password = new String(signupPassword.getPassword());

        if (email.isEmpty() || fullName.isEmpty() || password.isEmpty()) {
            showMessage("Please fill in all fields!", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            showMessage("Please enter a valid email!", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (UserManager.accountExists(email)) {
            showMessage("Email already registered!", JOptionPane.WARNING_MESSAGE);
            return;
        }

        UserManager.createAccount(email, password, fullName);
        showMessage("Account created successfully!", JOptionPane.INFORMATION_MESSAGE);

        signupEmail.setText("");
        signupName.setText("");
        signupPassword.setText("");
        cardLayout.show(mainPanel, "login");
    }

    // manajemen bagian lupa password
    private void handleForgotPassword() {
        String email = JOptionPane.showInputDialog(this, "Enter your email:",
                "Forgot Password", JOptionPane.QUESTION_MESSAGE);

        if (email == null || email.trim().isEmpty())
            return;

        if (!UserManager.accountExists(email.trim())) {
            showMessage("Email not found!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JPasswordField newPass = new JPasswordField();
        JPasswordField confirmPass = new JPasswordField();
        Object[] message = { "New password:", newPass, "Confirm:", confirmPass };

        int option = JOptionPane.showConfirmDialog(this, message, "Reset Password",
                JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String newPassword = new String(newPass.getPassword());
            String confirm = new String(confirmPass.getPassword());

            if (newPassword.isEmpty() || !newPassword.equals(confirm)) {
                showMessage("Passwords don't match or empty!", JOptionPane.ERROR_MESSAGE);
                return;
            }

            
            UserManager.updatePassword(email.trim(), newPassword);
            showMessage("Password reset successful!", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void openDashboard(String fullName) {
        UserManager.login(fullName);

        // Load data untuk user 
        try {
            Expense.loadFromFile(fullName);
            IncomeManager.loadFromFile(fullName);
            System.out.println("Data loaded for: " + fullName);
        } catch (Exception e) {
            System.out.println("Starting with empty data for: " + fullName);
        }

        // tutup login window
        dispose();

        // buka dashboard utama
        SwingUtilities.invokeLater(() -> {
            JFrame dashboardFrame = new JFrame("Money Tracker - Dashboard");
            DashboardGUI dashboard = new DashboardGUI(fullName);

            dashboardFrame.add(dashboard);
            dashboardFrame.setSize(500, 700);
            dashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            dashboardFrame.setLocationRelativeTo(null);
            dashboardFrame.setResizable(false);

            // auto-save data saat tutup aplikasi
            dashboardFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    try {
                        Expense.saveToFile(fullName);
                        IncomeManager.saveToFile(fullName);
                        System.out.println("Data saved for: " + fullName);
                    } catch (Exception ex) {
                        System.err.println("Error saving data: " + ex.getMessage());
                    }
                }
            });

            dashboardFrame.setVisible(true);
        });
    }


    private JPanel createCard(int x, int y, Color bgColor) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(bgColor);
                g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 40, 40));
            }
        };
        card.setOpaque(false);
        card.setBounds(x, y, 300, 420);
        card.setLayout(null);
        return card;
    }

    private JLabel addLabel(JPanel panel, String text, int size, int style, int x, int y) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", style, size));
        label.setForeground(Color.WHITE);
        label.setBounds(x, y, 220, style == Font.BOLD ? 35 : 20);
        panel.add(label);
        return label;
    }

    private JTextField addTextField(JPanel panel, int x, int y, Color bg) {
        JTextField field = new JTextField();
        field.setBounds(x, y, 220, 35);
        field.setBackground(bg);
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE));
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        panel.add(field);
        return field;
    }

    private JPasswordField addPasswordField(JPanel panel, int x, int y, Color bg) {
        JPasswordField field = new JPasswordField();
        field.setBounds(x, y, 220, 35);
        field.setBackground(bg);
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE));
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        panel.add(field);
        return field;
    }

    private JButton createButton(String text, int x, int y, Color bg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getModel().isPressed() ? bg.darker() :
                        getModel().isRollover() ? bg.brighter() : bg);
                g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
                super.paintComponent(g);
            }
        };
        btn.setBounds(x, y, 160, 45);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void showMessage(String message, int type) {
        JOptionPane.showMessageDialog(this, message, "Message", type);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MoneyTrackerLogin());
    }
}