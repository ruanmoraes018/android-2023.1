package com.android.tokentravel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.tokentravel.dao.Dao;
import com.android.tokentravel.objetos.Rotas;
import java.util.ArrayList;

public class Adapter_motoristas extends RecyclerView.Adapter<Adapter_motoristas.ViewHolder> {
    private ArrayList<Rotas> rotasInfoList;
    private Context context;
    private Dao dao; // Use "Dao" como o nome da sua classe Dao

    public Adapter_motoristas(Context context, ArrayList<Rotas> rotasInfoList) {
        this.context = context;
        this.rotasInfoList = rotasInfoList;

        // Inicialize a instância do Dao aqui
        dao = new Dao(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_motoristas, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Rotas rotaInfo = rotasInfoList.get(position);
        holder.origemTextView.setText("Origem: " + rotaInfo.getOrigem());
        holder.destinoTextView.setText("Destino: " + rotaInfo.getDestino());
        holder.valorTextView.setText("Valor: R$ " + rotaInfo.getValor());
        holder.horarioTextView.setText("Horário: " + rotaInfo.getHorario());

        // Set an OnClickListener for the button
        holder.rotaselectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int clickedPosition = holder.getAdapterPosition();
                if (clickedPosition != RecyclerView.NO_POSITION) {
                    int numeroRota = rotaInfo.getNumero_rota();
                    int idMotorista = rotaInfo.getId_motorista();

                    // Chame a função do Dao para buscar o ID da rota
                    int idRota = dao.buscarIdRotaPorNumeroRotaEIdMotorista(numeroRota, idMotorista);

                    if (idRota != -1) {
                        // Start the new activity here based on the clicked position
                        Intent intent = new Intent(context, Perfil_Apresent_RotaProf.class);
                        intent.putExtra("numeroRota", rotaInfo.getNumero_rota());
                        intent.putExtra("idRota", idRota);
                        intent.putExtra("origem", rotaInfo.getOrigem());
                        intent.putExtra("destino", rotaInfo.getDestino());
                        intent.putExtra("position", position);
                        intent.putExtra("codigoUnicoMotorista", rotaInfo.getCodigo_motorista()); // Substitua pela chave correta e pelo código correto do motorista


                        // Passe o id_motorista diretamente
                        intent.putExtra("id_motoristaagora", rotaInfo.getId_motorista());
                        Log.d("Perf_Apre_RotaProf", "Posição recebida: " + position);

                        Log.d("Adapter_motoristas", "ID do motorista: " + rotaInfo.getId_motorista());
                        Log.d("Adapter_motoristas", "Numero da rota: " + rotaInfo.getNumero_rota());
                        Log.d("Adapter_motoristas", "ID da rota: " + idRota);
                        context.startActivity(intent);
                    } else {
                        // Trate o caso em que a rota não foi encontrada
                        Log.d("Adapter_motoristas", "Rota não encontrada para o número de rota: " + numeroRota);
                        // Você pode mostrar uma mensagem de erro ou tomar outra ação apropriada aqui.
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return rotasInfoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView origemTextView;
        TextView destinoTextView;
        TextView valorTextView;
        TextView horarioTextView;
        androidx.appcompat.widget.AppCompatButton rotaselectButton;

        public ViewHolder(View itemView) {
            super(itemView);
            origemTextView = itemView.findViewById(R.id.textOrigem);
            destinoTextView = itemView.findViewById(R.id.textDestino);
            valorTextView = itemView.findViewById(R.id.textValor);
            horarioTextView = itemView.findViewById(R.id.textHorario);
            rotaselectButton = itemView.findViewById(R.id.rotaselect);
        }
    }
}
