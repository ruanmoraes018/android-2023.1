package com.android.tokentravel;

import static android.app.Activity.RESULT_OK;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.Manifest;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.tokentravel.dao.Dao;
import com.android.tokentravel.objetos.Pessoa;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

public class MenuFragment extends Fragment {

    private ImageView imageView;
    private Button button;

    TextView nomeDoPassageiroTextView, emailDoPassageiroTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        imageView = view.findViewById(R.id.fotoperfil);
        button = view.findViewById(R.id.btnfoto);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String nomeDoPassageiro = sharedPreferences.getString("nomeDoPassageiroLogado", "");
        String emailDoPassageiro = sharedPreferences.getString("emailDoPassageiroLogado", "");

        TextView nomeDoPassageiroTextView = view.findViewById(R.id.nomeDoPassageiroTextView);
        TextView emailDoPassageiroTextView = view.findViewById(R.id.emailDoPassageiroTextView);

        nomeDoPassageiroTextView.setText(nomeDoPassageiro);
        emailDoPassageiroTextView.setText(emailDoPassageiro);

        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, 101);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 101);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
            // Usar Glide para carregar a imagem na ImageView e aplicar a transformação de círculo
            Glide.with(this)
                    .load(bitmap)
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(imageView);
        }
    }
}
