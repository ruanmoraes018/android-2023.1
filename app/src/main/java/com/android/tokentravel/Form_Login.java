package com.android.tokentravel;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import com.android.tokentravel.dao.Dao;
import com.android.tokentravel.objetos.Pessoa;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Form_Login extends AppCompatActivity {
    private TextView text_tela_cadastro;
    private Button btEntrar;
    EditText editTextEmail, editTextSenha;

    public boolean emailPreenchido = false, senhaVisivel = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);
        IniciarComponentes();
        IniciarBotaoEntrar();

        editTextEmail = findViewById(R.id.edit_email);
        editTextSenha = findViewById(R.id.edit_senha);

        ImageView imageViewEye; // Ícone de olho
        imageViewEye = findViewById(R.id.imageViewEye);

        text_tela_cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Form_Login.this, FormCadastro.class);
                startActivity(intent);
            }
        });
        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String email = s.toString().trim();
                if (isValidEmail(email)) {
                    emailPreenchido = true;
                } else {
                    emailPreenchido = false;
                }
            }
        });
        imageViewEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                senhaVisivel = !senhaVisivel;
                if (senhaVisivel) {
                    editTextSenha.setInputType(InputType.TYPE_CLASS_TEXT);
                } else {
                    editTextSenha.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                editTextSenha.setSelection(editTextSenha.getText().length());
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

                            String email1 = editTextEmail.getText().toString().trim();
                            if (!isValidEmail(email1)) {
                                Toast.makeText(getApplicationContext(), "Email inválido", Toast.LENGTH_SHORT).show();
                                return;
                            }

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

                            String email1 = editTextEmail.getText().toString().trim();
                            if (!isValidEmail(email1)) {
                                Toast.makeText(getApplicationContext(), "Email inválido", Toast.LENGTH_SHORT).show();
                                return;
                            }
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
    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    private void IniciarComponentes(){
        text_tela_cadastro = findViewById(R.id.text_tela_cadastro);
    }

    private void IniciarBotaoEntrar(){
        btEntrar = findViewById(R.id.bt_entrar);
    }
}