import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;


public class MainMoneyTracker extends JFrame {

    public MainMoneyTracker(String username) {
        // Load data untuk user ini
        try {
            Expense.loadFromFile(username);
            IncomeManager.loadFromFile(username);
            System.out.println("Data loaded for: " + username);
        } catch (Exception e) {
            System.out.println("Starting with empty data for: " + username);
        }

        DashboardGUI dashboard = new DashboardGUI(username);

        setTitle("Money Tracker App - " + username);
        setSize(500, 700);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        this.add(dashboard);

        // Auto-save
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                try {
                    Expense.saveToFile(username);
                    IncomeManager.saveToFile(username);
                    System.out.println("Data saved for: " + username);
                } catch (Exception ex) {
                    System.err.println("Error saving data: " + ex.getMessage());
                }
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        UserManager.loadUsers();
        SwingUtilities.invokeLater(() -> {
            String testUsername = JOptionPane.showInputDialog(
                "Input Username (testing)");
            
            if (testUsername != null && !testUsername.trim().isEmpty()) {
                new MainMoneyTracker(testUsername.trim());
            } else {
                System.out.println("Username kosong, program dibatalkan.");
            }
        });
    }
}