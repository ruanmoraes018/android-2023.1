package com.android.tokentravel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.android.tokentravel.dao.Dao;
import com.android.tokentravel.objetos.Motorista;
import com.android.tokentravel.objetos.Pessoa;


public class Form_Login extends AppCompatActivity {
    private TextView text_tela_cadastro;
    private Button btEntrar;

    EditText editTextEmail, editTextSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);
        IniciarComponentes();
        IniciarBotaoEntrar();

        editTextEmail = findViewById(R.id.edit_email);
        editTextSenha = findViewById(R.id.edit_senha);

        text_tela_cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Form_Login.this, FormCadastro.class);
                startActivity(intent);
            }
        });

        btEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String email = editTextEmail.getText().toString();
                    String senha = editTextSenha.getText().toString();

                    Dao dao = new Dao(getApplicationContext());
                    String tipoUsuario = dao.autenticarUsuario(email, senha);

                    if (tipoUsuario != null) {
                        if (tipoUsuario.equals("Passageiro")) {
                            Toast.makeText(getApplicationContext(), "Bem vindo, Passageiro!", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(Form_Login.this, Navigation_View.class);
                            startActivity(intent);

                            Pessoa nomeUsuario = dao.buscaInfoPassageiro(email);
                            String nomeDoPassageiroAutenticado = nomeUsuario.getPessoa_nome();

                            String emailDoPassageiroAutenticado = email;

                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("nomeDoPassageiroLogado", nomeDoPassageiroAutenticado);
                            editor.putString("emailDoPassageiroLogado", emailDoPassageiroAutenticado);
                            editor.apply();
                        } else if (tipoUsuario.equals("Motorista")) {
                            Toast.makeText(getApplicationContext(), "Bem vindo, Motorista!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Form_Login.this, Tela_principal_motorista.class);
                            startActivity(intent);

                            int idDoMotoristaAutenticado = dao.buscaIdMotorista(email);

                            Pessoa nomeUsuario = dao.buscaInfoMotorista(email);
                            String nomeDoMotoristaAutenticado = nomeUsuario.getPessoa_nome();

                            String emailDoMotoristaAutenticado = email;

                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("nomeDoMotoristaLogado", nomeDoMotoristaAutenticado);
                            editor.putString("emailDoMotoristaLogado", emailDoMotoristaAutenticado);
                            editor.putInt("idDoMotoristaLogado", idDoMotoristaAutenticado);
                            editor.apply();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Email/Senha inválidos!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Ocorreu um erro. Por favor, tente novamente.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void IniciarComponentes(){
        text_tela_cadastro = findViewById(R.id.text_tela_cadastro);
    }

    private void IniciarBotaoEntrar(){
        btEntrar = findViewById(R.id.bt_entrar);
    }
}