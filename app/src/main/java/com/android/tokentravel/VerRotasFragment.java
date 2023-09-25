package com.android.tokentravel;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.tokentravel.dao.Dao;
import com.android.tokentravel.objetos.Rotas;
import java.util.ArrayList;
import java.util.List;

public class VerRotasFragment extends Fragment {
    private RecyclerView recyclerViewRotas;
    private AdapterListarRotasFragment adapter;
    private SharedPreferences sharedPreferences;
    private Dao dao; // Certifique-se de inicializar o objeto Dao em algum lugar

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ver_rotas, container, false);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        recyclerViewRotas = view.findViewById(R.id.recyclerViewVerRotasCdastradas);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewRotas.setLayoutManager(layoutManager);
        // Inicialize o objeto dao passando o contexto do fragmento
        dao = new Dao(getContext());
        // Obtenha o ID do motorista logado das preferências compartilhadas
        int idMotoristaLogado = sharedPreferences.getInt("idDoMotoristaLogado", -1);

        // Verifique se o ID do motorista é válido (-1 indica que não foi encontrado)
        if (idMotoristaLogado != -1) {
            // Chame a função para buscar as rotas do motorista passando o ID do motorista logado
            List<Rotas> rotas = buscaRotasDoMotorista(idMotoristaLogado);

            adapter = new AdapterListarRotasFragment(this); // 'this' é uma referência ao fragmento
            recyclerViewRotas.setAdapter(adapter);

            // Configure o Adapter com os dados recuperados
            adapter.setRotasList(rotas);
        } else {
            // Trate o caso em que o ID do motorista não foi encontrado
            Toast.makeText(getContext(), "ID do Motorista não encontrado", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private List<Rotas> buscaRotasDoMotorista(int idMotorista) {
        // Use a função do banco de dados para buscar as rotas do motorista com base no ID
        List<Rotas> rotas = dao.buscaRotasMotorista(idMotorista);

        // Verifique se a lista retornada é nula ou vazia
        if (rotas == null || rotas.isEmpty()) {
            return new ArrayList<>(); // Inicialize e retorne uma lista vazia
        }

        return rotas;
    }
    public void recarregarFragmento() {
        // Obtenha o ID do motorista logado das preferências compartilhadas
        int idMotoristaLogado = sharedPreferences.getInt("idDoMotoristaLogado", -1);

        // Verifique se o ID do motorista é válido (-1 indica que não foi encontrado)
        if (idMotoristaLogado != -1) {
            // Chame a função para buscar as rotas do motorista passando o ID do motorista logado
            List<Rotas> rotas = buscaRotasDoMotorista(idMotoristaLogado);

            // Atualize o Adapter com os novos dados
            if (adapter != null) {
                adapter.setRotasList(rotas);
                adapter.notifyDataSetChanged();
            }
        } else {
            // Trate o caso em que o ID do motorista não foi encontrado
            Toast.makeText(getContext(), "ID do Motorista não encontrado", Toast.LENGTH_SHORT).show();
        }
    }

}
