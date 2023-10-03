package com.android.tokentravel;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.tokentravel.dao.Dao;
import com.android.tokentravel.objetos.Rotas;
import com.bumptech.glide.Glide;

import java.util.List;

public class Adapter_motoristas extends RecyclerView.Adapter<Adapter_motoristas.MyViewHolder> {
    private List<Rotas> mylist;
    private Context context;
    private Dao dao; // Mova a inicialização do DAO para o construtor

    public Adapter_motoristas(Context context, List<Rotas> mylist) {
        this.context = context;
        this.mylist = mylist;
        dao = new Dao(context); // Inicialize o DAO com o contexto
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_motoristas, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Rotas rota = mylist.get(position); // Obtenha a rota na posição desejada

        // Chame o método getPessoa_nome() na instância de Dao para buscar o nome do motorista
        String nomeDoMotorista = dao.buscaNomeMotoristaPorId(rota.getId_motorista());

        // Verifique se o nomeDoMotorista não é nulo antes de usá-lo
        if (nomeDoMotorista != null) {
            holder.textName.setText("Motorista: " + nomeDoMotorista); // Define o nome do motorista no TextView
        } else {
            holder.textName.setText("Nome do Motorista: N/A"); // Define um valor padrão caso o nome não seja encontrado
        }



        // Adicione um OnClickListener à visualização que envolve o item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar a atividade Perfil_Apresent_RotaProf e passar informações relevantes
                Intent intent = new Intent(context, Perfil_Apresent_RotaProf.class);
                intent.putExtra("numeroRota", rota.getNumero_rota());
                intent.putExtra("origem", rota.getOrigem());
                intent.putExtra("destino", rota.getDestino());
                // Adicione outros extras conforme necessário

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mylist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textName;

        public MyViewHolder(View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
        }
    }
}