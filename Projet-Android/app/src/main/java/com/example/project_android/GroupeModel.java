package com.example.project_android;

import java.util.ArrayList;
import java.util.List;

public class GroupeModel {
    private String id;
    private String nom;
    private String description;
    private String createur;
    private List<String> membres;
    private List<PhotoModel> photos;
    private boolean estPublic;

    public GroupeModel(String id, String nom, String description,
                       String createur, boolean estPublic) {
        this.id          = id;
        this.nom         = nom;
        this.description = description;
        this.createur    = createur;
        this.estPublic   = estPublic;
        this.membres     = new ArrayList<>();
        this.photos      = new ArrayList<>();
        this.membres.add(createur);
    }

    public String          getId()          { return id; }
    public String          getNom()         { return nom; }
    public String          getDescription() { return description; }
    public String          getCreateur()    { return createur; }
    public List<String>    getMembres()     { return membres; }
    public List<PhotoModel> getPhotos()     { return photos; }
    public boolean         isEstPublic()    { return estPublic; }

    public void addMembre(String membre) {
        if (!membres.contains(membre)) membres.add(membre);
    }

    public void addPhoto(PhotoModel photo) {
        photos.add(0, photo);
    }

    public int getNbMembres() { return membres.size(); }
    public int getNbPhotos()  { return photos.size(); }
}