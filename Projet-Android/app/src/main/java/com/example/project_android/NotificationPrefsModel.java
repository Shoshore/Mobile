package com.example.project_android;

public class NotificationPrefsModel {
    private boolean notifNouvellesPhotos;
    private boolean notifGroupes;
    private boolean notifLieux;
    private String  auteurSuivi;
    private String  lieuSuivi;
    private String  groupeSuivi;

    public NotificationPrefsModel() {
        this.notifNouvellesPhotos = true;
        this.notifGroupes         = true;
        this.notifLieux           = false;
        this.auteurSuivi          = "";
        this.lieuSuivi            = "";
        this.groupeSuivi          = "";
    }

    public boolean isNotifNouvellesPhotos()   { return notifNouvellesPhotos; }
    public boolean isNotifGroupes()           { return notifGroupes; }
    public boolean isNotifLieux()             { return notifLieux; }
    public String  getAuteurSuivi()           { return auteurSuivi; }
    public String  getLieuSuivi()             { return lieuSuivi; }
    public String  getGroupeSuivi()           { return groupeSuivi; }

    public void setNotifNouvellesPhotos(boolean v) { notifNouvellesPhotos = v; }
    public void setNotifGroupes(boolean v)         { notifGroupes = v; }
    public void setNotifLieux(boolean v)           { notifLieux = v; }
    public void setAuteurSuivi(String v)           { auteurSuivi = v; }
    public void setLieuSuivi(String v)             { lieuSuivi = v; }
    public void setGroupeSuivi(String v)           { groupeSuivi = v; }
}