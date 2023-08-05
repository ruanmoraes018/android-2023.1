package com.android.tokentravel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class FormCadastro extends AppCompatActivity {
    EditText editTextNome, editTextEmail, editTextSenha;
    Spinner spinnerTipoUsuario;
    Button botao;
    SQLiteDatabase bancoDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro);

        editTextNome = (EditText) findViewById(R.id.edit_nome);
        editTextEmail = (EditText) findViewById(R.id.edit_email);
        editTextSenha = (EditText) findViewById(R.id.edit_senha);
        spinnerTipoUsuario = (Spinner) findViewById(R.id.spinner_user_type);
        botao = (Button) findViewById(R.id.bt_cadastrar);

        Spinner spinnerUserType = findViewById(R.id.spinner_user_type);

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
        spinnerUserType.setAdapter(adapter);

        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrar();
            }
        });
    }

    public void cadastrar(){
        if(!TextUtils.isEmpty(editTextNome.getText().toString())){
            try {
                bancoDados = openOrCreateDatabase("crudTokenTravel", MODE_PRIVATE, null);
                String sql = "INSERT INTO usuario (nome, email, senha, tipo) VALUES (?,?,?,?)";
                SQLiteStatement stmt = bancoDados.compileStatement(sql);
                stmt.bindString(1,editTextNome.getText().toString());
                stmt.bindString(2,editTextEmail.getText().toString());
                stmt.bindString(3,editTextSenha.getText().toString());
                stmt.bindString(4,spinnerTipoUsuario.getSelectedItem().toString());
                stmt.executeInsert();
                bancoDados.close();
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}