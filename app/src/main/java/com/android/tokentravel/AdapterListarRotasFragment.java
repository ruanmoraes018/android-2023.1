package com.android.tokentravel;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import com.android.tokentravel.dao.Dao;

import com.android.tokentravel.objetos.Rotas;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenContext;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterListarRotasFragment extends RecyclerView.Adapter<AdapterListarRotasFragment.MyViewHolder> {
    private List<Rotas> mylist;
    private VerRotasFragment fragment;

////    private AutoCompleteTextView editTextOrigem;
////    private AutoCompleteTextView editTextDestino;
//    private ArrayAdapter<String> autoCompleteAdapterOrigem;
//    private ArrayAdapter<String> autoCompleteAdapterDestino;
//    private Set<String> sugestoesUnicas = new HashSet<>();
//    private static final long AUTOCOMPLETE_DELAY = 100;
//    private Handler handler = new Handler(Looper.getMainLooper());
//    private Runnable autocompleteRunnable;


    public AdapterListarRotasFragment(VerRotasFragment fragment) {
        this.mylist = new ArrayList<>();
        this.fragment = fragment;
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


        AutoCompleteTextView editOrigem = dialogView.findViewById(R.id.editOrigem);
        AutoCompleteTextView editDestino = dialogView.findViewById(R.id.editDestino);

//        autoCompleteAdapterOrigem = new ArrayAdapter<>(fragment.requireActivity(), android.R.layout.simple_dropdown_item_1line);
//        autoCompleteAdapterDestino = new ArrayAdapter<>(fragment.requireActivity(), android.R.layout.simple_dropdown_item_1line);
//        editOrigem.setAdapter(autoCompleteAdapterOrigem);
//        editDestino.setAdapter(autoCompleteAdapterDestino);

        spinnerOutro.setAdapter(adapterOutroSpinner);

        spinnerOutro = dialogView.findViewById(R.id.editVeiculo);

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


//        // Configurar o TextWatcher para as caixas de texto de origem e destino
//        editOrigem.addTextChangedListener(new TextWatcher() {
//            private CharSequence beforeText;
//
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                beforeText = charSequence;
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                String newText = editable.toString();
//                handler.removeCallbacks(autocompleteRunnable);
//                handler.postDelayed(() -> consultarSugestoes(editOrigem, autoCompleteAdapterOrigem), AUTOCOMPLETE_DELAY);
//            }
//
//        });
//
//        editDestino.addTextChangedListener(new TextWatcher() {
//            private CharSequence beforeText;
//
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                beforeText = charSequence;
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                // Não é necessário implementar isso
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                String newText = editable.toString();
//                handler.removeCallbacks(autocompleteRunnable);
//                handler.postDelayed(() -> consultarSugestoes(editDestino, autoCompleteAdapterDestino), AUTOCOMPLETE_DELAY);
//            }
//        });


//        return view;



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
                        if (true) {

                            Dao dao = new Dao(context);
                            int linhasAfetadas = dao.deletarRota(idDoMotoristaLogado, idRota);                        // Atualize o RecyclerView após a exclusão
                            if (linhasAfetadas > 0) {
                                // Atualização bem-sucedida
                                fragment.recarregarFragmento();

                            } else {
                                // Algo deu errado na atualização
                            }

                            fragment.recarregarFragmento();
                        } else {
                            Toast.makeText(context, "ERRO!", Toast.LENGTH_SHORT).show();

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



//    private void consultarSugestoes(AutoCompleteTextView autoCompleteTextView, ArrayAdapter<String> adapter) {
//        String textoDigitado = autoCompleteTextView.getText().toString().trim();
//
//        if (!textoDigitado.isEmpty()) {
//            MapboxGeocoding geocodingService = MapboxGeocoding.builder()
//                    .accessToken(fragment.getResources().getString(R.string.mapbox_access_token))
//                    .query(textoDigitado)
//                    .geocodingTypes(GeocodingCriteria.TYPE_POI)
//                    .languages("pt-BR")
//                    .country("BR")
//                    .build();
//
//            geocodingService.enqueueCall(new Callback<GeocodingResponse>() {
//                @Override
//                public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
//                    if (response.isSuccessful() && response.body() != null) {
//                        List<CarmenFeature> features = response.body().features();
//                        if (features.size() >= 2) {
//                            sugestoesUnicas.clear();
//
//                            for (CarmenFeature feature : features) {
//                                String cidade = "";
//                                String estado = "";
//                                String pais = "";
//
//                                for (CarmenContext context : feature.context()) {
//                                    String type = context.id();
//                                    String text = context.text();
//
//                                    if (type.startsWith("place")) {
//                                        cidade = text;
//                                    } else if (type.startsWith("region")) {
//                                        estado = text;
//                                    } else if (type.startsWith("country")) {
//                                        pais = text;
//                                    }
//                                }
//
//                                String sugestao = cidade + " " + estado + ", " + pais;
//                                sugestoesUnicas.add(sugestao);
//                            }
//
//                            adapter.clear();
//                            adapter.addAll(new ArrayList<>(sugestoesUnicas));
//                            adapter.notifyDataSetChanged();
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<GeocodingResponse> call, Throwable t) {
//                    Toast.makeText(fragment.getActivity(), "Algo deu errado, verifique sua conexão!", Toast.LENGTH_LONG).show();
//                }
//            });
//        } else {
//            adapter.clear();
//            adapter.notifyDataSetChanged();
//        }
//    }
}

