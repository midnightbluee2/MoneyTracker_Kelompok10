import java.io.*;
import java.util.*;

public class Expense implements Serializable {

    private String tanggal;
    private String kategori;
    private String deskripsi;
    private double jumlah;

    private static List<Expense> daftarExpense = new ArrayList<>();

    public Expense(String tanggal, String kategori, String deskripsi, double jumlah) {
        this.tanggal = tanggal;
        this.kategori = kategori;
        this.deskripsi = deskripsi;
        this.jumlah = jumlah;
    }

    // Getter list
    public static List<Expense> getDaftarExpense() {
        return daftarExpense;
    }

    // Tambah data
    public static void addExpense(Expense expense) {
        daftarExpense.add(expense);
    }

    // Hapus data
    public static boolean removeExpense(int index) {
        if (index >= 0 && index < daftarExpense.size()) {
            daftarExpense.remove(index);
            return true;
        }
        return false;
    }

    // Total pengeluaran
    public static double getTotalExpense() {
        return daftarExpense.stream()
                .mapToDouble(e -> e.jumlah)
                .sum();
    }
    public static void saveToFile(String username) throws IOException {
        File folder = new File("data");
        if (!folder.exists()) folder.mkdir();

        String fileName = "data/" + username + "_expense.dat";

        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
        oos.writeObject(daftarExpense);
        oos.close();
    }

    public static void loadFromFile(String username)
            throws IOException, ClassNotFoundException {

        String fileName = "data/" + username + "_expense.dat";

        File file = new File(fileName);
        if (!file.exists()) {
            daftarExpense = new ArrayList<>();
            return;
        }

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
        daftarExpense = (List<Expense>) ois.readObject();
        ois.close();
    }

    @Override
    public String toString() {
        return tanggal + " | " + kategori + " | " + deskripsi + " | Rp " +
                String.format("%,.0f", jumlah);
    }

    // ======= Getter methods for Expense (used by ExpenseGUI and elsewhere) =======
    public String getTanggal() {
        return tanggal;
    }

    public String getKategori() {
        return kategori;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public double getJumlah() {
        return jumlah;
    }
    // ======= End getters =======
}
