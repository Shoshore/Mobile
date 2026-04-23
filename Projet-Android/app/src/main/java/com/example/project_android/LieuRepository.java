package com.example.project_android;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class LieuRepository {

    private static LieuRepository instance;

    public static LieuRepository getInstance() {
        if (instance == null) instance = new LieuRepository();
        return instance;
    }

    private LieuRepository() {}

    // Retourne les lieux pour une ville donnee
    public List<LieuModel> getlieuxForVille(String ville) {
        List<LieuModel> list = new ArrayList<>();
        switch (ville.toLowerCase()) {
            case "paris":
                list.add(createLouvre());
                list.add(createEiffel());
                list.add(createOrsay());
                list.add(createNotreDame());
                break;
            case "tokyo":
                list.add(createSensoJi());
                list.add(createTokyoTower());
                list.add(createTsukiji());
                break;
            case "rome":
                list.add(createColisee());
                list.add(createVatican());
                list.add(createTrevi());
                break;
            default:
                list.add(createGenericMusee(ville));
                list.add(createGenericResto(ville));
                list.add(createGenericPark(ville));
                break;
        }
        return list;
    }

    // Verifie si un lieu est ouvert maintenant
    public static boolean isOpenNow(LieuModel lieu) {
        Calendar cal  = Calendar.getInstance();
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK); // 1=dim, 2=lun...
        int hour      = cal.get(Calendar.HOUR_OF_DAY);
        int minute    = cal.get(Calendar.MINUTE);

        String[] jours = {"Dimanche","Lundi","Mardi","Mercredi",
                "Jeudi","Vendredi","Samedi"};
        String aujourdhui = jours[dayOfWeek - 1];

        for (CreneauModel c : lieu.getCreneaux()) {
            if (c.getJour().equalsIgnoreCase(aujourdhui)) {
                if (c.isFerme()) return false;
                try {
                    int oH = Integer.parseInt(c.getHeureOuverture().split(":")[0]);
                    int oM = Integer.parseInt(c.getHeureOuverture().split(":")[1]);
                    int fH = Integer.parseInt(c.getHeureFermeture().split(":")[0]);
                    int fM = Integer.parseInt(c.getHeureFermeture().split(":")[1]);
                    int now = hour * 60 + minute;
                    int open  = oH * 60 + oM;
                    int close = fH * 60 + fM;
                    return now >= open && now <= close;
                } catch (Exception e) {
                    return false;
                }
            }
        }
        return false;
    }

    // Retourne le statut d'ouverture sous forme de texte
    public static String getStatutOuverture(LieuModel lieu) {
        if (isOpenNow(lieu)) {
            // Trouver l'heure de fermeture du jour
            Calendar cal = Calendar.getInstance();
            String[] jours = {"Dimanche","Lundi","Mardi","Mercredi",
                    "Jeudi","Vendredi","Samedi"};
            String aujourdhui = jours[cal.get(Calendar.DAY_OF_WEEK) - 1];
            for (CreneauModel c : lieu.getCreneaux()) {
                if (c.getJour().equalsIgnoreCase(aujourdhui) && !c.isFerme()) {
                    return "Ouvert · Ferme a " + c.getHeureFermeture();
                }
            }
            return "Ouvert";
        } else {
            // Trouver la prochaine ouverture
            Calendar cal = Calendar.getInstance();
            String[] jours = {"Dimanche","Lundi","Mardi","Mercredi",
                    "Jeudi","Vendredi","Samedi"};
            String aujourdhui = jours[cal.get(Calendar.DAY_OF_WEEK) - 1];
            for (CreneauModel c : lieu.getCreneaux()) {
                if (c.getJour().equalsIgnoreCase(aujourdhui) && !c.isFerme()) {
                    return "Ferme · Ouvre a " + c.getHeureOuverture();
                }
                if (c.getJour().equalsIgnoreCase(aujourdhui) && c.isFerme()) {
                    return "Ferme aujourd'hui";
                }
            }
            return "Ferme";
        }
    }

    // ── Lieux Paris ───────────────────────────────────────────────

    private LieuModel createLouvre() {
        return new LieuModel("Musee du Louvre", "Musee",
                "Rue de Rivoli, 75001 Paris",
                "+33 1 40 20 53 17", "louvre.fr",
                Arrays.asList(
                        new CreneauModel("Lundi", "09:00", "18:00", false),
                        new CreneauModel("Mardi"),
                        new CreneauModel("Mercredi", "09:00", "21:45", false),
                        new CreneauModel("Jeudi", "09:00", "18:00", false),
                        new CreneauModel("Vendredi", "09:00", "21:45", false),
                        new CreneauModel("Samedi", "09:00", "18:00", false),
                        new CreneauModel("Dimanche", "09:00", "18:00", false)),
                48.8606, 2.3376);
    }

    private LieuModel createEiffel() {
        return new LieuModel("Tour Eiffel", "Monument",
                "Champ de Mars, 75007 Paris",
                "+33 892 70 12 39", "toureiffel.paris",
                Arrays.asList(
                        new CreneauModel("Lundi", "09:00", "23:45", false),
                        new CreneauModel("Mardi", "09:00", "23:45", false),
                        new CreneauModel("Mercredi", "09:00", "23:45", false),
                        new CreneauModel("Jeudi", "09:00", "23:45", false),
                        new CreneauModel("Vendredi", "09:00", "23:45", false),
                        new CreneauModel("Samedi", "09:00", "23:45", false),
                        new CreneauModel("Dimanche", "09:00", "23:45", false)),
                48.8584, 2.2945);
    }

    private LieuModel createOrsay() {
        return new LieuModel("Musee d'Orsay", "Musee",
                "1 Rue de la Legion d'Honneur, 75007 Paris",
                "+33 1 40 49 48 14", "musee-orsay.fr",
                Arrays.asList(
                        new CreneauModel("Lundi"),
                        new CreneauModel("Mardi", "09:30", "18:00", false),
                        new CreneauModel("Mercredi", "09:30", "21:45", false),
                        new CreneauModel("Jeudi", "09:30", "18:00", false),
                        new CreneauModel("Vendredi", "09:30", "18:00", false),
                        new CreneauModel("Samedi", "09:30", "18:00", false),
                        new CreneauModel("Dimanche", "09:30", "18:00", false)),
                48.8600, 2.3266);
    }

    private LieuModel createNotreDame() {
        return new LieuModel("Notre-Dame de Paris", "Monument",
                "6 Parvis Notre-Dame, 75004 Paris",
                "+33 1 42 34 56 10", "notredamedeparis.fr",
                Arrays.asList(
                        new CreneauModel("Lundi", "08:00", "18:45", false),
                        new CreneauModel("Mardi", "08:00", "18:45", false),
                        new CreneauModel("Mercredi", "08:00", "18:45", false),
                        new CreneauModel("Jeudi", "08:00", "18:45", false),
                        new CreneauModel("Vendredi", "08:00", "18:45", false),
                        new CreneauModel("Samedi", "08:00", "18:45", false),
                        new CreneauModel("Dimanche", "08:00", "19:15", false)),
                48.8530, 2.3499);
    }

    // ── Lieux Tokyo ───────────────────────────────────────────────

    private LieuModel createSensoJi() {
        return new LieuModel("Temple Senso-ji", "Temple",
                "2-3-1 Asakusa, Taito, Tokyo",
                "+81 3-3842-0181", "senso-ji.jp",
                Arrays.asList(
                        new CreneauModel("Lundi", "06:00", "17:00", false),
                        new CreneauModel("Mardi", "06:00", "17:00", false),
                        new CreneauModel("Mercredi", "06:00", "17:00", false),
                        new CreneauModel("Jeudi", "06:00", "17:00", false),
                        new CreneauModel("Vendredi", "06:00", "17:00", false),
                        new CreneauModel("Samedi", "06:00", "17:00", false),
                        new CreneauModel("Dimanche", "06:00", "17:00", false)),
                35.7148, 139.7967);
    }

    private LieuModel createTokyoTower() {
        return new LieuModel("Tokyo Tower", "Monument",
                "4 Chome-2-8 Shibakoen, Minato, Tokyo",
                "+81 3-3433-5111", "tokyotower.co.jp",
                Arrays.asList(
                        new CreneauModel("Lundi", "09:00", "23:00", false),
                        new CreneauModel("Mardi", "09:00", "23:00", false),
                        new CreneauModel("Mercredi", "09:00", "23:00", false),
                        new CreneauModel("Jeudi", "09:00", "23:00", false),
                        new CreneauModel("Vendredi", "09:00", "23:00", false),
                        new CreneauModel("Samedi", "09:00", "23:00", false),
                        new CreneauModel("Dimanche", "09:00", "23:00", false)),
                35.6586, 139.7454);
    }

    private LieuModel createTsukiji() {
        return new LieuModel("Marche Tsukiji", "Marche",
                "5 Chome-2-1 Tsukiji, Chuo, Tokyo",
                "+81 3-3541-9444", "tsukiji.or.jp",
                Arrays.asList(
                        new CreneauModel("Lundi", "05:00", "13:00", false),
                        new CreneauModel("Mardi", "05:00", "13:00", false),
                        new CreneauModel("Mercredi", "05:00", "13:00", false),
                        new CreneauModel("Jeudi", "05:00", "13:00", false),
                        new CreneauModel("Vendredi", "05:00", "13:00", false),
                        new CreneauModel("Samedi", "05:00", "13:00", false),
                        new CreneauModel("Dimanche")),
                35.6654, 139.7707);
    }

    // ── Lieux Rome ────────────────────────────────────────────────

    private LieuModel createColisee() {
        return new LieuModel("Colisee", "Monument",
                "Piazza del Colosseo, 1, 00184 Roma",
                "+39 06 3996 7700", "colosseo.it",
                Arrays.asList(
                        new CreneauModel("Lundi", "09:00", "19:00", false),
                        new CreneauModel("Mardi", "09:00", "19:00", false),
                        new CreneauModel("Mercredi", "09:00", "19:00", false),
                        new CreneauModel("Jeudi", "09:00", "19:00", false),
                        new CreneauModel("Vendredi", "09:00", "19:00", false),
                        new CreneauModel("Samedi", "09:00", "19:00", false),
                        new CreneauModel("Dimanche", "09:00", "19:00", false)),
                41.8902, 12.4922);
    }

    private LieuModel createVatican() {
        return new LieuModel("Musees du Vatican", "Musee",
                "Viale Vaticano, 00165 Roma",
                "+39 06 6988 4676", "museivaticani.va",
                Arrays.asList(
                        new CreneauModel("Lundi", "09:00", "18:00", false),
                        new CreneauModel("Mardi", "09:00", "18:00", false),
                        new CreneauModel("Mercredi", "09:00", "18:00", false),
                        new CreneauModel("Jeudi", "09:00", "18:00", false),
                        new CreneauModel("Vendredi", "09:00", "18:00", false),
                        new CreneauModel("Samedi", "09:00", "14:00", false),
                        new CreneauModel("Dimanche")),
                41.9065, 12.4536);
    }

    private LieuModel createTrevi() {
        return new LieuModel("Fontaine de Trevi", "Monument",
                "Piazza di Trevi, 00187 Roma",
                "", "turismoroma.it",
                Arrays.asList(
                        new CreneauModel("Lundi", "00:00", "23:59", false),
                        new CreneauModel("Mardi", "00:00", "23:59", false),
                        new CreneauModel("Mercredi", "00:00", "23:59", false),
                        new CreneauModel("Jeudi", "00:00", "23:59", false),
                        new CreneauModel("Vendredi", "00:00", "23:59", false),
                        new CreneauModel("Samedi", "00:00", "23:59", false),
                        new CreneauModel("Dimanche", "00:00", "23:59", false)),
                41.9009, 12.4833);
    }

    // ── Lieux generiques ─────────────────────────────────────────

    private LieuModel createGenericMusee(String ville) {
        return new LieuModel("Musee de " + ville, "Musee",
                "Centre-ville, " + ville, "", "",
                Arrays.asList(
                        new CreneauModel("Lundi"),
                        new CreneauModel("Mardi", "09:00", "17:00", false),
                        new CreneauModel("Mercredi", "09:00", "17:00", false),
                        new CreneauModel("Jeudi", "09:00", "17:00", false),
                        new CreneauModel("Vendredi", "09:00", "17:00", false),
                        new CreneauModel("Samedi", "10:00", "18:00", false),
                        new CreneauModel("Dimanche", "10:00", "16:00", false)),
                0, 0);
    }

    private LieuModel createGenericResto(String ville) {
        return new LieuModel("Restaurant local de " + ville, "Restaurant",
                "Quartier historique, " + ville, "", "",
                Arrays.asList(
                        new CreneauModel("Lundi", "12:00", "22:00", false),
                        new CreneauModel("Mardi", "12:00", "22:00", false),
                        new CreneauModel("Mercredi", "12:00", "22:00", false),
                        new CreneauModel("Jeudi", "12:00", "22:00", false),
                        new CreneauModel("Vendredi", "12:00", "23:00", false),
                        new CreneauModel("Samedi", "11:00", "23:00", false),
                        new CreneauModel("Dimanche", "11:00", "21:00", false)),
                0, 0);
    }

    private LieuModel createGenericPark(String ville) {
        return new LieuModel("Parc central de " + ville, "Parc",
                "Parc municipal, " + ville, "", "",
                Arrays.asList(
                        new CreneauModel("Lundi", "07:00", "21:00", false),
                        new CreneauModel("Mardi", "07:00", "21:00", false),
                        new CreneauModel("Mercredi", "07:00", "21:00", false),
                        new CreneauModel("Jeudi", "07:00", "21:00", false),
                        new CreneauModel("Vendredi", "07:00", "21:00", false),
                        new CreneauModel("Samedi", "07:00", "22:00", false),
                        new CreneauModel("Dimanche", "07:00", "22:00", false)),
                0, 0);
    }
}