package com.android.tokentravel;


import static android.app.Activity.RESULT_OK;
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MenuMotoraFragment extends Fragment {

    private ImageView imageView;
    private Button button;
    private static final int REQUEST_CAMERA_CAPTURE = 101;
    private static final int REQUEST_GALLERY_PICK = 102;
    private static final int CAMERA_PERMISSION_REQUEST = 103;
    private static final int STORAGE_PERMISSION_REQUEST = 104;
    private static final String IMAGE_FILE_NAME = "profile_image_motora.jpg"; // Nome do arquivo de imagem

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_motora, container, false);

        imageView = view.findViewById(R.id.fotoperfil2);
        button = view.findViewById(R.id.btnfoto2);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String nomeDoMotorista = sharedPreferences.getString("nomeDoUsuarioLogado", "");
        String emailDoMotorista = sharedPreferences.getString("emailDoUsuarioLogado", "");

        TextView nomeDoMotoristaTextView = view.findViewById(R.id.nomeDoMotoristaTextView);
        TextView emailDoMotoristaTextView = view.findViewById(R.id.emailDoMotoristaTextView);

        nomeDoMotoristaTextView.setText(nomeDoMotorista);
        emailDoMotoristaTextView.setText(emailDoMotorista);

        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickerDialog();
            }
        });

        // Carregue a imagem salva, se existir
        loadSavedImage();

        return view;
    }

    private void showImagePickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Escolha uma opção");
        String[] options = {"Tirar uma foto", "Escolher da galeria", "Cancelar"};
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        // Tirar uma foto com a câmera
                        checkCameraPermission();
                        break;
                    case 1:
                        // Escolher da galeria
                        checkStoragePermission();
                        break;
                    case 2:
                        // Cancelar
                        dialog.dismiss();
                        break;
                }
            }
        });
        builder.show();
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCameraCapture();
        } else {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
        }
    }

    private void checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openGalleryPicker();
        } else {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUEST);
        }
    }

    private void startCameraCapture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA_CAPTURE);
    }

    private void openGalleryPicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALLERY_PICK);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCameraCapture();
            }
        } else if (requestCode == STORAGE_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGalleryPicker();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA_CAPTURE) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                saveImage(bitmap); // Salve a imagem capturada
                displaySelectedImage(bitmap);
            } else if (requestCode == REQUEST_GALLERY_PICK) {
                if (data != null && data.getData() != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                                requireActivity().getContentResolver(), data.getData());
                        saveImage(bitmap); // Salve a imagem selecionada da galeria
                        displaySelectedImage(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void displaySelectedImage(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
        // Usar Glide para carregar a imagem na ImageView e aplicar a transformação de círculo
        Glide.with(this)
                .load(bitmap)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(imageView);
    }

    private void saveImage(Bitmap bitmap) {
        // Salvar a imagem no armazenamento interno do dispositivo
        File file = new File(requireContext().getFilesDir(), IMAGE_FILE_NAME);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadSavedImage() {
        // Carregar a imagem salva, se existir
        File file = new File(requireContext().getFilesDir(), IMAGE_FILE_NAME);
        if (file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            displaySelectedImage(bitmap);
        }
    }
}
