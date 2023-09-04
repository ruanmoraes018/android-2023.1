package com.android.tokentravel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tokentravel.dao.Dao;
import com.android.tokentravel.objetos.Pessoa;


public class FormCadastro extends AppCompatActivity {
    EditText editTextNome, editTextEmail, editTextSenha;
    Spinner spinnerTipo;
    Button botao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro);
        botao = (Button) findViewById(R.id.bt_cadastrar);

        editTextNome = findViewById(R.id.edit_nome);
        editTextEmail = findViewById(R.id.edit_email);
        editTextSenha = findViewById(R.id.edit_senha);
        spinnerTipo = findViewById(R.id.spinner_user_type);

        String[] userTypes = {"Passageiro", "Motorista"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, userTypes) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.BLACK); // Defina a cor do texto aqui
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.BLUE); // Defina a cor do texto aqui para o drop-down
                return view;
            }
        };
        spinnerTipo.setAdapter(adapter);

        Button button = findViewById(R.id.bt_cadastrar);

        if (button != null) {
            botao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // Verifica se o email já está cadastrado
                    String email = editTextEmail.getText().toString();
                    Dao dao = new Dao(getApplicationContext());
                    String resultado;
                    try {
                        resultado = dao.buscaPessoa(email);
                    } catch (Exception e) {
                        Log.e("Erro", e.getMessage());
                        Toast.makeText(getApplicationContext(), "Erro ao verificar o email.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (resultado != null) {
                        // O email já está cadastrado
                        Toast.makeText(getApplicationContext(), "O email já está cadastrado.", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        // O email não está cadastrado
                        cadastrar();
                    }
                }
            });

        } else {
            onRestart();
        }
    }
    private void cadastrar(){
        String nome = editTextNome.getText().toString();
        String email = editTextEmail.getText().toString();
        String senha = editTextSenha.getText().toString();
        String tipo = spinnerTipo.getSelectedItem().toString();

        if(TextUtils.isEmpty(nome)){
            Toast.makeText(this, "O nome é obrigatório.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "O e-mail é obrigatório.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(senha)){
            Toast.makeText(this, "A senha é obrigatória.", Toast.LENGTH_SHORT).show();
            return;
        }

        Pessoa pessoa = new Pessoa();
        pessoa.setPessoa_nome(nome);
        pessoa.setPessoa_email(email);
        pessoa.setPessoa_senha(senha);
        pessoa.setPessoa_tipo(tipo);

        Dao dao = new Dao(this);
        String resultado = dao.inserirPessoa(pessoa);
        Log.d("Resultado: ", resultado);

        Intent intent = new Intent(this, Form_Login.class);
        startActivity(intent);
    }

}