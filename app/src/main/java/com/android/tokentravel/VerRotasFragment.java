package com.android.tokentravel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VerRotasFragment extends Fragment {
    private RecyclerView recyclerViewRotas;
    private AdapterListarRotasFragment adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ver_rotas, container, false);

        recyclerViewRotas = view.findViewById(R.id.recyclerViewVerRotasCdastradas);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewRotas.setLayoutManager(layoutManager);

        adapter = new AdapterListarRotasFragment(getList());
        recyclerViewRotas.setAdapter(adapter);

        return view;
    }

    private List<Rota> getList() {
        List<Rota> rotas = new ArrayList<>();

        rotas.add(new Rota("Belém, Pará, Brasil", "Santarém, Pará, Brasil", "Domingo, Segunda, Terça, Quarta, Quinta, Sexta, Sábado", "Taxí", "150,00", "08:30"));
        rotas.add(new Rota("Ananindeua, Pará, Brasil", "Castanhal, Pará, Brasil", "Terça", "Van", "80,00", "09:45"));
        rotas.add(new Rota("Belém, Pará, Brasil", "Salinópolis, Pará, Brasil", "Quarta", "Taxí", "200,00", "14:00"));
        rotas.add(new Rota("Ananindeua, Pará, Brasil", "Paragominas, Pará, Brasil", "Quinta", "Van", "120,00", "17:15"));
        rotas.add(new Rota("Marituba, Pará, Brasil", "Barcarena, Pará, Brasil", "Sexta", "Taxí", "90,00", "10:30"));
        rotas.add(new Rota("Belém, Pará, Brasil", "Marabá, Pará, Brasil", "Sábado", "Van", "280,00", "13:45"));
        rotas.add(new Rota("Ananindeua, Pará, Brasil", "Abaetetuba, Pará, Brasil", "Domingo", "Taxí", "60,00", "16:00"));

        // Rotas fictícias

        return rotas;
    }

}

