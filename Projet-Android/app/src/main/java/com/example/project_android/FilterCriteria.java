package com.example.project_android;

public class FilterCriteria {
    public String auteur       = "";
    public String periodeDebut = "";
    public String periodeFin   = "";
    public String lieu         = "";
    public String typeLieu     = "";
    public int    rayonKm      = 0;

    public boolean isEmpty() {
        return auteur.isEmpty()
                && periodeDebut.isEmpty()
                && periodeFin.isEmpty()
                && lieu.isEmpty()
                && typeLieu.isEmpty()
                && rayonKm == 0;
    }
}