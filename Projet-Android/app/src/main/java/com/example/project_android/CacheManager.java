package com.example.project_android;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class CacheManager {

    private static final String PREFS_NAME     = "traveling_cache";
    private static final String KEY_PARCOURS   = "cached_parcours";
    private static final String KEY_PHOTOS     = "cached_photos";
    private static final String KEY_WEATHER    = "cached_weather";
    private static final String KEY_TIMESTAMP  = "cache_timestamp";
    private static final long   CACHE_DURATION = 24 * 60 * 60 * 1000L; // 24h

    private static CacheManager instance;
    private SharedPreferences prefs;

    public static CacheManager getInstance(Context context) {
        if (instance == null) instance = new CacheManager(context);
        return instance;
    }

    private CacheManager(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    // ── Vérification connectivité ─────────────────────────────────

    public static boolean isOnline(Context context) {
        android.net.ConnectivityManager cm = (android.net.ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        android.net.NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    // ── Cache Parcours ────────────────────────────────────────────

    public void saveParcours(List<ParcourModel> parcoursList) {
        try {
            JSONArray array = new JSONArray();
            for (ParcourModel p : parcoursList) {
                JSONObject obj = new JSONObject();
                obj.put("titre",  p.getTitre());
                obj.put("ville",  p.getVille());
                obj.put("budget", p.getBudget());
                obj.put("duree",  p.getDuree());
                obj.put("effort", p.getEffort());

                JSONArray etapesArr = new JSONArray();
                for (String e : p.getEtapes()) etapesArr.put(e);
                obj.put("etapes", etapesArr);

                array.put(obj);
            }
            prefs.edit()
                    .putString(KEY_PARCOURS, array.toString())
                    .putLong(KEY_TIMESTAMP, System.currentTimeMillis())
                    .apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<ParcourModel> loadParcours() {
        List<ParcourModel> list = new ArrayList<>();
        String json = prefs.getString(KEY_PARCOURS, null);
        if (json == null) return list;
        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                List<String> etapes = new ArrayList<>();
                JSONArray etapesArr = obj.getJSONArray("etapes");
                for (int j = 0; j < etapesArr.length(); j++) {
                    etapes.add(etapesArr.getString(j));
                }
                list.add(new ParcourModel(
                        obj.getString("titre"),
                        obj.getString("ville"),
                        obj.getInt("budget"),
                        obj.getInt("duree"),
                        obj.getString("effort"),
                        etapes));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ── Cache Photos ──────────────────────────────────────────────

    public void savePhotos(List<PhotoModel> photos) {
        try {
            JSONArray array = new JSONArray();
            for (PhotoModel p : photos) {
                JSONObject obj = new JSONObject();
                obj.put("title",    p.getTitle());
                obj.put("author",   p.getAuthor());
                obj.put("location", p.getLocation());
                obj.put("date",     p.getDate());
                obj.put("likes",    p.getLikes());
                obj.put("imageRes", p.getImageResId());
                array.put(obj);
            }
            prefs.edit().putString(KEY_PHOTOS, array.toString()).apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<PhotoModel> loadPhotos() {
        List<PhotoModel> list = new ArrayList<>();
        String json = prefs.getString(KEY_PHOTOS, null);
        if (json == null) return list;
        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                list.add(new PhotoModel(
                        obj.getString("title"),
                        obj.getString("author"),
                        obj.getString("location"),
                        obj.getString("date"),
                        obj.getInt("imageRes"),
                        obj.getInt("likes")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ── Cache Météo ───────────────────────────────────────────────

    public void saveWeather(String ville, WeatherModel weather) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("ville",       weather.getVille());
            obj.put("description", weather.getDescription());
            obj.put("temperature", weather.getTemperature());
            obj.put("tempMin",     weather.getTempMin());
            obj.put("tempMax",     weather.getTempMax());
            obj.put("humidity",    weather.getHumidity());
            obj.put("windSpeed",   weather.getWindSpeed());
            obj.put("icon",        weather.getIcon());
            prefs.edit()
                    .putString(KEY_WEATHER + "_" + ville.toLowerCase(), obj.toString())
                    .apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public WeatherModel loadWeather(String ville) {
        String json = prefs.getString(KEY_WEATHER + "_" + ville.toLowerCase(), null);
        if (json == null) return null;
        try {
            JSONObject obj = new JSONObject(json);
            return new WeatherModel(
                    obj.getString("ville"),
                    obj.getString("description"),
                    obj.getDouble("temperature"),
                    obj.getDouble("tempMin"),
                    obj.getDouble("tempMax"),
                    obj.getInt("humidity"),
                    obj.getDouble("windSpeed"),
                    obj.getString("icon"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ── Utilitaires ───────────────────────────────────────────────

    public boolean isCacheValid() {
        long timestamp = prefs.getLong(KEY_TIMESTAMP, 0);
        return System.currentTimeMillis() - timestamp < CACHE_DURATION;
    }

    public boolean hasCachedParcours() {
        return prefs.contains(KEY_PARCOURS);
    }

    public boolean hasCachedPhotos() {
        return prefs.contains(KEY_PHOTOS);
    }

    public void clearCache() {
        prefs.edit().clear().apply();
    }

    public String getCacheInfo() {
        long timestamp = prefs.getLong(KEY_TIMESTAMP, 0);
        if (timestamp == 0) return "Aucun cache";
        long diff = System.currentTimeMillis() - timestamp;
        long hours = diff / (60 * 60 * 1000);
        long minutes = (diff % (60 * 60 * 1000)) / (60 * 1000);
        return "Cache : il y a " + (hours > 0 ? hours + "h " : "") + minutes + "min";
    }
}