package com.example.eladoktarizo.mobel.data;

/**
 * Created by Elad Oktarizo on 12/03/2018.
 */

public class DataForMonitoring {

    private String id_laporan, judul_laporan, isi_laporan;

    public DataForMonitoring() {
    }

    public DataForMonitoring(String id_laporan, String judul_laporan, String isi_laporan) {
        this.id_laporan = id_laporan;
        this.judul_laporan = judul_laporan;
        this.isi_laporan = isi_laporan;
    }

    public String getId_laporan() {
        return id_laporan;
    }

    public void setId_laporan(String id_laporan) {
        this.id_laporan = id_laporan;
    }

    public String getJudul_laporan() {
        return judul_laporan;
    }

    public void setJudul_laporan(String judul_laporan) {
        this.judul_laporan = judul_laporan;
    }

    public String getIsi_laporan() {
        return isi_laporan;
    }

    public void setIsi_laporan(String isi_laporan) {
        this.isi_laporan = isi_laporan;
    }
}
