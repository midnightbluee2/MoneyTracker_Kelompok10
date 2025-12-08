import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private static final String USER_FILE = "data/userd.dat";
    private static Map<String, String[]> accounts = new HashMap<>();
    private static String currentUser;

    // Load Data saat aplikasi nyala
    static {
        loadUsers();
    }

    @SuppressWarnings("unchecked")
    public static void loadUsers() {
        File folder = new File("data");
        if (!folder.exists()) {
            folder.mkdir();
        }

        File file = new File(USER_FILE);
        if (!file.exists()) {
            System.out.println("No user file found. Starting fresh.");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            accounts = (Map<String, String[]>) ois.readObject();
            System.out.println("Loaded " + accounts.size() + " users from file.");
        } catch (Exception e) {
            System.out.println("Could not load users: " + e.getMessage());
            accounts = new HashMap<>();
        }
    }

    // Simpan Data
    public static void saveUsers() {
        File folder = new File("data");
        if (!folder.exists()) {
            folder.mkdir();
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_FILE))) {
            oos.writeObject(accounts);
            System.out.println("Users saved successfully.");
        } catch (IOException e) {
            System.out.println("Failed to save users: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void login(String username) {
        currentUser = username;
        System.out.println("👤 User logged in: " + username);
    }

    public static String getCurrentUser() {
        return currentUser;
    }

    public static void logout() {
        System.out.println("User logged out: " + currentUser);
        currentUser = null;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }



    // Cek Login
    public static boolean isValidLogin(String email, String password) {
        if (!accounts.containsKey(email)) {
            return false;
        }
        return accounts.get(email)[0].equals(password);
    }

    // Cek Email Terdaftar
    public static boolean isEmailTaken(String email) {
        return accounts.containsKey(email);
    }

    public static boolean accountExists(String email) {
        return isEmailTaken(email);
    }


    // Tambah User Baru
    public static void registerUser(String email, String password, String fullName) {
        accounts.put(email, new String[]{password, fullName});
        saveUsers(); // Langsung simpan permanen
        System.out.println("New user registered: " + email);
    }

    public static void createAccount(String email, String password, String fullName) {
        registerUser(email, password, fullName);
    }

    // Ambil Nama Lengkap
    public static String getFullName(String email) {
        if (accounts.containsKey(email)) {
            return accounts.get(email)[1];
        }
        return null;
    }

    // Update Password
    public static boolean updatePassword(String email, String newPassword) {
        if (accounts.containsKey(email)) {
            accounts.get(email)[0] = newPassword;
            saveUsers();
            System.out.println(" Password updated for: " + email);
            return true;
        }
        return false;
    }


    // Get all accounts 
    public static Map<String, String[]> getAccounts() {
        return accounts;
    }

    // Validasi lengkap untuk login 
    public static boolean validateLogin(String email, String password) {
        return isValidLogin(email, password);
    }

    public static void printAllAccounts() {
        System.out.println("\n=== REGISTERED ACCOUNTS ===");
        if (accounts.isEmpty()) {
            System.out.println("No accounts registered.");
        } else {
            for (Map.Entry<String, String[]> entry : accounts.entrySet()) {
                System.out.println("Email: " + entry.getKey() + 
                                 " | Name: " + entry.getValue()[1]);
            }
        }
        System.out.println("===========================\n");
    }
}