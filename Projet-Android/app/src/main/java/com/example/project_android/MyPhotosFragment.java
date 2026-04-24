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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MyPhotosFragment extends Fragment {

    private List<PhotoModel> myPhotos;
    private MyPhotosAdapter adapter;
    private TextView tvEmpty;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_photos, container, false);

        MainActivity activity = (MainActivity) requireActivity();
        String userName = activity.getLoggedUserName();

        TextView tvTitre  = view.findViewById(R.id.tv_my_photos_titre);
        TextView tvStats  = view.findViewById(R.id.tv_my_photos_stats);
        tvEmpty           = view.findViewById(R.id.tv_my_photos_empty);
        Button btnPublier = view.findViewById(R.id.btn_my_photos_publier);
        Button btnRetour  = view.findViewById(R.id.btn_my_photos_retour);

        tvTitre.setText("📸 Photos de " + userName);

        myPhotos = UserRepository.getInstance().getPhotosForUser(userName);
        updateStats(tvStats);
        updateEmpty();

        RecyclerView recycler = view.findViewById(R.id.recycler_my_photos);
        recycler.setLayoutManager(new GridLayoutManager(getContext(), 2));

        adapter = new MyPhotosAdapter(myPhotos,
                new MyPhotosAdapter.OnMyPhotoActionListener() {
                    @Override
                    public void onVoir(PhotoModel photo) {
                        PhotoDetailFragment detail =
                                PhotoDetailFragment.newInstance(photo);
                        requireActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, detail)
                                .addToBackStack(null)
                                .commit();
                    }

                    @Override
                    public void onSupprimer(int index) {
                        UserRepository.getInstance()
                                .removePhotoForUser(userName, index);
                        adapter.notifyItemRemoved(index);
                        updateStats(tvStats);
                        updateEmpty();
                        Toast.makeText(getContext(),
                                "Photo supprimee", Toast.LENGTH_SHORT).show();
                    }
                });

        recycler.setAdapter(adapter);

        btnPublier.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new PublishPhotoFragment())
                        .addToBackStack(null)
                        .commit()
        );

        btnRetour.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        return view;
    }

    private void updateStats(TextView tvStats) {
        int nb = myPhotos.size();
        tvStats.setText(nb + " photo" + (nb > 1 ? "s" : "") + " publiee"
                + (nb > 1 ? "s" : ""));
    }

    private void updateEmpty() {
        tvEmpty.setVisibility(myPhotos.isEmpty() ? View.VISIBLE : View.GONE);
    }
}