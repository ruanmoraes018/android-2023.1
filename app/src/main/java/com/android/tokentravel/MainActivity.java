package com.android.tokentravel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 3000; // 3 segundos
    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String authToken = sharedPreferences.getString("authToken", null);

                Intent intent;

                if (authToken != null) {
                    // Token de autenticação encontrado, o usuário está logado
                    String userType = sharedPreferences.getString("tipoDaPessoaLogada", "");

                    if ("Passageiro".equals(userType)) {
                        intent = new Intent(MainActivity.this, Navigation_View.class);
                    } else if ("Motorista".equals(userType)) {
                        intent = new Intent(MainActivity.this, Tela_principal_motorista.class);
                    } else {
                        intent = new Intent(MainActivity.this, SelecaoActivity.class);
                    }
                } else {
                    // Nenhum token de autenticação encontrado, o usuário precisa fazer login
                    intent = new Intent(MainActivity.this, SelecaoActivity.class);
                }

                startActivity(intent);
                finish(); // Encerre a tela de splash para evitar que o usuário retorne a ela
            }
        }, SPLASH_DELAY);
    }
}

