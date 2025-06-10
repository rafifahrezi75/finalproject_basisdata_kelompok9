# 🏕️ Sistem Informasi Penyewaan Alat Camping & Outdoor

Sistem informasi ini dirancang untuk mengelola proses penyewaan alat-alat camping dan outdoor secara digital, mulai dari pencatatan alat, pengguna, transaksi penyewaan, hingga pengembalian dan denda.

---

## 🔑 Fitur Utama

- **Manajemen Data Alat & Kategori**  
  Menyimpan informasi alat camping beserta kategorinya.

- **Penyewaan & Detail Transaksi**  
  Mencatat siapa yang menyewa, tanggal sewa, dan jumlah alat yang dipinjam.

- **Pengembalian & Denda Otomatis**  
  Sistem menghitung denda otomatis jika alat dikembalikan terlambat.

- **Tagihan Otomatis**  
  Menyediakan perhitungan total tagihan (biaya sewa + denda) untuk tiap penyewaan.

---

## 👥 Biodata Kelompok 9

| Nama Lengkap                | NIM           |
|-----------------------------|---------------|
| Muhammad Rafi Fahrezi       | 24082010099   |
| Nanda Risky Firmandany      | 24082010098   |
| Muhammad Izza Al Farizi     | 24082010094   |
| Fahmi Zaki Ahmad            | 24082010121   |

---

## 🧾 Struktur Tabel (Ringkasan Non-SQL)

| Tabel            | Kolom                                                                 |
|------------------|-----------------------------------------------------------------------|
| `kategori`       | `id_kategori`, `nama_kategori`                                        |
| `alat`           | `id_alat`, `id_kategori`, `nama_alat`, `stok`, `harga_perhari`        |
| `user`           | `id_user`, `role`, `nama`, `password`, `no_hp`, `alamat`              |
| `penyewaan`      | `id_penyewaan`, `id_user`, `tgl_sewa`, `tgl_rencana_menyewa`, `tgl_kembali`, `barang_jaminan`, `status` |
| `detail_penyewaan`| `id_detail_penyewaan`, `id_penyewaan`, `id_alat`, `jumlah_barang`    |
| `pengembalian`   | `id_pengembalian`, `id_penyewaan`, `tgl_dikembalikan`, `status_bayar`, `total_denda` |
| `denda`          | `id_denda`, `id_pengembalian`, `nilai`, `keterangan`                  |

## 📑 Struktur SQL Lengkap

```sql
CREATE DATABASE IF NOT EXISTS sewaalat;
USE sewaalat;

CREATE TABLE kategori (
    id_kategori INT AUTO_INCREMENT PRIMARY KEY,
    nama_kategori VARCHAR(100) NOT NULL
);

CREATE TABLE alat (
    id_alat INT AUTO_INCREMENT PRIMARY KEY,
    id_kategori INT,
    nama_alat VARCHAR(100) NOT NULL,
    stok INT DEFAULT 0,
    harga_perhari DECIMAL(10,2),
    FOREIGN KEY (id_kategori) REFERENCES kategori(id_kategori)
);

CREATE TABLE user (
    id_user INT AUTO_INCREMENT PRIMARY KEY,
    role VARCHAR(40),
    nama VARCHAR(100),
    password VARCHAR(100),
    no_hp VARCHAR(20),
    alamat TEXT
);

CREATE TABLE penyewaan (
    id_penyewaan INT AUTO_INCREMENT PRIMARY KEY,
    id_user INT,
    tgl_sewa TIMESTAMP,
    tgl_rencana_menyewa TIMESTAMP,
    tgl_kembali TIMESTAMP,
    barang_jaminan VARCHAR(50),
    status VARCHAR(20),
    FOREIGN KEY (id_user) REFERENCES user(id_user)
);

CREATE TABLE detail_penyewaan (
    id_detail_penyewaan INT AUTO_INCREMENT PRIMARY KEY,
    id_penyewaan INT,
    id_alat INT,
    jumlah_barang INT,
    FOREIGN KEY (id_penyewaan) REFERENCES penyewaan(id_penyewaan),
    FOREIGN KEY (id_alat) REFERENCES alat(id_alat)
);

CREATE TABLE pengembalian (
    id_pengembalian INT AUTO_INCREMENT PRIMARY KEY,
    id_penyewaan INT,
    tgl_dikembalikan TIMESTAMP,
    status_bayar VARCHAR(15),
    total_denda DECIMAL(10,2),
    FOREIGN KEY (id_penyewaan) REFERENCES penyewaan(id_penyewaan)
);

CREATE TABLE denda (
    id_denda INT AUTO_INCREMENT PRIMARY KEY,
    id_pengembalian INT,
    nilai DECIMAL(10,2),
    keterangan VARCHAR(255),
    FOREIGN KEY (id_pengembalian) REFERENCES pengembalian(id_pengembalian)
);
```

---

## ⚙️ Trigger Otomatis

### 1. Update stok saat pengembalian alat

