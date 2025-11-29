import java.time.LocalDate;                   // Untuk validasi tanggal
import java.time.format.DateTimeFormatter;    // Format tanggal
import java.time.format.DateTimeParseException;
import java.util.List;

public class InputExpense {

    //daftar kategori
    private static final List<String> kategoriValid = List.of(
            "Makanan", "Transportasi", "Hiburan", "Belanja", "Tagihan", "Lainnya"
    );

    //untuk menambah expense
    public static String tambahExpense(String tanggal, String kategori,
                                       String deskripsi, String jumlahStr) {

        try {
            //tanggal tidak boleh kosong
            if (tanggal == null || tanggal.isEmpty()) {
                throw new Exception("Tanggal tidak boleh kosong!");
            }

            //tanggal harus format dd-MM-yyyy
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate.parse(tanggal, formatter);

            //kategori harus sesuai dengan list yang ada
            if (!kategoriValid.contains(kategori)) {
                throw new Exception("Kategori tidak valid!");
            }

            //ketika tidak ingin memasukkan deskripsi bisa input "-"
            if (deskripsi == null || deskripsi.trim().isEmpty()) {
                deskripsi = "-";
            }

            //untuk validasi jumlah harus berupa angka
            double jumlah = Double.parseDouble(jumlahStr);
            if (jumlah <= 0) {
                throw new Exception("Jumlah harus lebih besar dari 0!");
            }

            // Membuat object expense baru
            Expense expense = new Expense(tanggal, kategori, deskripsi, jumlah);
            Expense.addExpense(expense); // menyimpan ke list
            return "SUCCESS"; //memberi pesan succes

        } catch (DateTimeParseException e) {
            return "Format tanggal salah!";
        } catch (NumberFormatException e) {
            return "Jumlah harus berupa angka!";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    //Fungsi untuk cek apakah input berhasil ditambah
    public static boolean isSuccess(String result) {
        return result.equals("SUCCESS");
    }
}