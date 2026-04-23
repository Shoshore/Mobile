package com.example.project_android;

import java.util.List;

public class ParcourModel {
    private String titre;
    private String ville;
    private int budget;
    private int duree;
    private String effort;
    private List<String> etapes;
    private boolean liked;

    public ParcourModel(String titre, String ville, int budget,
                        int duree, String effort, List<String> etapes) {
        this.titre  = titre;
        this.ville  = ville;
        this.budget = budget;
        this.duree  = duree;
        this.effort = effort;
        this.etapes = etapes;
        this.liked  = false;
    }

    public String getTitre()       { return titre; }
    public String getVille()       { return ville; }
    public int getBudget()         { return budget; }
    public int getDuree()          { return duree; }
    public String getEffort()      { return effort; }
    public List<String> getEtapes(){ return etapes; }
    public boolean isLiked()       { return liked; }
    public void toggleLike()       { liked = !liked; }
}