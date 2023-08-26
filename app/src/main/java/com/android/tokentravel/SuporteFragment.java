package com.android.tokentravel;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatButton;

public class SuporteFragment extends Fragment {

    private String suportepassageiro = "+5591998264357";
    private String suportemotorista = "+5591982817656";
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_suporte, container, false);

        AppCompatButton btnSuportePassageiro = view.findViewById(R.id.btnPassengerSupport);
        AppCompatButton btnSuporteMotorista = view.findViewById(R.id.btnDriverSupport);

        btnSuportePassageiro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogocontato1();
            }
        });

        btnSuporteMotorista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogocontato2();
            }
        });

        return view;
    }

    private void dialogocontato1(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Entrar em contato")
                .setMessage("O que você gostaria de fazer?")
                .setPositiveButton("WhatsApp", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        abrirWhatsApp(suportepassageiro, "Olá, sou um passageiro do TokenTravel mobile, gostaria de solicitar o suporte.");
                    }
                })
                .setNegativeButton("Ligar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        entrarEmContatoligacaoP(suportepassageiro);
                    }
                });
        builder.show();
    }

    private void dialogocontato2(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Entrar em contato")
                .setMessage("O que você gostaria de fazer?")
                .setPositiveButton("WhatsApp", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        abrirWhatsApp(suportemotorista, "Olá, sou um motorista do TokenTravel mobile, gostaria de solicitar o suporte.");
                    }
                })
                .setNegativeButton("Ligar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        entrarEmContatoligacaoM(suportemotorista);
                    }
                });
        builder.show();
    }

    private void entrarEmContatoligacaoM(String suportemotorista){
        Uri uri = Uri.parse("tel:"+suportemotorista);
        Intent intent = new Intent(Intent.ACTION_DIAL,uri);
        startActivity(intent);
    }

    private void entrarEmContatoligacaoP(String suportepassageiro){
        Uri uri = Uri.parse("tel:"+suportepassageiro);
        Intent intent = new Intent(Intent.ACTION_DIAL,uri);
        startActivity(intent);
    }

    private void abrirWhatsApp(String numeroTelefone, String mensagem) {
        try {
            String mensagemCodificada = Uri.encode(mensagem);
            String url = "https://api.whatsapp.com/send?phone=" + numeroTelefone + "&text=" + mensagemCodificada;

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Temporariamente indisponível, tente mais tarde!", Toast.LENGTH_SHORT).show();
        }
    }
}
