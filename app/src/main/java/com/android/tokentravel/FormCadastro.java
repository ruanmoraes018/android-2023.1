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
                View view = super.getDropDownView(position, convertView, parent);
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
                    Intent intent = new Intent(FormCadastro.this, Form_Login.class);
                    startActivity(intent);
                    cadastrar();
                }
            });
        } else {
            onRestart();
        }
    }

    public void cadastrar(){
        String nome = editTextNome.getText().toString();
        String email = editTextEmail.getText().toString();
        String senha = editTextSenha.getText().toString();
        String tipo = spinnerTipo.getSelectedItem().toString();

        Pessoa pessoa = new Pessoa();
        pessoa.setPessoa_nome(nome);
        pessoa.setPessoa_email(email);
        pessoa.setPessoa_senha(senha);
        pessoa.setPessoa_tipo(tipo);

        Dao dao = new Dao(this);
        String resultado = dao.inserirPessoa(pessoa);
        Log.d("Resultado: ", resultado);
    }
}