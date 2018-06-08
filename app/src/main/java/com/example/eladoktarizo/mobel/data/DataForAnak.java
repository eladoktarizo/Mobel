package com.example.eladoktarizo.mobel.data;

/**
 * Created by Elad Oktarizo on 01/03/2018.
 */

public class DataForAnak {
    private String id_anak, noinduk_anak, nama_lengkap, ttlahir, umur, namaibu,  iv_anak;

    public DataForAnak() {
    }

    public DataForAnak(String id_anak, String noinduk_anak, String nama_lengkap, String ttlahir, String umur, String namaibu, String iv_anak) {

        this.id_anak = id_anak;
        this.noinduk_anak = noinduk_anak;
        this.nama_lengkap = nama_lengkap;
        this.ttlahir = ttlahir;
        this.umur = umur;
        this.namaibu = namaibu;
        this.iv_anak = iv_anak;
    }

    public String getId_anak() {
        return id_anak;
    }

    public void setId_anak(String id_anak) {
        this.id_anak = id_anak;
    }

    public String getNoinduk_anak() {
        return noinduk_anak;
    }

    public void setNoinduk_anak(String noinduk_anak) {
        this.noinduk_anak = noinduk_anak;
    }

    public String getNama_lengkap() {
        return nama_lengkap;
    }

    public void setNama_lengkap(String nama_lengkap) {
        this.nama_lengkap = nama_lengkap;
    }

    public String getTtlahir() {
        return ttlahir;
    }

    public void setTtlahir(String ttlahir) {
        this.ttlahir = ttlahir;
    }

    public String getUmur() {
        return umur;
    }

    public void setUmur(String umur) {
        this.umur = umur;
    }

    public String getNamaibu() {
        return namaibu;
    }

    public void setNamaibu(String namaibu) {
        this.namaibu = namaibu;
    }

    public String getIv_anak() {
        return iv_anak;
    }

    public void setIv_anak(String iv_anak) {
        this.iv_anak = iv_anak;
    }
}
