import java.util.ArrayList;

//ini untuk menyimpan data lebih dari satu user
public class IncomeManager {
    private ArrayList<income> daftarincome = new ArrayList<>();

    public IncomeManager(){
        //ArrayList<String> daftarincome = new ArrayList<String>();
    }
    public void tambahincome(income newincome){
        daftarincome.add(newincome);
        }
    public void hapusincome(){
        daftarincome.clear();
    }
    public double totalincome(){
    double total = 0;

    for(income inctotal : daftarincome){
        total += inctotal.getJumlah();
    }

    return total;
    }
}
