package com.android.tokentravel;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class CadastrarFragment extends Fragment {

    private Button btncadastrarROTAFinal;
    private Spinner spinnerOutro;
//    private MultiAutoCompleteTextView multiAutoCompleteTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cadastrar, container, false);

        spinnerOutro = view.findViewById(R.id.edit_tipo_veiculo);
//        multiAutoCompleteTextView = view.findViewById(R.id.multiAutoCompleteTextView);
        btncadastrarROTAFinal = view.findViewById(R.id.bt_cadastrar_rota);

        String[] opcoesOutroSpinner = {"Taxí", "Van"};
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

//        // Configure o MultiAutoCompleteTextView
//        String[] daysOfWeek = {"Domingo", "Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado"};
//        ArrayAdapter<String> daysAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, daysOfWeek);
//
//        multiAutoCompleteTextView.setAdapter(daysAdapter);
//        multiAutoCompleteTextView.setTokenizer(new MultiAutoCompleteTextView.Tokenizer() {
//            @Override
//            public int findTokenStart(CharSequence text, int cursor) {
//                int i = cursor;
//                while (i > 0 && text.charAt(i - 1) != ',') {
//                    i--;
//                }
//                while (i < cursor && text.charAt(i) == ' ') {
//                    i++;
//                }
//                return i;
//            }
//
//            @Override
//            public int findTokenEnd(CharSequence text, int cursor) {
//                int i = cursor;
//                int len = text.length();
//                while (i < len) {
//                    if (text.charAt(i) == ',') {
//                        return i;
//                    } else {
//                        i++;
//                    }
//                }
//                return len;
//            }
//
//            @Override
//            public CharSequence terminateToken(CharSequence text) {
//                return text + ", ";
//            }
//        });
//
        btncadastrarROTAFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Criar uma instância do fragmento VerRotasFragment
                VerRotasFragment verRotasFragment = new VerRotasFragment();

                // Iniciar uma transação de fragmento
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();

                // Substituir o fragmento atual pelo VerRotasFragment
                transaction.replace(R.id.fragment_cadastrarrota, verRotasFragment);

                // Adicionar a transação à pilha de fragmentos (opcional)
                // transaction.addToBackStack(null);

                // Confirmar a transação
                transaction.commit();
            }
        });

        return view;
    }
}
