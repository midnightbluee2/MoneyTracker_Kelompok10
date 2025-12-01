import java.util.ArrayList;
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
}
