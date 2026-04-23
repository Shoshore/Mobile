package com.example.project_android;

import android.os.Handler;
import android.os.Looper;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WeatherService {

    private static final String API_KEY  = "55bd1132f1c7cb94013bea9670e8fba4";
    private static final String BASE_URL =
            "https://api.openweathermap.org/data/2.5/weather";

    public interface WeatherCallback {
        void onSuccess(WeatherModel weather);
        void onError(String message);
    }

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler      = new Handler(Looper.getMainLooper());

    public void fetchWeather(String ville, WeatherCallback callback) {
        executor.execute(() -> {
            try {
                String urlStr = BASE_URL
                        + "?q=" + ville.replace(" ", "+")
                        + "&appid=" + API_KEY
                        + "&units=metric"
                        + "&lang=fr";

                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);

                int responseCode = conn.getResponseCode();

                if (responseCode == 200) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) sb.append(line);
                    reader.close();

                    JSONObject json     = new JSONObject(sb.toString());
                    JSONObject main     = json.getJSONObject("main");
                    JSONObject wind     = json.getJSONObject("wind");
                    JSONObject weather0 = json.getJSONArray("weather").getJSONObject(0);

                    WeatherModel model = new WeatherModel(
                            ville,
                            weather0.getString("description"),
                            main.getDouble("temp"),
                            main.getDouble("temp_min"),
                            main.getDouble("temp_max"),
                            main.getInt("humidity"),
                            wind.getDouble("speed"),
                            weather0.getString("icon")
                    );

                    mainHandler.post(() -> callback.onSuccess(model));
                } else {
                    mainHandler.post(() -> callback.onError("Ville introuvable"));
                }
                conn.disconnect();

            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }

    // Convertit le code icon OpenWeatherMap en emoji
    public static String iconToEmoji(String icon) {
        if (icon == null) return "🌡️";
        switch (icon.substring(0, 2)) {
            case "01": return "☀️";
            case "02": return "🌤️";
            case "03": return "🌥️";
            case "04": return "☁️";
            case "09": return "🌧️";
            case "10": return "🌦️";
            case "11": return "⛈️";
            case "13": return "❄️";
            case "50": return "🌫️";
            default:   return "🌡️";
        }
    }

    // Conseil vestimentaire selon la meteo
    public static String getWeatherAdvice(WeatherModel weather) {
        double temp = weather.getTemperature();
        String desc = weather.getDescription().toLowerCase();

        if (desc.contains("pluie") || desc.contains("averse")) {
            return "🌂 Prenez un parapluie !";
        } else if (desc.contains("neige")) {
            return "🧤 Couvrez-vous bien, il neige !";
        } else if (desc.contains("orage")) {
            return "⚡ Evitez les sorties, orage prevu !";
        } else if (temp < 5) {
            return "🧥 Tres froid, habillez-vous chaudement.";
        } else if (temp < 15) {
            return "🧣 Frais, prenez une veste.";
        } else if (temp < 25) {
            return "👕 Temperature agreable, bonne balade !";
        } else {
            return "🕶️ Chaud ! Pensez a vous hydrater.";
        }
    }
}