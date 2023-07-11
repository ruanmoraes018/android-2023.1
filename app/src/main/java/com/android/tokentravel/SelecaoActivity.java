package com.android.tokentravel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelecaoActivity extends AppCompatActivity {
    private Button btRotas, btLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecao);
        IniciarBotaoRotas();
        IniciarBotaoLogin();

        btRotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelecaoActivity.this, PesquisaRota.class);
                startActivity(intent);
            }
        });

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelecaoActivity.this, Form_Login.class);
                startActivity(intent);
            }
        });
    }

    private void IniciarBotaoRotas(){
        btRotas = findViewById(R.id.bt_rotas);
    }

    private void IniciarBotaoLogin(){
        btLogin = findViewById(R.id.bt_login);
    }
}