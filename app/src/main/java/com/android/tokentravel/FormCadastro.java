package com.android.tokentravel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tokentravel.dao.Dao;
import com.android.tokentravel.objetos.Motorista;
import com.android.tokentravel.objetos.Passageiro;

public class FormCadastro extends AppCompatActivity {

    View containnermotora;
    EditText editTextNome, editTextCPF, editTextEmail, editTextSenha, editTextTelefone, editTextCnh, editTextModeloCarro, editTextPlacaVeiculo;
    Spinner spinnerTipo;
    Button botao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro);

        botao = findViewById(R.id.bt_cadastrar);

        editTextNome = findViewById(R.id.edit_nome);
        editTextCPF = findViewById(R.id.edit_cpf);
        editTextEmail = findViewById(R.id.edit_email);
        editTextSenha = findViewById(R.id.edit_senha);
        spinnerTipo = findViewById(R.id.spinner_user_type);
        editTextTelefone = findViewById(R.id.edit_telefone);
        editTextCnh = findViewById(R.id.edit_cnh);
        editTextModeloCarro = findViewById(R.id.edit_modeloCarro);
        editTextPlacaVeiculo = findViewById(R.id.edit_placaVeiculo);
        containnermotora = findViewById(R.id.containerComponentsmotora);

        spinnerTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // strm o tipo de pessoa selecionado
                String tipoPessoa = parent.getItemAtPosition(position).toString();

                if (tipoPessoa.equals("Motorista")) {
                    // Anima a exibição dos campos adicionais para Motorista
                    animateViewVisibility(editTextCnh, View.VISIBLE);
                    animateViewVisibility(editTextModeloCarro, View.VISIBLE);
                    animateViewVisibility(editTextPlacaVeiculo, View.VISIBLE);
                    animateViewVisibility(containnermotora, View.VISIBLE);
                } else {
                    // Anima a ocultação dos campos adicionais para Passageiro
                    animateViewVisibility(editTextCnh, View.GONE);
                    animateViewVisibility(editTextModeloCarro, View.GONE);
                    animateViewVisibility(editTextPlacaVeiculo, View.GONE);
                    animateViewVisibility(containnermotora, View.GONE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        String[] userTypes = {"Passageiro", "Motorista"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, userTypes) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.BLACK); // Defina a cor do texto aqui
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.BLUE); // Defina a cor do texto aqui para o drop-down
                return view;
            }
        };
        spinnerTipo.setAdapter(adapter);

        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Verifica se o email já está cadastrado
                String email = editTextEmail.getText().toString();
                String tipo = spinnerTipo.getSelectedItem().toString();
                Dao dao = new Dao(getApplicationContext());
                String resultadoEmail;

                try {
                    resultadoEmail = dao.buscaPessoaEmail(email);
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

                if (resultadoEmail != null) {
                    // O email já está cadastrado
                    Toast.makeText(getApplicationContext(), "O email já está cadastrado.", Toast.LENGTH_SHORT).show();
                } else if (resultadoCPF != null) {
                    // O CPF já está cadastrado
                    Toast.makeText(getApplicationContext(), "O CPF já está cadastrado.", Toast.LENGTH_SHORT).show();

                } else {
                    String tipoPessoa = spinnerTipo.getSelectedItem().toString();
                    View containerComponents = findViewById(R.id.containerComponents);
                    if (tipo.equals("Passageiro")) {
                        containerComponents.getLayoutParams().height = 380;
                        containerComponents.requestLayout();
                        cadastrarPassageiro();
                    } else if (tipo.equals("Motorista")) {
                        containerComponents.getLayoutParams().height = 720;
                        containerComponents.requestLayout();
                        cadastrarMotorista();
                    }
                }
            }


        });
    }

    private void animateViewVisibility(View view, int visibility) {
        view.setVisibility(visibility);
        Animation animation = visibility == View.VISIBLE ?
                AnimationUtils.loadAnimation(this, R.anim.slide_down) :
                AnimationUtils.loadAnimation(this, R.anim.slide_up);
        view.startAnimation(animation);
    }

    private void cadastrarPassageiro() {
        String nome = editTextNome.getText().toString();
        String cpf = editTextCPF.getText().toString();
        String email = editTextEmail.getText().toString();
        String senha = editTextSenha.getText().toString();
        String tipo = spinnerTipo.getSelectedItem().toString();
        String telefone = "";

        if (TextUtils.isEmpty(nome)) {
            Toast.makeText(this, "O nome é obrigatório.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(cpf)) {
            Toast.makeText(this, "O cpf é obrigatório.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "O e-mail é obrigatório.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(senha)) {
            Toast.makeText(this, "A senha é obrigatória.", Toast.LENGTH_SHORT).show();
            return;
        }

        Passageiro passageiro = new Passageiro(nome, cpf, email, senha,  telefone, tipo);

        Dao dao = new Dao(this);
        String resultado = dao.inserirPassageiro(passageiro);
        Log.d("Resultado: ", resultado);

        Intent intent = new Intent(this, Form_Login.class);
        startActivity(intent);
    }

    private void cadastrarMotorista() {
        String nome = editTextNome.getText().toString();
        String cpf = editTextCPF.getText().toString();
        String email = editTextEmail.getText().toString();
        String senha = editTextSenha.getText().toString();
        String tipo = spinnerTipo.getSelectedItem().toString();
        String telefone = editTextTelefone.getText().toString();
        String cnh = editTextCnh.getText().toString();
        String modelo_carro = editTextModeloCarro.getText().toString();
        String placa_veiculo = editTextPlacaVeiculo.getText().toString();

        if (TextUtils.isEmpty(nome)) {
            Toast.makeText(this, "O nome é obrigatório.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(cpf)) {
            Toast.makeText(this, "O cpf é obrigatório.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "O e-mail é obrigatório.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(senha)) {
            Toast.makeText(this, "A senha é obrigatória.", Toast.LENGTH_SHORT).show();
            return;
        }

        Motorista motorista = new Motorista(nome, cpf, email, senha, telefone, tipo, cnh, modelo_carro, placa_veiculo);

        Dao dao = new Dao(this);
        String resultado = dao.inserirMotorista(motorista);
        Log.d("Resultado: ", resultado);

        Intent intent = new Intent(this, Form_Login.class);
        startActivity(intent);
    }
}
