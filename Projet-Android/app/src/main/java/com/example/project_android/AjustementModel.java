package com.example.project_android;

public class AjustementModel {
    private int     budget;
    private int     duree;
    private boolean plusMusees;
    private boolean plusRestos;
    private boolean moinsMarche;
    private boolean eviterFoule;

    public AjustementModel() {
        this.budget      = 100;
        this.duree       = 5;
        this.plusMusees  = false;
        this.plusRestos  = false;
        this.moinsMarche = false;
        this.eviterFoule = false;
    }

    public int     getBudget()      { return budget; }
    public int     getDuree()       { return duree; }
    public boolean isPlusMusees()   { return plusMusees; }
    public boolean isPlusRestos()   { return plusRestos; }
    public boolean isMoinsMarche()  { return moinsMarche; }
    public boolean isEviterFoule()  { return eviterFoule; }

    public void setBudget(int v)          { budget = v; }
    public void setDuree(int v)           { duree = v; }
    public void setPlusMusees(boolean v)  { plusMusees = v; }
    public void setPlusRestos(boolean v)  { plusRestos = v; }
    public void setMoinsMarche(boolean v) { moinsMarche = v; }
    public void setEviterFoule(boolean v) { eviterFoule = v; }
}