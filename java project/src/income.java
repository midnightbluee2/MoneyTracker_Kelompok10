public class income {
    private String Tanggal;
    private String Kategori;
    private String Deskripsi;
    private String Jumlah; 

    income(String Tanggal, String Kategori, String Deskripsi, String Jumlah){
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
    public String getJumlah(){
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
    public void setjumlah(String newjumlah){
        this.Jumlah = newjumlah;
    }
}
