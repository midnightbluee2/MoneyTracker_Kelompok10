import java.util.ArrayList;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class IncomeManager {
    private ArrayList<Income> daftarincomee = new ArrayList<>();

    private static IncomeManager instance = null;

    public IncomeManager() {
    }

    public void addIncome(Income newIncome) {
        daftarincomee.add(newIncome);
    }

    public boolean removeIncome(int index) {
        if (index >= 0 && index < daftarincomee.size()) {
            daftarincomee.remove(index);
            return true;
        }
        return false;
    }
    
    public ArrayList<Income> ambilsemuaincome() {
        return daftarincomee;
    }

    public double totalIncome() {
        double total = 0;
        for (Income inc : daftarincomee) {
            total += inc.getJumlah();
        }
        return total;
    }

    // Method untuk validasi dan penambahan income
    public String tambahIncome(String tanggal, String kategori, String deskripsi, String jumlahStr) {
        try {
            // Validasi tanggal
            if (tanggal == null || tanggal.isEmpty() || tanggal.equals("TANGGAL")) {
                return "Tanggal tidak boleh kosong!";
            }
            
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                LocalDate.parse(tanggal, formatter);
            } catch (DateTimeParseException e) {
                return "Format tanggal salah! Gunakan format dd-MM-yyyy";
            }
            
            // Validasi kategori
            if (kategori == null || kategori.isEmpty() || kategori.equals("KATEGORI")) {
                return "Kategori tidak boleh kosong!";
            }
            
            // Validasi deskripsi
            if (deskripsi == null || deskripsi.isEmpty() || deskripsi.equals("DESKRIPSI")) {
                deskripsi = "-";
            }
            
            // Validasi jumlah
            double jumlah;
            try {
                jumlah = Double.parseDouble(jumlahStr);
                if (jumlah <= 0) {
                    return "Jumlah harus lebih besar dari 0!";
                }
            } catch (NumberFormatException e) {
                return "Jumlah harus berupa angka!";
            }
            
            // Tambahkan income
            Income income = new Income(tanggal, kategori, deskripsi, jumlah);
            addIncome(income);
            return "SUCCESS";
            
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // Save/Load methods
    public void saveData(String username) throws IOException {
        File folder = new File("data");
        if (!folder.exists())
            folder.mkdir();
            
        String fileName = "data/" + username + "_income.dat";
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
        oos.writeObject(daftarincomee);
        oos.close();
    }

    @SuppressWarnings("unchecked")
    public void loadData(String username) throws IOException, ClassNotFoundException {
        String fileName = "data/" + username + "_income.dat";
        File file = new File(fileName);
        
        if (!file.exists()) {
            daftarincomee = new ArrayList<>();
            return;
        }
        
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
        daftarincomee = (ArrayList<Income>) ois.readObject();
        ois.close();
    }

    // add
    public static void setInstance(IncomeManager manager) {
        instance = manager;
    }

    // Get instance yang sedang digunakan
    public static IncomeManager getInstance() {
        if (instance == null) {
            instance = new IncomeManager();
        }
        return instance;
    }

    // Static method untuk get total income
    public static double getTotalIncome() {
        if (instance == null) {
            return 0.0;
        }
        return instance.totalIncome();
    }

    // Static method untuk load dari file
    public static void loadFromFile(String username) throws IOException, ClassNotFoundException {
        if (instance == null) {
            instance = new IncomeManager();
        }
        instance.loadData(username);
    }

    // Static method untuk save ke file
    public static void saveToFile(String username) throws IOException {
        if (instance != null) {
            instance.saveData(username);
        }
    }

}