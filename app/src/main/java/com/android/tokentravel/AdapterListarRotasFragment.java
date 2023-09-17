package com.android.tokentravel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AdapterListarRotasFragment extends RecyclerView.Adapter<AdapterListarRotasFragment.MyViewHolder> {
    private List<Rota> mylist;

    public AdapterListarRotasFragment(List<Rota> mylist) {
        this.mylist = mylist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_adapter_listar_rotas, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Rota rota = mylist.get(position);
        holder.textOrigem.setText("Origem: " + rota.getOrigem());
        holder.textDestino.setText("Destino: " + rota.getDestino());
        holder.textDia_da_semana.setText("Dia: " + rota.getDiaDaSemana());
        holder.textTaxí_OU_Van.setText("Veículo: " + rota.getVeiculo());
        holder.textValor.setText("Preço: " + rota.getPreco());
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
    private void showEditDialog(Context context, final Rota rota) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.editar_rotas_cadastradas, null);

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
    private void showDeleteDialog(Context context, final Rota rota) {
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


class Rota {
    private String origem;
    private String destino;
    private String diaDaSemana;
    private String veiculo;
    private String preco;
    private String horario;

    public Rota(String origem, String destino, String diaDaSemana, String veiculo, String preco, String horario) {
        this.origem = origem;
        this.destino = destino;
        this.diaDaSemana = diaDaSemana;
        this.veiculo = veiculo;
        this.preco = preco;
        this.horario = horario;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getDiaDaSemana() {
        return diaDaSemana;
    }

    public void setDiaDaSemana(String diaDaSemana) {
        this.diaDaSemana = diaDaSemana;
    }

    public String getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(String veiculo) {
        this.veiculo = veiculo;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }
}
