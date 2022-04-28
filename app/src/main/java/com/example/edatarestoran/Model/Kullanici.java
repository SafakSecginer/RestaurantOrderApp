package com.example.edatarestoran.Model;

public class Kullanici {

    private String id;
    private String kullaniciTipi;
    private String kullaniciAdi;
    private String ad;
    private String resimUrl;
    private String restoranId;

    public Kullanici() {
    }

    public Kullanici(String id, String kullaniciTipi, String kullaniciAdi, String ad, String resimUrl, String restoranId) {
        this.id = id;
        this.kullaniciTipi = kullaniciTipi;
        this.kullaniciAdi = kullaniciAdi;
        this.ad = ad;
        this.resimUrl = resimUrl;
        this.restoranId = restoranId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKullaniciTipi() {
        return kullaniciTipi;
    }

    public void setKullaniciTipi(String kullaniciTipi) {
        this.kullaniciTipi = kullaniciTipi;
    }

    public String getKullaniciAdi() {
        return kullaniciAdi;
    }

    public void setKullaniciAdi(String kullaniciAdi) {
        this.kullaniciAdi = kullaniciAdi;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getResimUrl() {
        return resimUrl;
    }

    public void setResimUrl(String resimUrl) {
        this.resimUrl = resimUrl;
    }

    public String getRestoranId() {
        return restoranId;
    }

    public void setRestoranId(String restoranId) {
        this.restoranId = restoranId;
    }
}
