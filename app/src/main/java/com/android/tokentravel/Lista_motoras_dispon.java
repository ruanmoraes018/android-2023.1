package com.android.tokentravel;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class Lista_motoras_dispon extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Adapter_motoristas adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_motoras_dispon);

        recyclerView = findViewById(R.id.recyclerView); // Certifique-se de que o RecyclerView tenha um ID no seu layout XML.

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new Adapter_motoristas(getList());
        recyclerView.setAdapter(adapter);
    }

    private List<String> getList() {
        return Arrays.asList(
                "Maria da Silva Santos",
                "João Pereira Alves",
                "Ana Carolina Oliveira",
                "Pedro Henrique Lima",
                "Luiza Fernandes Costa",
                "Rafael Souza Rodrigues",
                "Julia Martins Pereira",
                "Diego Oliveira Cardoso",
                "Camila Santos Silva",
                "André Almeida Fernandes",
                "Laura Costa Gomes",
                "Bruno Rodrigues Castro",
                "Isabela Ramos Santos",
                "Lucas Oliveira Pereira",
                "Carolina Castro Lima",
                "Guilherme Fernandes Carvalho",
                "Mariana Alves Rodrigues",
                "Leonardo Gomes Silva",
                "Amanda Pereira Oliveira",
                "Tiago Santos Almeida"
        );
    }
}
