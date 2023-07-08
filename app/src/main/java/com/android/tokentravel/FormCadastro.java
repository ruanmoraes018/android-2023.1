package com.android.tokentravel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.view.View;
import android.widget.Button;


public class FormCadastro extends AppCompatActivity {

    private Button btCadastrado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro);
        IniciarBotaoCadastrado();


        Spinner spinnerUserType = findViewById(R.id.spinner_user_type);

        String[] userTypes = {"Passageiro", "Motorista"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, userTypes);
        spinnerUserType.setAdapter(adapter);

        String selectedUserType = spinnerUserType.getSelectedItem().toString();

        btCadastrado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FormCadastro.this, Navigation_View.class);
                startActivity(intent);
            }
        });
    }

    private void IniciarBotaoCadastrado(){
        btCadastrado = findViewById(R.id.bt_cadastrar);
    }
}
