package com.example.eladoktarizo.mobel.data;

/**
 * Created by Elad Oktarizo on 23/04/2018.
 */

public class DataForGuru {
    private String id_guru, namalengkap, alamat, pendidikan, ttl_guru, iv_guru;

    public DataForGuru () {
    }

    public DataForGuru (String id_guru, String namalengkap, String alamat, String pendidikan, String ttl_guru, String iv_guru) {

        this.id_guru = id_guru;
        this.namalengkap = namalengkap;
        this.alamat = alamat;
        this.pendidikan = pendidikan;
        this.ttl_guru = ttl_guru;
        this.iv_guru = iv_guru;
    }


    public String getId_guru() {
        return id_guru;
    }

    public void setId_guru(String id_guru) {
        this.id_guru = id_guru;
    }

    public String getNamalengkap() {
        return namalengkap;
    }

    public void setNamalengkap(String namalengkap) {
        this.namalengkap = namalengkap;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getPendidikan() {
        return pendidikan;
    }

    public void setPendidikan(String pendidikan) {
        this.pendidikan = pendidikan;
    }

    public String getIv_guru() {
        return iv_guru;
    }

    public void setIv_guru(String iv_guru) {
        this.iv_guru = iv_guru;
    }

    public String getTtl_guru() {
        return ttl_guru;
    }

    public void setTtl_guru(String ttl_guru) {
        this.ttl_guru = ttl_guru;
    }

}
