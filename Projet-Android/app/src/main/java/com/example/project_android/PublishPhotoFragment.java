package com.example.project_android;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class PublishPhotoFragment extends Fragment {

    private ImageView ivPreview;
    private EditText etTitle, etLocation, etDescription;
    private Uri selectedImageUri = null;

    // Launcher galerie
    private final ActivityResultLauncher<Intent> galleryLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    ivPreview.setImageURI(selectedImageUri);
                    ivPreview.setVisibility(View.VISIBLE);
                }
            });

    // Launcher camera
    private final ActivityResultLauncher<Intent> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Bundle extras = result.getData().getExtras();
                    if (extras != null) {
                        android.graphics.Bitmap bitmap = (android.graphics.Bitmap) extras.get("data");
                        ivPreview.setImageBitmap(bitmap);
                        ivPreview.setVisibility(View.VISIBLE);
                    }
                }
            });

    // Launcher permission camera
    private final ActivityResultLauncher<String> cameraPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                if (granted) openCamera();
                else Toast.makeText(getContext(), "Permission camera refusee", Toast.LENGTH_SHORT).show();
            });

    // Launcher permission galerie
    private final ActivityResultLauncher<String> galleryPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                if (granted) openGallery();
                else Toast.makeText(getContext(), "Permission galerie refusee", Toast.LENGTH_SHORT).show();
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_publish_photo, container, false);

        ivPreview      = view.findViewById(R.id.iv_preview);
        etTitle        = view.findViewById(R.id.et_photo_title);
        etLocation     = view.findViewById(R.id.et_photo_location);
        etDescription  = view.findViewById(R.id.et_photo_description);
        Button btnGallery = view.findViewById(R.id.btn_pick_gallery);
        Button btnCamera  = view.findViewById(R.id.btn_take_photo);
        Button btnPublish = view.findViewById(R.id.btn_publish_photo);
        Button btnRetour  = view.findViewById(R.id.btn_publish_retour);

        btnGallery.setOnClickListener(v -> checkGalleryPermission());
        btnCamera.setOnClickListener(v -> checkCameraPermission());

        btnPublish.setOnClickListener(v -> {
            String title    = etTitle.getText().toString().trim();
            String location = etLocation.getText().toString().trim();
            String desc     = etDescription.getText().toString().trim();

            if (title.isEmpty()) {
                etTitle.setError("Titre requis");
                return;
            }
            if (location.isEmpty()) {
                etLocation.setError("Lieu requis");
                return;
            }
            if (selectedImageUri == null) {
                Toast.makeText(getContext(),
                        "Veuillez choisir ou prendre une photo",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Sauvegarder dans le profil utilisateur
            MainActivity activity = (MainActivity) requireActivity();
            if (activity.isLoggedIn()) {
                PhotoModel newPhoto = new PhotoModel(
                        title,
                        activity.getLoggedUserName(),
                        location,
                        new java.text.SimpleDateFormat("MMM yyyy",
                                java.util.Locale.FRANCE).format(new java.util.Date()),
                        android.R.drawable.ic_menu_gallery,
                        0);
                UserRepository.getInstance()
                        .addPhotoForUser(activity.getLoggedUserName(), newPhoto);
            }

            Toast.makeText(getContext(),
                    "Photo \"" + title + "\" publiee !", Toast.LENGTH_SHORT).show();

            requireActivity().getSupportFragmentManager().popBackStack();
        });

        btnRetour.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        return view;
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void checkGalleryPermission() {
        String permission = android.os.Build.VERSION.SDK_INT >= 33
                ? Manifest.permission.READ_MEDIA_IMAGES
                : Manifest.permission.READ_EXTERNAL_STORAGE;

        if (ContextCompat.checkSelfPermission(requireContext(), permission)
                == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            galleryPermissionLauncher.launch(permission);
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        galleryLauncher.launch(intent);
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            cameraLauncher.launch(intent);
        } else {
            Toast.makeText(getContext(), "Camera non disponible", Toast.LENGTH_SHORT).show();
        }
    }
}