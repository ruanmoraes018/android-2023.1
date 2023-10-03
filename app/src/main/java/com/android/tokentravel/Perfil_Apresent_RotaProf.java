package com.android.tokentravel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.tokentravel.dao.Dao;
import com.android.tokentravel.objetos.Motorista;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Perfil_Apresent_RotaProf extends AppCompatActivity {

    private ImageView imageView;
    private TextView nomeTextView, emailTextView, origemTextView, destinoTextView,
            horarioTextView, diasTextView, tipoVeiculoTextView, valorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_apresent_rota_prof);

        imageView = findViewById(R.id.imageMotorista);
        nomeTextView = findViewById(R.id.nomeMotorista);
        emailTextView = findViewById(R.id.emailMotorista);
        origemTextView = findViewById(R.id.id_origem);
        destinoTextView = findViewById(R.id.id_destino);
        horarioTextView = findViewById(R.id.id_Horario);
        diasTextView = findViewById(R.id.id_Dia);
        tipoVeiculoTextView = findViewById(R.id.id_TipoVeiculo);
        valorTextView = findViewById(R.id.id_Valor);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String emailDoMotoristaRota = sharedPreferences.getString("emailDoMotorista", "");
        String tipoVeiculo = sharedPreferences.getString("tipoVeiculo", "");
        String destinoRota = sharedPreferences.getString("destinoRota", "");
        String nomeDoMotoristaRota = sharedPreferences.getString("nomeDoMotorista", "");
        String horarioRota = sharedPreferences.getString("horarioRota", "");
        String valorRota = sharedPreferences.getString("valorRota", "");

        // Obtenha os extras da intent
        Intent intent = getIntent();
        if (intent != null) {
            String nomeMotorista = intent.getStringExtra("nomeMotorista");
            String emailMotorista = intent.getStringExtra("emailMotorista");
            String origem = intent.getStringExtra("origem");
            String destino = intent.getStringExtra("destino");
            String horario = intent.getStringExtra("horario");
            String dias = intent.getStringExtra("dias");
            String valor = intent.getStringExtra("valor");

            String diasDaSemanaString = sharedPreferences.getString("diasDaSemana", "");
            // Divida a string nas vírgulas para obter a lista de dias da semana
            List<String> diasDaSemana = Arrays.asList(diasDaSemanaString.split(","));

            // Defina os valores nos TextViews
            nomeTextView.setText(nomeDoMotoristaRota);
            emailTextView.setText(emailDoMotoristaRota);
            origemTextView.setText("Origem: " + origem);
            destinoTextView.setText("Destino: " + destino);
            horarioTextView.setText("Horário: " + horarioRota);
            diasTextView.setText("Dia(s): " + TextUtils.join(", ", diasDaSemana));
            tipoVeiculoTextView.setText("Tipo: " + tipoVeiculo);
            valorTextView.setText("R$: " + valorRota);

            // Carregue a imagem do motorista, se existir
            loadMotoristaImage();
        }
    }

    private void loadMotoristaImage() {
        // Carregue a imagem do motorista salva, se existir
        File file = new File(getFilesDir(), "profile_image_motora.jpg");
        if (file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

            // Aplicar a transformação de círculo à imagem usando Glide
            Glide.with(this)
                    .load(bitmap)
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(imageView);
        }
    }
}



