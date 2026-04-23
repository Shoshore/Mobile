package com.example.project_android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CacheSettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cache_settings, container, false);

        CacheManager cache = CacheManager.getInstance(requireContext());

        TextView tvStatut    = view.findViewById(R.id.tv_cache_statut);
        TextView tvInfo      = view.findViewById(R.id.tv_cache_info);
        Button btnViderCache = view.findViewById(R.id.btn_vider_cache);
        Button btnRetour     = view.findViewById(R.id.btn_cache_retour);

        // Statut connexion
        boolean online = CacheManager.isOnline(requireContext());
        tvStatut.setText(online ? "🟢 En ligne" : "🔴 Hors-ligne");
        tvStatut.setTextColor(online ? 0xFF43A047 : 0xFFE53935);

        // Infos cache
        StringBuilder info = new StringBuilder();
        info.append(cache.getCacheInfo()).append("\n");
        info.append(cache.hasCachedPhotos()
                ? "✅ Photos en cache\n" : "❌ Pas de photos en cache\n");
        info.append(cache.hasCachedParcours()
                ? "✅ Parcours en cache\n" : "❌ Pas de parcours en cache\n");
        info.append(cache.isCacheValid()
                ? "✅ Cache valide (< 24h)" : "⚠️ Cache expire (> 24h)");
        tvInfo.setText(info.toString());

        btnViderCache.setOnClickListener(v -> {
            cache.clearCache();
            Toast.makeText(getContext(), "Cache vide !", Toast.LENGTH_SHORT).show();
            tvInfo.setText("Cache vide");
        });

        btnRetour.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        return view;
    }
}