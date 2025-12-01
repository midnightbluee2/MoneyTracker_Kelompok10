import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

//ini untuk menyimpan data lebih dari satu user
public class IncomeManager{
    private ArrayList<income> daftarincome = new ArrayList<>();

    public IncomeManager() {
    }

    public void tambahincome(income newincome) {
        daftarincome.add(newincome);
    }

    public void hapusincome(int index) {
        if (index >= 0 && index < daftarincome.size()) {
            daftarincome.remove(index);
        }
    }
    public ArrayList<income> ambilsemuaincome(){
        return daftarincome;
    }

    public double totalincome() {
        double total = 0;

        for (income inctotal : daftarincome) {
            total += inctotal.getJumlah();
        }

        return total;
    }
    public void saveToFile(String filename) {
        try {
            FileOutputStream fileOut = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(daftarincome);
            out.close();
            fileOut.close();
            System.out.println("Data berhasil disimpan ke " + filename);
        } catch (IOException e) {
            System.out.println("Error saat menyimpan file: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    //LOAD DATA DARI FILE
    @SuppressWarnings("unchecked")
    public void loadFromFile(String filename) {
        try {
            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            daftarincome = (ArrayList<income>) in.readObject();
            in.close();
            fileIn.close();
            System.out.println("Data berhasil dimuat dari " + filename);
        } catch (FileNotFoundException e) {
            System.out.println("File tidak ditemukan: " + filename);
        } catch (IOException e) {
            System.out.println("Error saat membaca file: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Class tidak ditemukan: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public boolean fileExists(String filename) {
        File file = new File(filename);
        return file.exists();
    }
}

