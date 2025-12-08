import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class InputExpense {

    public static String tambahExpense(String tanggal, String kategori,
                                       String deskripsi, String jumlahStr) {

        try {
            if (tanggal == null || tanggal.isEmpty()) {
                throw new Exception("Date cannot be empty!");
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate.parse(tanggal, formatter);

            if (kategori == null || kategori.trim().isEmpty()) {
                throw new Exception("Category cannot be empty!");
            }

            if (deskripsi == null || deskripsi.trim().isEmpty()) {
                deskripsi = "-";
            }

            double jumlah = Double.parseDouble(jumlahStr);
            if (jumlah <= 0) {
                throw new Exception("Amount must be greater than 0!");
            }

            Expense expense = new Expense(tanggal, kategori, deskripsi, jumlah);
            Expense.addExpense(expense);

            return "SUCCESS";

        } catch (DateTimeParseException e) {
            return "Invalid date format!";
        } catch (NumberFormatException e) {
            return "Amount must be a number!";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public static boolean isSuccess(String result) {
        return result.equals("SUCCESS");
    }
}
