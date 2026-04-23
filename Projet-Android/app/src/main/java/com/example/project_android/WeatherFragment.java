package com.example.project_android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class WeatherFragment extends Fragment {

    private WeatherService weatherService;
    private EditText etVilleMeteo;
    private ProgressBar progressBar;
    private LinearLayout layoutResult;
    private TextView tvWeatherEmoji, tvWeatherVille, tvWeatherTemp;
    private TextView tvWeatherDesc, tvWeatherMinMax;
    private TextView tvWeatherHumidity, tvWeatherWind;
    private TextView tvWeatherAdvice;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        weatherService    = new WeatherService();
        etVilleMeteo      = view.findViewById(R.id.et_ville_meteo);
        progressBar       = view.findViewById(R.id.progress_weather);
        layoutResult      = view.findViewById(R.id.layout_weather_result);
        tvWeatherEmoji    = view.findViewById(R.id.tv_weather_emoji);
        tvWeatherVille    = view.findViewById(R.id.tv_weather_ville);
        tvWeatherTemp     = view.findViewById(R.id.tv_weather_temp);
        tvWeatherDesc     = view.findViewById(R.id.tv_weather_desc);
        tvWeatherMinMax   = view.findViewById(R.id.tv_weather_minmax);
        tvWeatherHumidity = view.findViewById(R.id.tv_weather_humidity);
        tvWeatherWind     = view.findViewById(R.id.tv_weather_wind);
        tvWeatherAdvice   = view.findViewById(R.id.tv_weather_advice);

        Button btnSearch = view.findViewById(R.id.btn_search_weather);
        Button btnRetour = view.findViewById(R.id.btn_weather_retour);

        btnSearch.setOnClickListener(v -> {
            String ville = etVilleMeteo.getText().toString().trim();
            if (ville.isEmpty()) {
                etVilleMeteo.setError("Entrez une ville");
                return;
            }
            fetchWeather(ville);
        });

        btnRetour.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        // Si une ville est passee en argument (depuis TravelPath)
        Bundle args = getArguments();
        if (args != null && args.containsKey("ville")) {
            String ville = args.getString("ville");
            etVilleMeteo.setText(ville);
            fetchWeather(ville);
        }

        return view;
    }

    private void fetchWeather(String ville) {
        progressBar.setVisibility(View.VISIBLE);
        layoutResult.setVisibility(View.GONE);

        weatherService.fetchWeather(ville, new WeatherService.WeatherCallback() {
            @Override
            public void onSuccess(WeatherModel weather) {
                progressBar.setVisibility(View.GONE);
                layoutResult.setVisibility(View.VISIBLE);

                String emoji = WeatherService.iconToEmoji(weather.getIcon());
                tvWeatherEmoji.setText(emoji);
                tvWeatherVille.setText(weather.getVille());
                tvWeatherTemp.setText(String.format("%.0f°C", weather.getTemperature()));
                tvWeatherDesc.setText(weather.getDescription());
                tvWeatherMinMax.setText(String.format("↓ %.0f°C   ↑ %.0f°C",
                        weather.getTempMin(), weather.getTempMax()));
                tvWeatherHumidity.setText("💧 Humidite : " + weather.getHumidity() + "%");
                tvWeatherWind.setText(String.format("💨 Vent : %.0f km/h",
                        weather.getWindSpeed() * 3.6));
                tvWeatherAdvice.setText(WeatherService.getWeatherAdvice(weather));
            }

            @Override
            public void onError(String message) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(),
                        "Erreur : " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}