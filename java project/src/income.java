import java.io.*;
import java.util.*;

public class Income implements Serializable {
    public String Tanggal;
    public String Kategori;
    public String Deskripsi;
    public double Jumlah;

    public static List<Income> daftarIncomes = new ArrayList<>();

    // Constructor
    public Income(String Tanggal, String Kategori, String Deskripsi, double Jumlah) {
        this.Tanggal = Tanggal;
        this.Kategori = Kategori;
        this.Deskripsi = Deskripsi;
        this.Jumlah = Jumlah;
    }

    // ========== GETTER METHODS ==========
    public String getTanggal() {
        return this.Tanggal;
    }
    
    public String getKategori() {
        return this.Kategori;
    }
    
    public String getDeskripsi() {
        return this.Deskripsi;
    }
    
    public double getJumlah() {
        return this.Jumlah;
    }
    // ====================================

    // ========== SETTER METHODS ==========
    public void setTanggal(String Tanggal) {
        this.Tanggal = Tanggal;
    }
    
    public void setKategori(String Kategori) {
        this.Kategori = Kategori;
    }
    
    public void setDeskripsi(String Deskripsi) {
        this.Deskripsi = Deskripsi;
    }
    
    public void setJumlah(double Jumlah) {
        this.Jumlah = Jumlah;
    }
    // ====================================

    // Static methods
    public static List<Income> getDaftarIncomes() {
        return daftarIncomes;
    }

    public static void addIncome(Income income) {
        daftarIncomes.add(income);
    }

    public static boolean removeIncome(int index) {
        if (index >= 0 && index < daftarIncomes.size()) {
            daftarIncomes.remove(index);
            return true;
        }
        return false;
    }

    public static double getTotalIncome() {
        double total = 0;
        for (Income income : daftarIncomes) {
            total += income.getJumlah();
        }
        return total;
    }

    // Save/Load methods
    public static void saveToFile(String username) throws IOException {
        File folder = new File("data");
        if (!folder.exists()) folder.mkdir();
        String fileName = "data/" + username + "_income.dat";
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
        oos.writeObject(daftarIncomes);
        oos.close();
    }

    @SuppressWarnings("unchecked")
    public static void loadFromFile(String username) throws IOException, ClassNotFoundException {
        String fileName = "data/" + username + "_income.dat";
        File file = new File(fileName);
        if (!file.exists()) {
            daftarIncomes = new ArrayList<>();
            return;
        }
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
        daftarIncomes = (List<Income>) ois.readObject();
        ois.close();
    }

    @Override
    public String toString() {
        return getTanggal() + " | " + getKategori() + " | " + getDeskripsi() + " | Rp " + 
               String.format("%,.0f", getJumlah());
    }
}