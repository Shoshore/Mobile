package com.example.project_android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRepository {

    private static UserRepository instance;
    public static UserRepository getInstance() {
        if (instance == null) instance = new UserRepository();
        return instance;
    }

    private Map<String, String> users     = new HashMap<>();
    private Map<String, String> userNames = new HashMap<>();
    private Map<String, List<PhotoModel>> userPhotos = new HashMap<>();

    private UserRepository() {
        users.put("demo@traveling.com", "1234");
        userNames.put("demo@traveling.com", "Jean Dupont");

        // Photos de demo pour Jean Dupont
        List<PhotoModel> demophotos = new ArrayList<>();
        demophotos.add(new PhotoModel("Mon voyage a Paris", "Jean Dupont",
                "Paris, France", "Avril 2024",
                android.R.drawable.ic_menu_gallery, 12));
        demophotos.add(new PhotoModel("Coucher de soleil Tokyo", "Jean Dupont",
                "Tokyo, Japon", "Janvier 2024",
                android.R.drawable.ic_menu_gallery, 34));
        demophotos.add(new PhotoModel("Plage de Lisbonne", "Jean Dupont",
                "Lisbonne, Portugal", "Mars 2024",
                android.R.drawable.ic_menu_gallery, 8));
        userPhotos.put("Jean Dupont", demophotos);
    }

    public boolean register(String email, String password, String name) {
        if (users.containsKey(email)) return false;
        users.put(email, password);
        userNames.put(email, name);
        userPhotos.put(name, new ArrayList<>());
        return true;
    }

    public boolean login(String email, String password) {
        return users.containsKey(email) && users.get(email).equals(password);
    }

    public String getName(String email) {
        return userNames.getOrDefault(email, "Utilisateur");
    }

    public List<PhotoModel> getPhotosForUser(String userName) {
        List<PhotoModel> photos = userPhotos.get(userName);
        if (photos == null) {
            photos = new ArrayList<>();
            userPhotos.put(userName, photos);
        }
        return photos;
    }

    public void addPhotoForUser(String userName, PhotoModel photo) {
        List<PhotoModel> photos = getPhotosForUser(userName);
        photos.add(0, photo);
    }

    public void removePhotoForUser(String userName, int index) {
        List<PhotoModel> photos = getPhotosForUser(userName);
        if (index >= 0 && index < photos.size()) {
            photos.remove(index);
        }
    }
}