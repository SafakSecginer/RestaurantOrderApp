package com.example.edatarestoran.Model;

public class Restoran {

    private String id;
    private String restoranAdi;
    private String restoranKodu;
    private String restoranLogo;

    public Restoran(String id, String restoranAdi, String restoranKodu, String restoranLogo) {
        this.id = id;
        this.restoranAdi = restoranAdi;
        this.restoranKodu = restoranKodu;
        this.restoranLogo = restoranLogo;
    }

    public Restoran() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRestoranAdi() {
        return restoranAdi;
    }

    public void setRestoranAdi(String restoranAdi) {
        this.restoranAdi = restoranAdi;
    }

    public String getRestoranKodu() {
        return restoranKodu;
    }

    public void setRestoranKodu(String restoranKodu) {
        this.restoranKodu = restoranKodu;
    }

    public String getRestoranLogo() {
        return restoranLogo;
    }

    public void setRestoranLogo(String restoranLogo) {
        this.restoranLogo = restoranLogo;
    }
}
