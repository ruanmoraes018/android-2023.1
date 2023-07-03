package com.android.tokentravel;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class FormCadastro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro);

        Spinner spinnerUserType = findViewById(R.id.spinner_user_type);

        String[] userTypes = {"Passageiro", "Motorista"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, userTypes);
        spinnerUserType.setAdapter(adapter);

        String selectedUserType = spinnerUserType.getSelectedItem().toString();
    }
}
