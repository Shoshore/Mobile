package com.example.project_android;

import java.util.HashMap;
import java.util.Map;

public class UserRepository {

    // Singleton
    private static UserRepository instance;
    public static UserRepository getInstance() {
        if (instance == null) instance = new UserRepository();
        return instance;
    }

    // Stockage en memoire : email → mot de passe
    private Map<String, String> users = new HashMap<>();
    private Map<String, String> userNames = new HashMap<>();

    private UserRepository() {
        // Compte de demo pre-enregistre
        users.put("demo@traveling.com", "1234");
        userNames.put("demo@traveling.com", "Jean Dupont");
    }

    public boolean register(String email, String password, String name) {
        if (users.containsKey(email)) return false; // email deja pris
        users.put(email, password);
        userNames.put(email, name);
        return true;
    }

    public boolean login(String email, String password) {
        return users.containsKey(email) && users.get(email).equals(password);
    }

    public String getName(String email) {
        return userNames.getOrDefault(email, "Utilisateur");
    }
}