import java.util.ArrayList;
import java.io.*;

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

    // method untuk validasi dan penambahan income
    public String tambahIncome(String tanggal, String kategori, String deskripsi, String jumlahStr) {
        try {
            // validasi kategori
            if (kategori == null || kategori.isEmpty() || kategori.equals("KATEGORI")) {
                return "Category cannot be empty!";
            }
            
            // validasi deskripsi
            if (deskripsi == null || deskripsi.isEmpty() || deskripsi.equals("DESKRIPSI")) {
                deskripsi = "-";
            }
            
            // validasi jumlah
            double jumlah;
            try {
                jumlah = Double.parseDouble(jumlahStr);
                if (jumlah <= 0) {
                    return "Amount must be greater than 0!";
                }
            } catch (NumberFormatException e) {
                return "Amount must be a number!";
            }
            
            // tambahkan income
            Income income = new Income(tanggal, kategori, deskripsi, jumlah);
            addIncome(income);
            return "SUCCESS";
            
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // save/load methods
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

    // sedang digunakan
    public static IncomeManager getInstance() {
        if (instance == null) {
            instance = new IncomeManager();
        }
        return instance;
    }

    // untuk get total income
    public static double getTotalIncome() {
        if (instance == null) {
            return 0.0;
        }
        return instance.totalIncome();
    }

    // untuk load dari file
    public static void loadFromFile(String username) throws IOException, ClassNotFoundException {
        if (instance == null) {
            instance = new IncomeManager();
        }
        instance.loadData(username);
    }

    // untuk save ke file
    public static void saveToFile(String username) throws IOException {
        if (instance != null) {
            instance.saveData(username);
        }
    }

}