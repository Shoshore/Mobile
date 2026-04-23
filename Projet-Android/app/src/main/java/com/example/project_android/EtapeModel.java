package com.example.project_android;

import java.util.List;

public class EtapeModel {
    private String titre;
    private String horaire;
    private String description;
    private String duree;
    private String distance;
    private List<Integer> photos; // resIds pour la demo

    public EtapeModel(String titre, String horaire, String description,
                      String duree, String distance, List<Integer> photos) {
        this.titre       = titre;
        this.horaire     = horaire;
        this.description = description;
        this.duree       = duree;
        this.distance    = distance;
        this.photos      = photos;
    }

    public String getTitre()           { return titre; }
    public String getHoraire()         { return horaire; }
    public String getDescription()     { return description; }
    public String getDuree()           { return duree; }
    public String getDistance()        { return distance; }
    public List<Integer> getPhotos()   { return photos; }
}