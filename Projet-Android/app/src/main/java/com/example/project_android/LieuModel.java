package com.example.project_android;

import java.util.List;

public class LieuModel {
    private String nom;
    private String type;
    private String adresse;
    private String telephone;
    private String siteWeb;
    private List<CreneauModel> creneaux;
    private double latitude;
    private double longitude;

    public LieuModel(String nom, String type, String adresse,
                     String telephone, String siteWeb,
                     List<CreneauModel> creneaux,
                     double latitude, double longitude) {
        this.nom       = nom;
        this.type      = type;
        this.adresse   = adresse;
        this.telephone = telephone;
        this.siteWeb   = siteWeb;
        this.creneaux  = creneaux;
        this.latitude  = latitude;
        this.longitude = longitude;
    }

    public String           getNom()       { return nom; }
    public String           getType()      { return type; }
    public String           getAdresse()   { return adresse; }
    public String           getTelephone() { return telephone; }
    public String           getSiteWeb()   { return siteWeb; }
    public List<CreneauModel> getCreneaux(){ return creneaux; }
    public double           getLatitude()  { return latitude; }
    public double           getLongitude() { return longitude; }
}