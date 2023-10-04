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
import android.util.Base64;
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

import com.android.tokentravel.dao.Dao;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MenuMotoraFragment extends Fragment {

    private ImageView imageView;
    private Button button;
    private static final int REQUEST_CAMERA_CAPTURE = 101;
    private static final int REQUEST_GALLERY_PICK = 102;
    private static final int CAMERA_PERMISSION_REQUEST = 103;
    private static final int STORAGE_PERMISSION_REQUEST = 104;

    private Dao Dao;

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

        Dao = new Dao(getContext());

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
                String imageString = convertBitmapToString(bitmap);

                // Chame a função salvarFoto com o ID da pessoa apropriado e a string da imagem
                int idPessoa = obterIdDaPessoa();
                if (idPessoa != -1) {
                    boolean sucesso = Dao.salvarFoto(idPessoa, imageString);
                    if (sucesso) {
                        displaySelectedImage(bitmap);
                    }
                }
            } else if (requestCode == REQUEST_GALLERY_PICK) {
                if (data != null && data.getData() != null) {
                    try {
                        Bitmap originalBitmap = MediaStore.Images.Media.getBitmap(
                                requireActivity().getContentResolver(), data.getData());
                        Bitmap resizedBitmap = resizeBitmap(originalBitmap, 800, 800); // Defina o tamanho máximo desejado

                        String imageString = convertBitmapToString(resizedBitmap);

                        // Chame a função salvarFoto com o ID da pessoa apropriado e a string da imagem
                        int idPessoa = obterIdDaPessoa();
                        if (idPessoa != -1) {
                            boolean sucesso = Dao.salvarFoto(idPessoa, imageString);
                            if (sucesso) {
                                displaySelectedImage(resizedBitmap);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private String convertBitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private Bitmap resizeBitmap(Bitmap originalBitmap, int maxWidth, int maxHeight) {
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();

        float aspectRatio = (float) width / (float) height;

        if (width > maxWidth || height > maxHeight) {
            if (aspectRatio > 1) {
                width = maxWidth;
                height = (int) (width / aspectRatio);
            } else {
                height = maxHeight;
                width = (int) (height * aspectRatio);
            }
        }

        return Bitmap.createScaledBitmap(originalBitmap, width, height, true);
    }

    private int obterIdDaPessoa() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        return sharedPreferences.getInt("idDoMotoristaLogado", -1);
    }

    private void displaySelectedImage(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
        Glide.with(this)
                .load(bitmap)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(imageView);
    }

    private void loadSavedImage() {
        int idPessoa = obterIdDaPessoa();
        if (idPessoa != -1) {
            // Obtenha a string Base64 da imagem do banco de dados
            String imageString = Dao.obterFoto(idPessoa);

            if (imageString != null && !imageString.isEmpty()) {
                // Decodifique a string Base64 em um array de bytes
                byte[] decodedBytes = Base64.decode(imageString, Base64.DEFAULT);

                // Crie um objeto Bitmap a partir do array de bytes decodificado
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

                // Exiba o objeto Bitmap no ImageView
                displaySelectedImage(bitmap);
            }
        }
    }
}

