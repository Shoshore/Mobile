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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Parametres carte
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        // Ajouter les pins des photos
        List<PhotoPin> pins = getSamplePins();
        for (PhotoPin pin : pins) {
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(pin.latLng)
                    .title(pin.title)
                    .snippet(pin.author + " · " + pin.date)
                    .icon(BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_AZURE)));
            if (marker != null) marker.setTag(pin);
        }

        // Clic sur un marker → fiche detail
        mMap.setOnMarkerClickListener(marker -> {
            marker.showInfoWindow();
            return false;
        });

        mMap.setOnInfoWindowClickListener(marker -> {
            PhotoPin pin = (PhotoPin) marker.getTag();
            if (pin != null) {
                PhotoModel photo = new PhotoModel(
                        pin.title, pin.author, pin.location,
                        pin.date, android.R.drawable.ic_menu_gallery, pin.likes);
                PhotoDetailFragment detail = PhotoDetailFragment.newInstance(photo);
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, detail)
                        .addToBackStack(null)
                        .commit();
            }
        });

        // Centrer sur l'Europe par defaut
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(48.8566, 2.3522), 4f));
    }

    private List<PhotoPin> getSamplePins() {
        List<PhotoPin> list = new ArrayList<>();
        list.add(new PhotoPin("Tour Eiffel au coucher", "Marie L.",
                "Paris, France", "Avril 2024", 142,
                new LatLng(48.8584, 2.2945)));
        list.add(new PhotoPin("Plage de Bondi", "Tom R.",
                "Sydney, Australie", "Janvier 2024", 98,
                new LatLng(-33.8908, 151.2743)));
        list.add(new PhotoPin("Marche de Marrakech", "Sophie M.",
                "Marrakech, Maroc", "Mars 2024", 76,
                new LatLng(31.6295, -7.9811)));
        list.add(new PhotoPin("Foret de Kyoto", "Kenji A.",
                "Kyoto, Japon", "Novembre 2023", 215,
                new LatLng(35.0116, 135.7681)));
        list.add(new PhotoPin("Musee du Louvre", "Emma D.",
                "Paris, France", "Juin 2024", 310,
                new LatLng(48.8606, 2.3376)));
        list.add(new PhotoPin("Montagne enneigee", "Pierre V.",
                "Chamonix, France", "Jan 2024", 129,
                new LatLng(45.9237, 6.8694)));
        return list;
    }

    // Modele interne pour les pins
    static class PhotoPin {
        String title, author, location, date;
        int likes;
        LatLng latLng;

        PhotoPin(String title, String author, String location,
                 String date, int likes, LatLng latLng) {
            this.title    = title;
            this.author   = author;
            this.location = location;
            this.date     = date;
            this.likes    = likes;
            this.latLng   = latLng;
        }
    }
}