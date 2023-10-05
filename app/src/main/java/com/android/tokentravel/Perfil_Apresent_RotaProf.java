package com.android.tokentravel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.tokentravel.dao.Dao;
import com.android.tokentravel.objetos.Motorista;
import com.android.tokentravel.objetos.Rotas;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

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

        // Obtenha o motoristaId e numeroRota da Intent
        Intent intent = getIntent();
        if (intent != null) {
            int motoristaId = intent.getIntExtra("id_motoristaagora", -1);
            int numeroRota = intent.getIntExtra("numeroRota", -1);

            Log.d("Adapter_motoristas", "ID do motorista: " + motoristaId);
            Log.d("Adapter_motoristas", "ID da rota: " + numeroRota);

            if (motoristaId != -1 && numeroRota != -1) {
                // Crie uma instância do seu Dao
                Dao dao = new Dao(this);
                Rotas rota = dao.buscaRotaPorId(motoristaId, numeroRota);

                // Chame o método buscaMotoristaPorId para obter os dados do motorista
                Motorista motorista = dao.buscaMotoristaPorId(motoristaId);

                if (motorista != null) {
                    // Defina os valores nos TextViews
                    nomeTextView.setText(motorista.getPessoa_nome());
                    emailTextView.setText(motorista.getPessoa_email());
                    origemTextView.setText("Origem: " + rota.getOrigem());
                    destinoTextView.setText("Destino: " + rota.getDestino());
                    horarioTextView.setText("Horário: " + rota.getHorario());
                    diasTextView.setText("Dia(s): " + TextUtils.join(", ", rota.getDiasAtivos()));
                    tipoVeiculoTextView.setText("Tipo: " + rota.getTipo());
                    valorTextView.setText("R$: " + rota.getValor());

                    // Obtenha a imagem passada através da Intent como um ByteArray
                    byte[] motoristaImageByteArray = intent.getByteArrayExtra("motoristaImage");
                    if (motoristaImageByteArray != null) {
                        Bitmap motoristaImage = BitmapFactory.decodeByteArray(motoristaImageByteArray, 0, motoristaImageByteArray.length);
                        // Aplicar a transformação de círculo à imagem usando Glide
                        Glide.with(this)
                                .load(motoristaImage)
                                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                                .into(imageView);
                    }
                }
            }
        }
    }
}
