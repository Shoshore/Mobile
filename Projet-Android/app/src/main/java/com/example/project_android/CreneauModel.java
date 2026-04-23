package com.example.project_android;

public class CreneauModel {
    private String jour;
    private String heureOuverture;
    private String heureFermeture;
    private boolean ferme;

    public CreneauModel(String jour, String heureOuverture,
                        String heureFermeture, boolean ferme) {
        this.jour            = jour;
        this.heureOuverture  = heureOuverture;
        this.heureFermeture  = heureFermeture;
        this.ferme           = ferme;
    }

    // Constructeur jour ferme
    public CreneauModel(String jour) {
        this.jour           = jour;
        this.heureOuverture = "";
        this.heureFermeture = "";
        this.ferme          = true;
    }

    public String  getJour()            { return jour; }
    public String  getHeureOuverture()  { return heureOuverture; }
    public String  getHeureFermeture()  { return heureFermeture; }
    public boolean isFerme()            { return ferme; }

    public String getLabel() {
        if (ferme) return "Ferme";
        return heureOuverture + " - " + heureFermeture;
    }
}