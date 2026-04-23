package com.example.project_android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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

public class EtapeMapFragment extends Fragment implements OnMapReadyCallback {

    private static final String ARG_TITRE   = "titre";
    private static final String ARG_HORAIRE = "horaire";
    private static final String ARG_DESC    = "desc";
    private static final String ARG_LAT     = "lat";
    private static final String ARG_LNG     = "lng";

    private double latitude;
    private double longitude;
    private String titre;

    public static EtapeMapFragment newInstance(EtapeModel etape) {
        EtapeMapFragment f = new EtapeMapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITRE,   etape.getTitre());
        args.putString(ARG_HORAIRE, etape.getHoraire());
        args.putString(ARG_DESC,    etape.getDescription());
        args.putDouble(ARG_LAT,     etape.getLatitude());
        args.putDouble(ARG_LNG,     etape.getLongitude());
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_etape_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        titre     = args != null ? args.getString(ARG_TITRE, "")   : "";
        String horaire = args != null ? args.getString(ARG_HORAIRE, "") : "";
        String desc    = args != null ? args.getString(ARG_DESC, "")   : "";
        latitude  = args != null ? args.getDouble(ARG_LAT, 48.8566) : 48.8566;
        longitude = args != null ? args.getDouble(ARG_LNG, 2.3522)  : 2.3522;

        // Infos étape
        TextView tvTitre   = view.findViewById(R.id.tv_etape_map_titre);
        TextView tvHoraire = view.findViewById(R.id.tv_etape_map_horaire);
        TextView tvDesc    = view.findViewById(R.id.tv_etape_map_desc);
        tvTitre.setText(titre);
        tvHoraire.setText("🕐 " + horaire);
        tvDesc.setText(desc);

        // Bouton itinéraire Google Maps
        Button btnItineraire = view.findViewById(R.id.btn_etape_itineraire);
        btnItineraire.setOnClickListener(v -> {
            String query = Uri.encode(titre);
            Uri mapUri   = Uri.parse("google.navigation:q=" + query);
            Intent intent = new Intent(Intent.ACTION_VIEW, mapUri);
            intent.setPackage("com.google.android.apps.maps");
            if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Uri browserUri = Uri.parse(
                        "https://www.google.com/maps/search/?api=1&query=" + query);
                startActivity(new Intent(Intent.ACTION_VIEW, browserUri));
            }
        });

        // Retour
        view.findViewById(R.id.btn_etape_map_retour).setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        // Carte
        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map_etape);
        if (mapFragment != null) mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);

        LatLng pos = new LatLng(latitude, longitude);

        googleMap.addMarker(new MarkerOptions()
                .position(pos)
                .title(titre)
                .icon(BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_AZURE)));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 15f));
    }
}