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
import com.android.tokentravel.objetos.Motorista;
import com.android.tokentravel.objetos.Passageiro;
import com.android.tokentravel.objetos.Pessoa;


public class FormCadastro extends AppCompatActivity {
    EditText editTextNome, editTextCPF, editTextEmail, editTextSenha;
    Spinner spinnerTipo;
    Button botao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro);
        botao = (Button) findViewById(R.id.bt_cadastrar);

        editTextNome = findViewById(R.id.edit_nome);
        editTextCPF = findViewById(R.id.edit_cpf);
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
                    String tipo = spinnerTipo.getSelectedItem().toString();
                    Dao dao = new Dao(getApplicationContext());
                    String resultadoEmail;

                    try {
                        resultadoEmail = dao.buscaPessoaemail(email);
                    } catch (Exception e) {
                        Log.e("Erro", e.getMessage());
                        Toast.makeText(getApplicationContext(), "Erro ao verificar o email.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Verifica se o CPF já está cadastrado
                    String cpf = editTextCPF.getText().toString();
                    String resultadoCPF;

                    try {
                        resultadoCPF = dao.buscaPessoaCPF(cpf);
                    } catch (Exception e) {
                        Log.e("Erro", e.getMessage());
                        Toast.makeText(getApplicationContext(), "Erro ao verificar o CPF.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String resultadoTipo;

                    try {
                        resultadoTipo = dao.buscaPessoaTipo(tipo);
                    } catch (Exception e) {
                        Log.e("Erro", e.getMessage());
                        Toast.makeText(getApplicationContext(), "Erro ao verificar o Tipo.", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    if (resultadoEmail != null) {
                        // O email já está cadastrado
                        Toast.makeText(getApplicationContext(), "O email já está cadastrado.", Toast.LENGTH_SHORT).show();
                    } else if (resultadoCPF != null) {
                        // O CPF já está cadastrado
                        Toast.makeText(getApplicationContext(), "O CPF já está cadastrado.", Toast.LENGTH_SHORT).show();

                    } else if (resultadoTipo != null) {
                        // O Tipo já está cadastrado
                        Toast.makeText(getApplicationContext(), "Tipo já definido neste usuário.", Toast.LENGTH_SHORT).show();

                    } else {
                        if (tipo.equals("Passageiro")) {
                            cadastrarPassageiro();
                        } else if (tipo.equals("Motorista")) {
                            cadastrarMotorista();
                        }
                    }
                }
            });
        } else {
            onRestart();
        }
    }
    private void cadastrarPassageiro(){
        String nome = editTextNome.getText().toString();
        String cpf = editTextCPF.getText().toString();
        String email = editTextEmail.getText().toString();
        String senha = editTextSenha.getText().toString();
        String tipo = spinnerTipo.getSelectedItem().toString();
        int numero = 0;

        if(TextUtils.isEmpty(nome)){
            Toast.makeText(this, "O nome é obrigatório.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(cpf)){
            Toast.makeText(this, "O cpf é obrigatório.", Toast.LENGTH_SHORT).show();
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

        Passageiro passageiro = new Passageiro(nome, cpf, email, senha, tipo, numero);
//        pessoa.setPessoa_nome(nome);
//        pessoa.setPessoa_cpf(cpf);
//        pessoa.setPessoa_email(email);
//        pessoa.setPessoa_senha(senha);
//        pessoa.setPessoa_tipo(tipo);

        Dao dao = new Dao(this);
        String resultado = dao.inserirPessoa(passageiro);
        Log.d("Resultado: ", resultado);

        Intent intent = new Intent(this, Form_Login.class);
        startActivity(intent);
    }
    private void cadastrarMotorista(){
        String nome = editTextNome.getText().toString();
        String cpf = editTextCPF.getText().toString();
        String email = editTextEmail.getText().toString();
        String senha = editTextSenha.getText().toString();
        String tipo = spinnerTipo.getSelectedItem().toString();
        String cnh = "";

        if(TextUtils.isEmpty(nome)){
            Toast.makeText(this, "O nome é obrigatório.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(cpf)){
            Toast.makeText(this, "O cpf é obrigatório.", Toast.LENGTH_SHORT).show();
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

        Motorista motorista = new Motorista(nome, cpf, email, senha, tipo, cnh);
//        pessoa.setPessoa_nome(nome);
//        pessoa.setPessoa_cpf(cpf);
//        pessoa.setPessoa_email(email);
//        pessoa.setPessoa_senha(senha);
//        pessoa.setPessoa_tipo(tipo);

        Dao dao = new Dao(this);
        String resultado = dao.inserirPessoa(motorista);
        Log.d("Resultado: ", resultado);

        Intent intent = new Intent(this, Form_Login.class);
        startActivity(intent);
    }

}