package org.example.wallet.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "transaksi")
public class Transaksi extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public UUID id;

    @ManyToOne
    public Nasabah nasabah;

    public String transaksi_tipe; // 'top-up', 'transfer', 'pembayaran'
    public Double jumlah;
    public Double saldo_awal;
    public Double saldo_akhir;

    @ManyToOne
    public Nasabah tujuanNasabah;

    @ManyToOne
    public Layanan layanan;
}

