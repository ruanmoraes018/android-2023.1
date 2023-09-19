package com.android.tokentravel;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.tokentravel.dao.Dao;
import com.android.tokentravel.objetos.Pessoa;
import com.android.tokentravel.objetos.Rotas;

public class CadastrarFragment extends Fragment {
    EditText editTextOrigem, editTextDestino, editTextValor, editTextHorarioIda;
    Spinner tipoVeiculo;
    CheckBox checkBoxDomingo, checkBoxSegunda, checkBoxTerca, checkBoxQuarta, checkBoxQuinta, checkBoxSexta, checkBoxSabado;
    private Button btncadastrarROTAFinal;
    private Spinner spinnerOutro;
//    private MultiAutoCompleteTextView multiAutoCompleteTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cadastrar, container, false);



        spinnerOutro = view.findViewById(R.id.edit_tipo_veiculo);
//        multiAutoCompleteTextView = view.findViewById(R.id.multiAutoCompleteTextView);
        btncadastrarROTAFinal = view.findViewById(R.id.bt_cadastrar_rota);

        String[] opcoesOutroSpinner = {"Táxi", "Van"};
        ArrayAdapter<String> adapterOutroSpinner = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, opcoesOutroSpinner) {
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

        btncadastrarROTAFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Inicialize os campos de entrada aqui
                    editTextOrigem = view.findViewById(R.id.edit_origem);
                    editTextDestino = view.findViewById(R.id.edit_destino);
                    spinnerOutro = view.findViewById(R.id.edit_tipo_veiculo); // Use o spinner personalizado
                    editTextValor = view.findViewById(R.id.edit_valor);
                    editTextHorarioIda = view.findViewById(R.id.edit_horario_ida);
                    checkBoxDomingo = view.findViewById(R.id.checkbox_sunday);
                    checkBoxSegunda = view.findViewById(R.id.checkbox_monday);
                    checkBoxTerca = view.findViewById(R.id.checkbox_tuesday);
                    checkBoxQuarta = view.findViewById(R.id.checkbox_wednesday);
                    checkBoxQuinta = view.findViewById(R.id.checkbox_thursday);
                    checkBoxSexta = view.findViewById(R.id.checkbox_friday);
                    checkBoxSabado = view.findViewById(R.id.checkbox_saturday);

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                    Integer idDoMotoristaLogado = sharedPreferences.getInt("idDoMotoristaLogado", 0);
                    String emailDoMotoristaLogado = sharedPreferences.getString("emailDoMotoristaLogado", "");
                    Rotas rotas = new Rotas(
                            editTextOrigem.getText().toString(),
                            editTextDestino.getText().toString(),
                            spinnerOutro.getSelectedItem().toString(), // Use o spinner personalizado
                            Float.parseFloat(editTextValor.getText().toString()),
                            editTextHorarioIda.getText().toString(),
                            idDoMotoristaLogado,
                            checkBoxDomingo.isChecked(),
                            checkBoxSegunda.isChecked(),
                            checkBoxTerca.isChecked(),
                            checkBoxQuarta.isChecked(),
                            checkBoxQuinta.isChecked(),
                            checkBoxSexta.isChecked(),
                            checkBoxSabado.isChecked()
                    );

                    Dao dao = new Dao(requireContext());
                    long rotaId = dao.inserirRota(rotas);

                    if (rotaId != -1) {

//                        // Criar um Bundle e adicionar os valores das variáveis
//                        Bundle bundle = new Bundle();
//                        bundle.putString("origemDaRota", editTextOrigem.getText().toString());
//                        bundle.putString("destinoDaRota", editTextDestino.getText().toString());
//                        bundle.putString("tipoDaRota", spinnerOutro.getSelectedItem().toString());
//                        bundle.putFloat("valorDaRota", Float.parseFloat(editTextValor.getText().toString()));
//                        bundle.putString("horarioDaRota", editTextHorarioIda.getText().toString());
//                        bundle.putInt("idDoMotora", idDoMotoristaLogado);
//                        bundle.putBoolean("domingo", checkBoxDomingo.isChecked());
//                        bundle.putBoolean("segundaFeira", checkBoxSegunda.isChecked());
//                        bundle.putBoolean("tercaFeira", checkBoxTerca.isChecked());
//                        bundle.putBoolean("quartaFeira", checkBoxQuarta.isChecked());
//                        bundle.putBoolean("quintaFeira", checkBoxQuinta.isChecked());
//                        bundle.putBoolean("sextaFeira", checkBoxSexta.isChecked());
//                        bundle.putBoolean("sabado", checkBoxSabado.isChecked());
//
//// Obtém o FragmentManager
//                        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
//
//                        // Inicializa a VerRotasFragment com os argumentos
//                        VerRotasFragment verRotasFragment = new VerRotasFragment();
//                        verRotasFragment.setArguments(bundle);
//
//                        // Começa uma transação de fragmento
//                        FragmentTransaction transaction = fragmentManager.beginTransaction();
//
//                        // Adiciona a VerRotasFragment à pilha de fragmentos
//                        transaction.addToBackStack(null);
//
//                        // Substitui o fragmento atual pelo VerRotasFragment
//                        transaction.replace(R.id.fragment_cadastrarrota, verRotasFragment);
//
//                        // Confirma a transação
//                        transaction.commit();


                        // Somente substitua o fragmento se a rota for cadastrada com sucesso
                        VerRotasFragment verRotasFragment = new VerRotasFragment();
                        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_cadastrarrota, verRotasFragment);
                        transaction.commit();

                        String email = sharedPreferences.getString("emailDoMotoristaLogado", "");

                        Toast.makeText(getContext(), "Rota cadastrada com sucesso!", Toast.LENGTH_SHORT).show();
                        Log.d("CadastroRota", "Rota cadastrada com sucesso!");
                    } else {
                        Toast.makeText(getContext(), "Ocorreu um erro ao cadastrar rota!", Toast.LENGTH_SHORT).show();
                    }

                    // Se a inserção for bem-sucedida, você pode executar qualquer ação adicional aqui

                } catch (Exception e) {
                    // Trate a exceção aqui, por exemplo, exiba uma mensagem de erro ou faça o que for necessário
                    e.printStackTrace(); // Isso imprimirá a exceção no logcat para fins de depuração
                }
            }
        });


        return view;
    }
}