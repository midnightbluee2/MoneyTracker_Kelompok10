public class BalanceManager {
    public static double getBalance(){
        double totalIncome = IncomeManager.getTotalIncome();
        double totalExpense = Expense.getTotalExpense();

        // Untuk mendapat balance
        return totalIncome - totalExpense;
    }

    public static String getBalanceFormatted(){
        double balance = getBalance();
        return "Rp " + String.format("%,.0f", balance);
    }

        
}
