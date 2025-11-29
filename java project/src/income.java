public class income {
    private String Tanggal;
    private String Kategori;
    private String Deskripsi;
    private double Jumlah; 

    income(String Tanggal, String Kategori, String Deskripsi, double Jumlah){
        this.Tanggal = Tanggal;
        this.Kategori = Kategori;
        this.Deskripsi = Deskripsi;
        this.Jumlah = Jumlah;
    }

    public String getTanggal(){
        return Tanggal;
    }
    public String getKategori(){
        return Kategori;
    }
    public String getDeskripsi(){
        return Deskripsi;
    }
    public double getJumlah(){
        return Jumlah;
    }
    public void settanggal(String newtanggal){
        this.Tanggal = newtanggal;
    }
    public void setkategori(String newkategori){
        this.Kategori = newkategori;
    }
    public void setdeskripsi(String newdeskripsi){
        this.Deskripsi = newdeskripsi;
    }
    public void setjumlah(double newjumlah){
        this.Jumlah = newjumlah;
    }
}
