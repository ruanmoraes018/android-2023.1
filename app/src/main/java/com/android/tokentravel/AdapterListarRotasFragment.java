package com.android.tokentravel;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.tokentravel.objetos.Rotas;

import java.util.ArrayList;
import java.util.List;

public class AdapterListarRotasFragment extends RecyclerView.Adapter<AdapterListarRotasFragment.MyViewHolder> {
    private List<Rotas> mylist;

    public AdapterListarRotasFragment() {
        this.mylist = new ArrayList<>();
    }

    public void setRotasList(List<Rotas> rotasList) {
        this.mylist = rotasList;
        notifyDataSetChanged(); // Notifique o adaptador sobre a alteração nos dados
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_adapter_listar_rotas, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Rotas rota = mylist.get(position);
        holder.textOrigem.setText("Origem: " + rota.getOrigem());
        holder.textDestino.setText("Destino: " + rota.getDestino());

        // Crie uma lista de nomes de dias ativos com base nas variáveis booleanas
        List<String> diasAtivos = new ArrayList<>();
        if (rota.isDomingo()) {
            diasAtivos.add("Domingo");
        }
        if (rota.isSegunda()) {
            diasAtivos.add("Segunda-feira");
        }
        if (rota.isTerca()) {
            diasAtivos.add("Terça-feira");
        }
        if (rota.isQuarta()) {
            diasAtivos.add("Quarta-feira");
        }
        if (rota.isQuinta()) {
            diasAtivos.add("Quinta-feira");
        }
        if (rota.isSexta()) {
            diasAtivos.add("Sexta-feira");
        }
        if (rota.isSabado()) {
            diasAtivos.add("Sábado");
        }

        // Converta a lista de nomes dos dias em uma única string
        String diasString = TextUtils.join(", ", diasAtivos);

        holder.textDia_da_semana.setText("Dia: " + diasString);
        holder.textTaxí_OU_Van.setText("Veículo: " + rota.getTipo());
        holder.textValor.setText("Preço: " + rota.getValor());
        holder.textHorario_Ida.setText("Horário: " + rota.getHorario());

        // Configurar um clique no ícone de edição
        holder.imageEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Quando o ícone de edição é clicado, exibe um diálogo de edição
                showEditDialog(holder.itemView.getContext(), rota); // Passe o contexto correto aqui
            }
        });

        // Configurar um clique no ícone de exclusão
        holder.imageDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Quando o ícone de exclusão é clicado, exibe um diálogo de exclusão
                showDeleteDialog(holder.itemView.getContext(), rota); // Passe o contexto correto aqui
            }
        });
    }

    @Override
    public int getItemCount() {
        return mylist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textOrigem;
        public TextView textDestino;
        public TextView textDia_da_semana;
        public TextView textTaxí_OU_Van;
        public TextView textValor;
        public TextView textHorario_Ida;
        public ImageView imageEditar;
        public ImageView imageDeletar;

        public MyViewHolder(View itemView) {
            super(itemView);
            textOrigem = itemView.findViewById(R.id.textOrigem);
            textDestino = itemView.findViewById(R.id.textDestino);
            textDia_da_semana = itemView.findViewById(R.id.textDia_da_semana);
            textTaxí_OU_Van = itemView.findViewById(R.id.textTaxí_OU_Van);
            textValor = itemView.findViewById(R.id.textValor);
            textHorario_Ida = itemView.findViewById(R.id.textHorario_Ida);
            imageEditar = itemView.findViewById(R.id.imageeditar);
            imageDeletar = itemView.findViewById(R.id.imagedeletar);
        }
    }

    // Método para exibir o diálogo de edição
    private void showEditDialog(Context context, final Rotas rota) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.editar_rotas_cadastradas, null);

        Spinner spinnerOutro;
        spinnerOutro = dialogView.findViewById(R.id.editVeiculo);

        String[] opcoesOutroSpinner = {"Taxí", "Van"};
        ArrayAdapter<String> adapterOutroSpinner = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, opcoesOutroSpinner) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.BLACK); // Defina a cor do texto para o spinner
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.BLUE); // Defina a cor do texto para o dropdown do spinner
                return view;
            }
        };

        // Defina o adaptador para o spinner
        spinnerOutro.setAdapter(adapterOutroSpinner);


        // Aqui você pode inicializar os campos do diálogo com os valores da Rota
        // Por exemplo:
        // EditText editOrigem = dialogView.findViewById(R.id.editOrigem);
        // editOrigem.setText(rota.getOrigem());

        builder.setView(dialogView)
                .setTitle("Editar Rota")
                .setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Lógica para salvar a edição da rota aqui
                        // Por exemplo:
                        // rota.setOrigem(editOrigem.getText().toString());
                        // Atualize o RecyclerView após a edição
                        notifyDataSetChanged();
                        dialog.dismiss(); // Feche o diálogo após salvar
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss(); // Feche o diálogo ao cancelar
                    }
                });
        builder.create().show();
    }

    // Método para exibir o diálogo de exclusão
    private void showDeleteDialog(Context context, final Rotas rota) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Excluir Rota")
                .setMessage("Tem certeza de que deseja excluir esta rota?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Lógica para excluir a rota aqui
                        // Por exemplo:
                        // mylist.remove(rota);
                        // Atualize o RecyclerView após a exclusão
                        notifyDataSetChanged();
                        dialog.dismiss(); // Feche o diálogo após excluir
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss(); // Feche o diálogo ao cancelar
                    }
                });
        builder.create().show();
    }
}

