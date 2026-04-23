package com.example.project_android;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimilarityEngine {

    // Score de similarité entre deux photos (0.0 à 1.0)
    public static double computeSimilarity(PhotoModel a, PhotoModel b) {
        double score = 0.0;

        // Même lieu (pays ou ville)
        if (sameCountry(a.getLocation(), b.getLocation())) score += 0.3;
        if (sameCity(a.getLocation(), b.getLocation()))    score += 0.2;

        // Même auteur
        if (a.getAuthor().equalsIgnoreCase(b.getAuthor())) score += 0.2;

        // Même période (année)
        if (sameYear(a.getDate(), b.getDate())) score += 0.15;

        // Mots clés communs dans le titre
        score += titleSimilarity(a.getTitle(), b.getTitle()) * 0.15;

        return Math.min(score, 1.0);
    }

    // Retourne les N photos les plus similaires à une photo donnée
    public static List<ScoredPhoto> findSimilar(PhotoModel reference,
                                                List<PhotoModel> allPhotos,
                                                int maxResults) {
        List<ScoredPhoto> scored = new ArrayList<>();

        for (PhotoModel p : allPhotos) {
            if (p.getTitle().equals(reference.getTitle()) &&
                    p.getAuthor().equals(reference.getAuthor())) continue;

            double score = computeSimilarity(reference, p);
            if (score > 0.1) scored.add(new ScoredPhoto(p, score));
        }

        Collections.sort(scored,
                (a, b) -> Double.compare(b.getScore(), a.getScore()));

        return scored.subList(0, Math.min(maxResults, scored.size()));
    }

    // ── Helpers ───────────────────────────────────────────────────

    private static boolean sameCity(String locA, String locB) {
        if (locA == null || locB == null) return false;
        String cityA = locA.contains(",") ? locA.split(",")[0].trim() : locA;
        String cityB = locB.contains(",") ? locB.split(",")[0].trim() : locB;
        return cityA.equalsIgnoreCase(cityB);
    }

    private static boolean sameCountry(String locA, String locB) {
        if (locA == null || locB == null) return false;
        String countryA = locA.contains(",")
                ? locA.split(",")[locA.split(",").length - 1].trim() : locA;
        String countryB = locB.contains(",")
                ? locB.split(",")[locB.split(",").length - 1].trim() : locB;
        return countryA.equalsIgnoreCase(countryB);
    }

    private static boolean sameYear(String dateA, String dateB) {
        if (dateA == null || dateB == null) return false;
        try {
            String yearA = dateA.replaceAll("[^0-9]", "").substring(0, 4);
            String yearB = dateB.replaceAll("[^0-9]", "").substring(0, 4);
            return yearA.equals(yearB);
        } catch (Exception e) {
            return false;
        }
    }

    private static double titleSimilarity(String titleA, String titleB) {
        if (titleA == null || titleB == null) return 0.0;
        String[] wordsA = titleA.toLowerCase().split(" ");
        String[] wordsB = titleB.toLowerCase().split(" ");

        int commonWords = 0;
        for (String wA : wordsA) {
            if (wA.length() < 3) continue; // Ignorer mots courts
            for (String wB : wordsB) {
                if (wA.equalsIgnoreCase(wB)) { commonWords++; break; }
            }
        }

        int maxWords = Math.max(wordsA.length, wordsB.length);
        return maxWords > 0 ? (double) commonWords / maxWords : 0.0;
    }

    // Modele interne photo avec score
    public static class ScoredPhoto {
        private PhotoModel photo;
        private double score;

        public ScoredPhoto(PhotoModel photo, double score) {
            this.photo = photo;
            this.score = score;
        }

        public PhotoModel getPhoto() { return photo; }
        public double     getScore() { return score; }

        public String getScoreLabel() {
            if (score >= 0.7) return "Tres similaire";
            if (score >= 0.4) return "Similaire";
            return "Peut-etre similaire";
        }
    }
}