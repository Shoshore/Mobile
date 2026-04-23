package com.example.project_android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import java.util.ArrayList;
import java.util.List;

public class TravelPathMapFragment extends Fragment implements OnMapReadyCallback {

    private static final String ARG_VILLE = "ville";
    private static final String ARG_TITRE = "titre";
    private GoogleMap mMap;
    private String ville;
    private String titre;

    public static TravelPathMapFragment newInstance(String ville, String titre) {
        TravelPathMapFragment f = new TravelPathMapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_VILLE, ville);
        args.putString(ARG_TITRE, titre);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        ville = args != null ? args.getString(ARG_VILLE, "Paris") : "Paris";
        titre = args != null ? args.getString(ARG_TITRE, "Parcours") : "Parcours";
        return inflater.inflate(R.layout.fragment_travelpath_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btn_map_retour).setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map_parcours);
        if (mapFragment != null) mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        List<LatLng> etapeCoords = getEtapesForVille(ville);
        List<String> etapeLabels = getLabelsForVille(ville);

        if (etapeCoords.isEmpty()) return;

        // Ajouter markers numerotes
        for (int i = 0; i < etapeCoords.size(); i++) {
            float color = i == 0
                    ? BitmapDescriptorFactory.HUE_GREEN
                    : (i == etapeCoords.size() - 1
                    ? BitmapDescriptorFactory.HUE_RED
                    : BitmapDescriptorFactory.HUE_AZURE);

            mMap.addMarker(new MarkerOptions()
                    .position(etapeCoords.get(i))
                    .title("Etape " + (i + 1))
                    .snippet(etapeLabels.get(i))
                    .icon(BitmapDescriptorFactory.defaultMarker(color)));
        }

        // Tracer le chemin entre les etapes
        PolylineOptions polyline = new PolylineOptions()
                .addAll(etapeCoords)
                .width(6f)
                .color(0xFF1A1A2E)
                .geodesic(true);
        mMap.addPolyline(polyline);

        // Centrer sur le centre du parcours
        LatLng centre = etapeCoords.get(0);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centre, 14f));
    }

    private List<LatLng> getEtapesForVille(String ville) {
        List<LatLng> list = new ArrayList<>();
        switch (ville.toLowerCase()) {
            case "paris":
                list.add(new LatLng(48.8534, 2.3488)); // Hotel de Ville
                list.add(new LatLng(48.8606, 2.3376)); // Louvre
                list.add(new LatLng(48.8738, 2.2950)); // Arc de Triomphe
                list.add(new LatLng(48.8584, 2.2945)); // Tour Eiffel
                break;
            case "tokyo":
                list.add(new LatLng(35.6762, 139.6503)); // Shinjuku
                list.add(new LatLng(35.7148, 139.7967)); // Senso-ji
                list.add(new LatLng(35.6586, 139.7454)); // Tokyo Tower
                break;
            case "rome":
                list.add(new LatLng(41.9022, 12.4539)); // Colisee
                list.add(new LatLng(41.8986, 12.4768)); // Forum romain
                list.add(new LatLng(41.9009, 12.4833)); // Palatin
                break;
            default:
                // Coordonnees generiques centrees sur la France
                list.add(new LatLng(48.8566, 2.3522));
                list.add(new LatLng(48.8606, 2.3376));
                list.add(new LatLng(48.8738, 2.2950));
                break;
        }
        return list;
    }

    private List<String> getLabelsForVille(String ville) {
        List<String> list = new ArrayList<>();
        switch (ville.toLowerCase()) {
            case "paris":
                list.add("Hotel de Ville");
                list.add("Musee du Louvre");
                list.add("Arc de Triomphe");
                list.add("Tour Eiffel");
                break;
            case "tokyo":
                list.add("Shinjuku");
                list.add("Temple Senso-ji");
                list.add("Tokyo Tower");
                break;
            case "rome":
                list.add("Colisee");
                list.add("Forum romain");
                list.add("Mont Palatin");
                break;
            default:
                list.add("Depart");
                list.add("Etape centrale");
                list.add("Arrivee");
                break;
        }
        return list;
    }
}