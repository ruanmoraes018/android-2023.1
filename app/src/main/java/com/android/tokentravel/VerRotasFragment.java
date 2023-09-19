package com.android.tokentravel;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.tokentravel.objetos.Rotas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VerRotasFragment extends Fragment {
    private RecyclerView recyclerViewRotas;
    private AdapterListarRotasFragment adapter;
    private SharedPreferences sharedPreferences;
    private String origemDaRotaCriada;
    private String destinoDaRotaCriada;
    private String tipoDaRotaCriada;
    private float valorDaRotaCriada;
    private String horarioDaRotaCriada;
    private Integer idDoMotoraDaRotaCriada;
    private boolean diaDomingo;
    private boolean diaSegunda;
    private boolean diaTerca;
    private boolean diaQuarta;
    private boolean diaQuinta;
    private boolean diaSexta;
    private boolean diaSabado;
    private String diasDaSemana;
    private String valorDaRotaCriadaStr;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ver_rotas, container, false);

        // Inicialize as variáveis dentro do onCreateView, onde o contexto está disponível
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        origemDaRotaCriada = sharedPreferences.getString("origemDaRota", "");
        destinoDaRotaCriada = sharedPreferences.getString("destinoDaRota", "");
        tipoDaRotaCriada = sharedPreferences.getString("tipoDaRota", "");
        valorDaRotaCriada = sharedPreferences.getFloat("valorDaRota", 0);
        horarioDaRotaCriada = sharedPreferences.getString("horarioDaRota", "");
        idDoMotoraDaRotaCriada = sharedPreferences.getInt("idDoMotora", 0);
        diaDomingo = sharedPreferences.getBoolean("domingo", true);
        diaSegunda = sharedPreferences.getBoolean("segundaFeira", true);
        diaTerca = sharedPreferences.getBoolean("tercaFeira", true);
        diaQuarta = sharedPreferences.getBoolean("quartaFeira", true);
        diaQuinta = sharedPreferences.getBoolean("quintaFeira", true);
        diaSexta = sharedPreferences.getBoolean("sextaFeira", true);
        diaSabado = sharedPreferences.getBoolean("sabado", true);
//        diasDaSemana = String.format(
//                "%b,%b,%b,%b,%b,%b,%b",
//                diaDomingo, diaSegunda, diaTerca, diaQuarta, diaQuinta, diaSexta, diaSabado
//        );
//        valorDaRotaCriadaStr = Float.toString(valorDaRotaCriada);

        recyclerViewRotas = view.findViewById(R.id.recyclerViewVerRotasCdastradas);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewRotas.setLayoutManager(layoutManager);

        adapter = new AdapterListarRotasFragment(getList());
        recyclerViewRotas.setAdapter(adapter);



        return view;
    }

    private List<Rotas> getList() {
        List<Rotas> rotas = new ArrayList<>();

    rotas.add(new Rotas(origemDaRotaCriada, destinoDaRotaCriada, tipoDaRotaCriada, valorDaRotaCriada, horarioDaRotaCriada, idDoMotoraDaRotaCriada, diaDomingo, diaSegunda, diaTerca, diaQuarta, diaQuinta, diaSexta, diaSabado));
//        rotas.add(new Rota("Ananindeua, Pará, Brasil", "Castanhal, Pará, Brasil", "Terça", "Van", "80,00", "09:45"));
//        rotas.add(new Rota("Belém, Pará, Brasil", "Salinópolis, Pará, Brasil", "Quarta", "Taxí", "200,00", "14:00"));
//        rotas.add(new Rota("Ananindeua, Pará, Brasil", "Paragominas, Pará, Brasil", "Quinta", "Van", "120,00", "17:15"));
//        rotas.add(new Rota("Marituba, Pará, Brasil", "Barcarena, Pará, Brasil", "Sexta", "Taxí", "90,00", "10:30"));
//        rotas.add(new Rota("Belém, Pará, Brasil", "Marabá, Pará, Brasil", "Sábado", "Van", "280,00", "13:45"));
//        rotas.add(new Rota("Ananindeua, Pará, Brasil", "Abaetetuba, Pará, Brasil", "Domingo", "Taxí", "60,00", "16:00"));

        // Rotas fictícias

        return rotas;
    }

}

