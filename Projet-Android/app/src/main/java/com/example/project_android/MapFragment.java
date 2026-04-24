package com.example.project_android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
    private List<PhotoPin> allPins;
    private String currentFilter = "Tous";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        allPins = getSamplePins();

        // Spinner filtre type de lieu
        Spinner spinner = view.findViewById(R.id.spinner_map_filter);
        String[] filtres = {"Tous", "Nature", "Monument", "Musee",
                "Restaurant", "Rue", "Plage"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                filtres);
        spinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v,
                                       int position, long id) {
                currentFilter = filtres[position];
                if (mMap != null) applyFilter();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        // Clic info window → fiche détail
        mMap.setOnInfoWindowClickListener(marker -> {
            PhotoPin pin = (PhotoPin) marker.getTag();
            if (pin != null) {
                PhotoModel photo = new PhotoModel(
                        pin.title, pin.author, pin.location,
                        pin.date, android.R.drawable.ic_menu_gallery,
                        pin.likes);
                PhotoDetailFragment detail = PhotoDetailFragment.newInstance(photo);
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, detail)
                        .addToBackStack(null)
                        .commit();
            }
        });

        applyFilter();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(48.8566, 2.3522), 4f));
    }

    private void applyFilter() {
        mMap.clear();
        List<PhotoPin> filtered = getFilteredPins();

        for (PhotoPin pin : filtered) {
            float color = getColorForType(pin.type);
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(pin.latLng)
                    .title(pin.title)
                    .snippet(pin.author + " · " + pin.location)
                    .icon(BitmapDescriptorFactory.defaultMarker(color)));
            if (marker != null) marker.setTag(pin);
        }
    }

    private List<PhotoPin> getFilteredPins() {
        if (currentFilter.equals("Tous")) return allPins;
        List<PhotoPin> result = new ArrayList<>();
        for (PhotoPin pin : allPins) {
            if (pin.type.equalsIgnoreCase(currentFilter)) result.add(pin);
        }
        return result;
    }

    private float getColorForType(String type) {
        switch (type.toLowerCase()) {
            case "nature":     return BitmapDescriptorFactory.HUE_GREEN;
            case "monument":   return BitmapDescriptorFactory.HUE_ORANGE;
            case "musee":      return BitmapDescriptorFactory.HUE_VIOLET;
            case "restaurant": return BitmapDescriptorFactory.HUE_YELLOW;
            case "plage":      return BitmapDescriptorFactory.HUE_CYAN;
            case "rue":        return BitmapDescriptorFactory.HUE_ROSE;
            default:           return BitmapDescriptorFactory.HUE_AZURE;
        }
    }

    private List<PhotoPin> getSamplePins() {
        List<PhotoPin> list = new ArrayList<>();
        list.add(new PhotoPin("Tour Eiffel au coucher", "Marie L.",
                "Paris, France", "Avril 2024", 142, "Monument",
                new LatLng(48.8584, 2.2945)));
        list.add(new PhotoPin("Plage de Bondi", "Tom R.",
                "Sydney, Australie", "Janvier 2024", 98, "Plage",
                new LatLng(-33.8908, 151.2743)));
        list.add(new PhotoPin("Marche de Marrakech", "Sophie M.",
                "Marrakech, Maroc", "Mars 2024", 76, "Rue",
                new LatLng(31.6295, -7.9811)));
        list.add(new PhotoPin("Foret de Kyoto", "Kenji A.",
                "Kyoto, Japon", "Novembre 2023", 215, "Nature",
                new LatLng(35.0116, 135.7681)));
        list.add(new PhotoPin("Musee du Louvre", "Emma D.",
                "Paris, France", "Juin 2024", 310, "Musee",
                new LatLng(48.8606, 2.3376)));
        list.add(new PhotoPin("Montagne enneigee", "Pierre V.",
                "Chamonix, France", "Jan 2024", 129, "Nature",
                new LatLng(45.9237, 6.8694)));
        list.add(new PhotoPin("Sushi bar Shibuya", "Yuki T.",
                "Tokyo, Japon", "Dec 2023", 63, "Restaurant",
                new LatLng(35.6595, 139.7004)));
        list.add(new PhotoPin("Rue coloree", "Carlos M.",
                "Lisbonne, Portugal", "Mai 2024", 87, "Rue",
                new LatLng(38.7139, -9.1394)));
        list.add(new PhotoPin("Plage paradisiaque", "Sophie M.",
                "Maldives", "Mars 2024", 210, "Plage",
                new LatLng(3.2028, 73.2207)));
        list.add(new PhotoPin("Temple bouddhiste", "Kenji A.",
                "Kyoto, Japon", "Nov 2023", 178, "Monument",
                new LatLng(35.7148, 139.7967)));
        return list;
    }

    static class PhotoPin {
        String title, author, location, date, type;
        int likes;
        LatLng latLng;

        PhotoPin(String title, String author, String location,
                 String date, int likes, String type, LatLng latLng) {
            this.title    = title;
            this.author   = author;
            this.location = location;
            this.date     = date;
            this.likes    = likes;
            this.type     = type;
            this.latLng   = latLng;
        }
    }
}