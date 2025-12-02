import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class InputExpense {

    // ✅ HAPUS validasi kategori strict - sekarang bebas
    public static String tambahExpense(String tanggal, String kategori,
                                       String deskripsi, String jumlahStr) {

        try {
            if (tanggal == null || tanggal.isEmpty()) {
                throw new Exception("Tanggal tidak boleh kosong!");
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate.parse(tanggal, formatter);

            // ✅ Kategori hanya perlu tidak kosong
            if (kategori == null || kategori.trim().isEmpty()) {
                throw new Exception("Kategori tidak boleh kosong!");
            }

            if (deskripsi == null || deskripsi.trim().isEmpty()) {
                deskripsi = "-";
            }

            double jumlah = Double.parseDouble(jumlahStr);
            if (jumlah <= 0) {
                throw new Exception("Jumlah harus lebih besar dari 0!");
            }

            Expense expense = new Expense(tanggal, kategori, deskripsi, jumlah);
            Expense.addExpense(expense);

            return "SUCCESS";

        } catch (DateTimeParseException e) {
            return "Format tanggal salah!";
        } catch (NumberFormatException e) {
            return "Jumlah harus berupa angka!";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public static boolean isSuccess(String result) {
        return result.equals("SUCCESS");
    }
}