package com.android.tokentravel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelecaoActivity extends AppCompatActivity {
    private Button btRotas, btLogin;

    private SQLiteDatabase bancoDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecao);
        IniciarBotaoRotas();
        IniciarBotaoLogin();

        criarBancoDados();
        

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
    public void criarBancoDados(){
        try {
            bancoDados = openOrCreateDatabase("crudTokenTravel", MODE_PRIVATE, null);
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS usuario(" +
                    " id INTEGER PRIMARY KEY AUTOINCREMENT" +
                    ", nome VARCHAR, " +
                    "email VARCHAR, " +
                    "senha VARCHAR," +
                    "tipo VARCHAR)");
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    private void IniciarBotaoRotas(){
        btRotas = findViewById(R.id.bt_rotas);
    }

    private void IniciarBotaoLogin(){
        btLogin = findViewById(R.id.bt_login);
    }
}