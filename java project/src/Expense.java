import java.io.*;
import java.util.*;

public class Expense implements Serializable {   // Serializable agar bisa disimpan ke file

    private String tanggal;     // Menyimpan tanggal pengeluaran
    private String kategori;    // Menyimpan kategori
    private String deskripsi;
    private double jumlah;      // Menyimpan nominal uang

    private static final String FILE_NAME = "expense.dat";  //nama file penyimpanan
    private static List<Expense> daftarExpense = new ArrayList<>(); //menyimpan semua data expense

    //Constructor yang dipanggil saat membuat object Expense baru
    public Expense(String tanggal, String kategori, String deskripsi, double jumlah) {
        this.tanggal = tanggal;
        this.kategori = kategori;
        this.deskripsi = deskripsi;
        this.jumlah = jumlah;
    }

    //Getter untuk mengambil seluruh daftar expense
    public static List<Expense> getDaftarExpense() {
        return daftarExpense;
    }

    //menambahkan object expense ke list
    public static void addExpense(Expense expense) {
        daftarExpense.add(expense);
    }

    //menghapus item expense
    public static boolean removeExpense(int index) {
        if (index >= 0 && index < daftarExpense.size()) {
            daftarExpense.remove(index);
            return true;
        }
        return false;
    }

    //Menghitung total semua pengeluaran
    public static double getTotalExpense() {
        return daftarExpense.stream()
                .mapToDouble(e -> e.jumlah)  // Mengambil field jumlah setiap expense
                .sum();                      // Menjumlahkan semuanya
    }

    //menyimpan list expense ke file .dat
    public static void saveToFile() throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME));
        oos.writeObject(daftarExpense);
        oos.close();
    }

    // untuk membaca data dari file saat program dimulai
    public static void loadFromFile() throws IOException, ClassNotFoundException {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;  //jika file belum ada, tidak perlu load

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME));
        daftarExpense = (List<Expense>) ois.readObject();  //untuk mengambil list dari file
        ois.close();
    }

    //Format tampilan data
    @Override
    public String toString() {
        return tanggal + " | " + kategori + " | " + deskripsi + " | Rp " +
                String.format("%,.0f", jumlah);
    }
}