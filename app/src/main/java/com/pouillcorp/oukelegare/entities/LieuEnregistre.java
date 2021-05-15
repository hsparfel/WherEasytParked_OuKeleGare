package com.pouillcorp.oukelegare.entities;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class LieuEnregistre implements Comparable<LieuEnregistre> {

    @Id
    private Long id;

    @NotNull
    private String nom;

    private String detail;

    @Generated(hash = 525190206)
    public LieuEnregistre(Long id, @NotNull String nom, String detail) {
        this.id = id;
        this.nom = nom;
        this.detail = detail;
    }

    @Generated(hash = 1378548383)
    public LieuEnregistre() {
    }

    @Override
    public String toString() {
        return nom;
    }

    @Override
    public int compareTo(LieuEnregistre o) {
        return this.nom.compareTo(o.nom);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDetail() {
        return this.detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }


}
