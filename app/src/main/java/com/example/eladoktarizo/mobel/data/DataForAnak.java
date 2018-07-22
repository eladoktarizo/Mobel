package com.example.eladoktarizo.mobel.data;

/**
 * Created by Elad Oktarizo on 01/03/2018.
 */

public class DataForAnak {
    private String id_anak, noinduk_anak, nama_lengkap, tempat_lahir, tanggal_lahir, namaibu,  iv_anak;

    public DataForAnak() {
    }

    public DataForAnak(String id_anak, String noinduk_anak, String nama_lengkap, String tempat_lahir, String tanggal_lahir, String namaibu, String iv_anak) {

        this.id_anak = id_anak;
        this.noinduk_anak = noinduk_anak;
        this.nama_lengkap = nama_lengkap;
        this.tempat_lahir = tempat_lahir;
        this.tanggal_lahir = tanggal_lahir;
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

    public String getTempat_lahir() {
        return tempat_lahir;
    }

    public void setTempat_lahir(String tempat_lahir) {
        this.tempat_lahir = tempat_lahir;
    }

    public String getTanggal_lahir() {
        return tanggal_lahir;
    }

    public void setTanggal_lahir(String tanggal_lahir) {
        this.tanggal_lahir = tanggal_lahir;
    }

    public String getIv_anak() {
        return iv_anak;
    }

    public void setIv_anak(String iv_anak) {
        this.iv_anak = iv_anak;
    }

    //    public String getUmur() {
//        return umur;
//    }

//    public void setUmur(String umur) {
//        this.umur = umur;
//    }
}
