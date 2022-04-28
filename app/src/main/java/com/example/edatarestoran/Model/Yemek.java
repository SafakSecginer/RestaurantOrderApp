package com.example.edatarestoran.Model;

public class Yemek {

    private String id;
    private String yemekHakkinda;
    private String kategori;
    private String yemekAdi;
    private String yemekFiyati;
    private String yemekResmiUrl;
    private String yemekSahibi;

    public Yemek() {
    }

    public Yemek(String id, String yemekHakkinda, String kategori, String yemekAdi, String yemekFiyati, String yemekResmiUrl, String yemekSahibi) {
        this.id = id;
        this.yemekHakkinda = yemekHakkinda;
        this.kategori = kategori;
        this.yemekAdi = yemekAdi;
        this.yemekFiyati = yemekFiyati;
        this.yemekResmiUrl = yemekResmiUrl;
        this.yemekSahibi = yemekSahibi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getYemekHakkinda() {
        return yemekHakkinda;
    }

    public void setYemekHakkinda(String yemekHakkinda) {
        this.yemekHakkinda = yemekHakkinda;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getYemekAdi() {
        return yemekAdi;
    }

    public void setYemekAdi(String yemekAdi) {
        this.yemekAdi = yemekAdi;
    }

    public String getYemekFiyati() {
        return yemekFiyati;
    }

    public void setYemekFiyati(String yemekFiyati) {
        this.yemekFiyati = yemekFiyati;
    }

    public String getYemekResmiUrl() {
        return yemekResmiUrl;
    }

    public void setYemekResmiUrl(String yemekResmiUrl) {
        this.yemekResmiUrl = yemekResmiUrl;
    }

    public String getYemekSahibi() {
        return yemekSahibi;
    }

    public void setYemekSahibi(String yemekSahibi) {
        this.yemekSahibi = yemekSahibi;
    }
}

