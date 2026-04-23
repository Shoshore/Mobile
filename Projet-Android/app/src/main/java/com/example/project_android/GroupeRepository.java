package com.example.project_android;

import java.util.ArrayList;
import java.util.List;

public class GroupeRepository {

    private static GroupeRepository instance;
    private List<GroupeModel> groupes = new ArrayList<>();

    public static GroupeRepository getInstance() {
        if (instance == null) instance = new GroupeRepository();
        return instance;
    }

    private GroupeRepository() {
        // Groupes de demo
        GroupeModel g1 = new GroupeModel("1",
                "Voyageurs du monde",
                "Partagez vos plus beaux voyages autour du globe.",
                "Marie L.", true);
        g1.addMembre("Tom R.");
        g1.addMembre("Sophie M.");
        g1.addPhoto(new PhotoModel("Tour Eiffel", "Marie L.",
                "Paris, France", "Avril 2024",
                android.R.drawable.ic_menu_gallery, 142));

        GroupeModel g2 = new GroupeModel("2",
                "Amateurs de nature",
                "Photos de paysages naturels et randonnees.",
                "Pierre V.", true);
        g2.addMembre("Alice B.");
        g2.addPhoto(new PhotoModel("Montagne enneigee", "Pierre V.",
                "Chamonix, France", "Jan 2024",
                android.R.drawable.ic_menu_gallery, 129));

        GroupeModel g3 = new GroupeModel("3",
                "Foodies en voyage",
                "Decouverte gastronomique autour du monde.",
                "Yuki T.", false);
        g3.addMembre("Carlos M.");

        groupes.add(g1);
        groupes.add(g2);
        groupes.add(g3);
    }

    public List<GroupeModel> getAllGroupes() { return groupes; }

    public List<GroupeModel> getGroupesForUser(String userName) {
        List<GroupeModel> result = new ArrayList<>();
        for (GroupeModel g : groupes) {
            if (g.getMembres().contains(userName)) result.add(g);
        }
        return result;
    }

    public boolean rejoindreGroupe(String groupeId, String userName) {
        for (GroupeModel g : groupes) {
            if (g.getId().equals(groupeId)) {
                if (g.getMembres().contains(userName)) return false;
                g.addMembre(userName);
                return true;
            }
        }
        return false;
    }

    public GroupeModel creerGroupe(String nom, String description,
                                   String createur, boolean estPublic) {
        String id = String.valueOf(groupes.size() + 1);
        GroupeModel g = new GroupeModel(id, nom, description, createur, estPublic);
        groupes.add(g);
        return g;
    }

    public GroupeModel getGroupeById(String id) {
        for (GroupeModel g : groupes) {
            if (g.getId().equals(id)) return g;
        }
        return null;
    }

    public void publierDansGroupe(String groupeId, PhotoModel photo) {
        GroupeModel g = getGroupeById(groupeId);
        if (g != null) g.addPhoto(photo);
    }
}