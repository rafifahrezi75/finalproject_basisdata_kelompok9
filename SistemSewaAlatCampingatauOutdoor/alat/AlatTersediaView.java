package alat;

import kategori.Kategori;

public class AlatTersediaView {
    private int idAlat;
    private String namaAlat;
    private Kategori kategori;
    private int stok;
    private int hargaPerHari;

    public AlatTersediaView(int idAlat, String namaAlat, Kategori kategori, int stok, int hargaPerHari) {
        this.idAlat = idAlat;
        this.namaAlat = namaAlat;
        this.kategori = kategori;
        this.stok = stok;
        this.hargaPerHari = hargaPerHari;
    }

    // Getter dan Setter
    public int getIdAlat() { return idAlat; }
    public String getNamaAlat() { return namaAlat; }
    public Kategori getKategori() { return kategori; }
    public int getStok() { return stok; }
    public int getHargaPerHari() { return hargaPerHari; }

    public void setNamaAlat(String namaAlat) { this.namaAlat = namaAlat; }
    public void setKategori(Kategori kategori) { this.kategori = kategori; }
    public void setStok(int stok) { this.stok = stok; }
    public void setHargaPerHari(int hargaPerHari) { this.hargaPerHari = hargaPerHari; }
}
