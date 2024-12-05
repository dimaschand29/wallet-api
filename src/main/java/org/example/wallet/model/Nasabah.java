package org.example.wallet.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name = "nasabah")
public class Nasabah extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String nik;
    public String nama_lengkap;
    public String email;
    public String password;
}

