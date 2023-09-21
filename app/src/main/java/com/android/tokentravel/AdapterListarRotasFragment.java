package com.android.tokentravel;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import com.android.tokentravel.dao.Dao;

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
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(v.getContext());
                Integer idDoMotoristaLogado = sharedPreferences.getInt("idDoMotoristaLogado", 0);
                Log.d("ID_Motorista", "ID do Motorista: " + idDoMotoristaLogado);
                Log.d("Dao", "Número de rota a ser buscado: " + rota.getNumero_rota());
                Dao dao = new Dao(v.getContext());
                int idRota = dao.buscarIdRotaPorNumeroRota(rota.getNumero_rota());
                Log.d("Dao", "ID da rota encontrado: " + idRota);

                showEditDialog(holder.itemView.getContext(), idDoMotoristaLogado, idRota, rota);

            }
        });

        // Configurar um clique no ícone de exclusão
        holder.imageDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(v.getContext());
                Integer idDoMotoristaLogado = sharedPreferences.getInt("idDoMotoristaLogado", 0);
                Log.d("ID_Motorista", "ID do Motorista: " + idDoMotoristaLogado);
                Log.d("Dao", "Número de rota a ser buscado: " + rota.getNumero_rota());
                Dao dao = new Dao(v.getContext());
                int idRota = dao.buscarIdRotaPorNumeroRota(rota.getNumero_rota());
                Log.d("Dao", "ID da rota encontrado: " + idRota);

                showDeleteDialog(holder.itemView.getContext(), idDoMotoristaLogado, idRota);

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
    private void showEditDialog(Context context, Integer idDoMotoristaLogado, int idRota, Rotas rota) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.editar_rotas_cadastradas, null);

        Spinner spinnerOutro = dialogView.findViewById(R.id.editVeiculo);

        String[] opcoesOutroSpinner = {"Taxí", "Van"};
        ArrayAdapter<String> adapterOutroSpinner = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, opcoesOutroSpinner) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.BLACK);
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.BLUE);
                return view;
            }
        };

        spinnerOutro.setAdapter(adapterOutroSpinner);

        spinnerOutro = dialogView.findViewById(R.id.editVeiculo);
        EditText editOrigem = dialogView.findViewById(R.id.editOrigem);
        EditText editDestino = dialogView.findViewById(R.id.editDestino);
        EditText editHorario = dialogView.findViewById(R.id.editHorario);
        EditText editValor = dialogView.findViewById(R.id.editPreco);

        CheckBox checkDomingo = dialogView.findViewById(R.id.checkDomingo);
        CheckBox checkSegunda = dialogView.findViewById(R.id.checkSegunda);
        CheckBox checkTerca = dialogView.findViewById(R.id.checkTerca);
        CheckBox checkQuarta = dialogView.findViewById(R.id.checkQuarta);
        CheckBox checkQuinta = dialogView.findViewById(R.id.checkQuinta);
        CheckBox checkSexta = dialogView.findViewById(R.id.checkSexta);
        CheckBox checkSabado = dialogView.findViewById(R.id.checkSabado);

        spinnerOutro.setSelection(rota.getTipo().equals("Taxí") ? 0 : 1);
        editOrigem.setText(rota.getOrigem());
        editDestino.setText(rota.getDestino());
        editHorario.setText(rota.getHorario());
        editValor.setText(String.valueOf(rota.getValor()));

        checkDomingo.setChecked(rota.isDomingo());
        checkSegunda.setChecked(rota.isSegunda());
        checkTerca.setChecked(rota.isTerca());
        checkQuarta.setChecked(rota.isQuarta());
        checkQuinta.setChecked(rota.isQuinta());
        checkSexta.setChecked(rota.isSexta());
        checkSabado.setChecked(rota.isSabado());

        Spinner finalSpinnerOutro = spinnerOutro;
        builder.setView(dialogView)
                .setTitle("Editar Rota")
                .setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Atualize os valores da rota com base nos campos do diálogo
                        rota.setOrigem(editOrigem.getText().toString());
                        rota.setDestino(editDestino.getText().toString());
                        rota.setTipo(finalSpinnerOutro.getSelectedItem().toString());
                        rota.setHorario(editHorario.getText().toString());
                        rota.setValor(Float.parseFloat(editValor.getText().toString()));

                        // Atualize os valores dos dias da semana com base nos CheckBoxes
                        rota.setDomingo(checkDomingo.isChecked());
                        rota.setSegunda(checkSegunda.isChecked());
                        rota.setTerca(checkTerca.isChecked());
                        rota.setQuarta(checkQuarta.isChecked());
                        rota.setQuinta(checkQuinta.isChecked());
                        rota.setSexta(checkSexta.isChecked());
                        rota.setSabado(checkSabado.isChecked());

                        // Verifique se o objeto rota não é nulo
                        if (rota != null) {
                            // Chame o método atualizarRota do Dao para persistir as alterações no banco de dados
                            Dao dao = new Dao(context);
                            int linhasAfetadas = dao.atualizarRota(idDoMotoristaLogado, idRota, rota);

                            if (linhasAfetadas > 0) {
                                // Atualização bem-sucedida
                                notifyDataSetChanged();

                            } else {
                                // Algo deu errado na atualização
                            }

                            notifyDataSetChanged();
                        } else {
                            Toast.makeText(context, "NUUUULOO!", Toast.LENGTH_SHORT).show();

                        }

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
    private void showDeleteDialog(Context context, Integer idDoMotoristaLogado, int idRota) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Excluir Rota")
                .setMessage("Tem certeza de que deseja excluir esta rota?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Lógica para excluir a rota aqui
                        // Por exemplo:
                        Dao dao = new Dao(context);
                        int linhasAfetadas = dao.deletarRota(idDoMotoristaLogado, idRota);                        // Atualize o RecyclerView após a exclusão
                        if (linhasAfetadas > 0) {
                            // Atualização bem-sucedida
                            notifyDataSetChanged();

                        } else {
                        }
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