```sql
DELIMITER $$

CREATE TRIGGER trg_update_stok_pengembalian
AFTER INSERT ON pengembalian
FOR EACH ROW
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE v_id_alat INT;
    DECLARE v_jumlah_barang INT;

    DECLARE cur CURSOR FOR
        SELECT id_alat, jumlah_barang
        FROM detail_penyewaan
        WHERE id_penyewaan = NEW.id_penyewaan;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    OPEN cur;

    read_loop: LOOP
        FETCH cur INTO v_id_alat, v_jumlah_barang;
        IF done THEN
            LEAVE read_loop;
        END IF;

        UPDATE alat
        SET stok = stok + v_jumlah_barang
        WHERE id_alat = v_id_alat;
    END LOOP;

    CLOSE cur;
END$$

DELIMITER ;
```

---

### 2. Kurangi stok setelah alat disewa

```sql
DELIMITER //

CREATE TRIGGER kurangi_stok_setelah_sewa
AFTER INSERT ON detail_penyewaan
FOR EACH ROW
BEGIN
    UPDATE alat
    SET stok = stok - NEW.jumlah_barang
    WHERE id_alat = NEW.id_alat;
END;
//

DELIMITER ;
```

---

### 3. Tambah denda otomatis jika terlambat

```sql
DELIMITER $$

CREATE TRIGGER trg_insert_denda_otomatis
AFTER INSERT ON pengembalian
FOR EACH ROW
BEGIN
    DECLARE jumlah_barang_terlambat INT DEFAULT 0;

    IF NEW.tgl_dikembalikan > (SELECT tgl_kembali FROM penyewaan WHERE id_penyewaan = NEW.id_penyewaan) THEN
        SELECT SUM(jumlah_barang)
        INTO jumlah_barang_terlambat
        FROM detail_penyewaan
        WHERE id_penyewaan = NEW.id_penyewaan;

        INSERT INTO denda (id_pengembalian, nilai, keterangan)
        VALUES (
            NEW.id_pengembalian,
            jumlah_barang_terlambat * 5000,
            CONCAT('Terlambat kembalikan ', jumlah_barang_terlambat, ' barang')
        );
    END IF;
END$$

DELIMITER ;
```

---

## 📊 Stored Procedure: Hitung Total Sewa + Denda

```sql
DROP PROCEDURE IF EXISTS hitung_total_sewa_denda;
DELIMITER //

CREATE PROCEDURE hitung_total_sewa_denda(
    IN p_id_penyewaan INT
)
BEGIN
    DECLARE total_sewa DECIMAL(10,2);
    DECLARE total_denda DECIMAL(10,2);
    DECLARE lama_hari INT;

    SELECT 
        DATEDIFF(pw.tgl_kembali, pw.tgl_rencana_menyewa)
    INTO lama_hari
    FROM penyewaan pw
    LEFT JOIN pengembalian pg ON pw.id_penyewaan = pg.id_penyewaan
    WHERE pw.id_penyewaan = p_id_penyewaan;

    IF lama_hari < 1 THEN
        SET lama_hari = 1;
    END IF;

    SELECT 
        IFNULL(SUM(dp.jumlah_barang * a.harga_perhari * lama_hari), 0)
    INTO total_sewa
    FROM detail_penyewaan dp
    JOIN alat a ON dp.id_alat = a.id_alat
    WHERE dp.id_penyewaan = p_id_penyewaan;

    SELECT 
        IFNULL(SUM(d.nilai), 0)
    INTO total_denda
    FROM pengembalian pg
    JOIN denda d ON pg.id_pengembalian = d.id_pengembalian
    WHERE pg.id_penyewaan = p_id_penyewaan;

    SELECT 
        p_id_penyewaan AS id_penyewaan,
        lama_hari AS lama_sewa_hari,
        total_sewa AS total_harga_sewa,
        total_denda AS total_denda,
        (total_sewa + total_denda) AS total_tagihan;
END //

DELIMITER ;
```

---

## 📌 Catatan

- Sistem denda menggunakan tarif **Rp5.000 per barang** jika dikembalikan terlambat.
- Trigger memastikan stok barang selalu up-to-date.
- Stored Procedure dapat digunakan oleh aplikasi backend untuk menampilkan total tagihan penyewaan.

---

## 🚀 Cara Menjalankan Program Java

### 🧱 Step 1: Compile Semua File Java
Pastikan Anda berada di direktori project, lalu jalankan perintah berikut:

```bash
javac -d bin -cp "lib/mysql-connector-j-9.3.0.jar" -sourcepath . \
  user/*.java connection/*.java kategori/*.java alat/*.java \
  penyewaan/*.java detail_penyewaan/*.java tagihan_penyewaan/*.java \
  pengembalian/*.java Main.java
```

### ▶️ Step 2: Jalankan Program
Gunakan perintah berikut untuk menjalankan program:

```bash
java -cp "bin;lib/mysql-connector-j-9.3.0.jar" Main
```
