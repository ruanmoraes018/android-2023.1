package com.android.tokentravel;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.tokentravel.dao.Dao;
import com.android.tokentravel.objetos.Rotas;

import java.util.Arrays;
import java.util.List;

public class Lista_motoras_dispon extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Adapter_motoristas adapter;
    private Dao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_motoras_dispon);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String origem = sharedPreferences.getString("nomeOrigem", "");
        String destino = sharedPreferences.getString("nomeDestino", "");

        TextView origemTextView = findViewById(R.id.origemfinal);
        TextView destinoTextView = findViewById(R.id.destinofinal);

        origemTextView.setText(origem);
        destinoTextView.setText(destino);

        recyclerView = findViewById(R.id.recyclerView); // Certifique-se de que o RecyclerView tenha um ID no seu layout XML.

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Inicialize o objeto dao passando o contexto da atividade
        dao = new Dao(this);

        // Chame o método para buscar as rotas disponíveis com base na origem e destino
        List<Rotas> rotasDisponiveis = buscaRotasDisponiveis(origem, destino);

        // Passe o contexto e a lista de rotas para o Adapter_motoristas
        adapter = new Adapter_motoristas(this, rotasDisponiveis);

        recyclerView.setAdapter(adapter);
    }

    private List<Rotas> buscaRotasDisponiveis(String origem, String destino) {
        // Chame o método do DAO para buscar as rotas disponíveis com base na origem e destino
        return dao.buscaRotasDisponiveis(origem, destino);
    }
}