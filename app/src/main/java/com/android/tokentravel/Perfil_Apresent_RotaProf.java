package com.android.tokentravel;

import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.app.AlertDialog;

import com.android.tokentravel.dao.Dao;
import com.android.tokentravel.objetos.Motorista;
import com.android.tokentravel.objetos.Rotas;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Perfil_Apresent_RotaProf extends AppCompatActivity {

    private ImageView imageView;
    private TextView nomeTextView, emailTextView, origemTextView, destinoTextView,
            horarioTextView, diasTextView, tipoVeiculoTextView, valorTextView;
    private AppCompatButton retornarInicioButton;
    private ImageView contactar_motora;

    // Mapeamento dos nomes dos dias da semana
    private static final Map<String, String> diaSemanaMapping = new HashMap<String, String>() {{
        put("domingo", "Domingo");
        put("segunda", "Segunda");
        put("terca", "Terça");
        put("quarta", "Quarta");
        put("quinta", "Quinta");
        put("sexta", "Sexta");
        put("sabado", "Sábado");
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_apresent_rota_prof);

        imageView = findViewById(R.id.imageMotorista);
        nomeTextView = findViewById(R.id.nomeMotorista);
        emailTextView = findViewById(R.id.emailMotorista);
        origemTextView = findViewById(R.id.id_origem);
        destinoTextView = findViewById(R.id.id_destino);
        horarioTextView = findViewById(R.id.id_Horario);
        diasTextView = findViewById(R.id.id_Dia);
        tipoVeiculoTextView = findViewById(R.id.id_TipoVeiculo);
        valorTextView = findViewById(R.id.id_Valor);
        retornarInicioButton = findViewById(R.id.retornar_inicio);
        contactar_motora = findViewById(R.id.motora_contato);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String nomeDoPassageiro = sharedPreferences.getString("nomeDoUsuarioLogado", "");
        // Dividir o nome completo em palavras
        String[] nomePartes = nomeDoPassageiro.split(" ");
        // Pega a primeira palavra
        String primeiroNome = nomePartes[0];

        // Configurar o OnClickListener para o botão de retorno à atividade inicial
        retornarInicioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crie um Intent para iniciar a atividade de navegação inicial (NavigationView)
                Intent intent = new Intent(Perfil_Apresent_RotaProf.this, Navigation_View.class);

                // Inicie a atividade
                startActivity(intent);
            }
        });

        // Configurar o OnClickListener para o ImageView de contato com o motorista
        contactar_motora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtenha o motoristaId, numeroRota e código único do motorista da Intent
                Intent intent = getIntent();
                if (intent != null) {
                    int motoristaId = intent.getIntExtra("id_motoristaagora", -1);
                    int numeroRota = intent.getIntExtra("numeroRota", -1);
                    int idRota = intent.getIntExtra("idRota", -1);
                    String codigoUnicoMotorista = intent.getStringExtra("codigoUnicoMotorista"); // Obtenha o código único do motorista

                    if (motoristaId != -1 && numeroRota != -1 && idRota != -1 && !TextUtils.isEmpty(codigoUnicoMotorista)) {
                        // Crie uma instância do seu Dao
                        Dao dao = new Dao(Perfil_Apresent_RotaProf.this);

                        // Obtenha o número de telefone do motorista
                        Motorista motorista = dao.buscaMotoristaPorCodigoUnico(codigoUnicoMotorista);
                        String numeroTelefoneMotorista = motorista.getPessoa_telefone(); // Substitua pelo método correto para obter o número de telefone do motorista

                        // Obtenha as informações da rota
                        Rotas rota = dao.buscaRotaPorId(motoristaId, numeroRota, idRota);

                        // Crie a mensagem
                        String mensagem = "Olá, me chamo " + primeiroNome + ", sou um passageiro do TokenTravel e gostaria de entrar em contato.\n";
                        mensagem += "*Nome do Motorista: " + motorista.getPessoa_nome() + "*\n";
                        mensagem += "*Origem: " + rota.getOrigem() + "*\n";
                        mensagem += "*Destino: " + rota.getDestino() + "*\n";
                        mensagem += "*Horário: " + rota.getHorario() + "*\n";
                        mensagem += "*Dia(s): " + mapearDiasSemana(rota.getDiasAtivos()) + "*\n";
                        mensagem += "*Tipo: " + rota.getTipo() + "*\n";
                        mensagem += "*R$: " + rota.getValor() + "*\n";
                        mensagem += "_Essa mensagem foi gerada automaticamente pelo aplicativo TokenTravel!_\n";

                        // Crie um AlertDialog para perguntar ao usuário a opção desejada
                        AlertDialog.Builder builder = new AlertDialog.Builder(Perfil_Apresent_RotaProf.this);
                        builder.setTitle("Escolha uma opção");
                        builder.setMessage("Deseja entrar em contato pelo WhatsApp ou fazer uma ligação?");

                        // Botão para entrar em contato pelo WhatsApp
                        String finalMensagem = mensagem;
                        builder.setPositiveButton("WhatsApp", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Coloque o código para abrir o WhatsApp aqui
                                // Você pode usar a função abrirWhatsApp que você já definiu
                                abrirWhatsApp(numeroTelefoneMotorista, finalMensagem);
                            }
                        });

                        // Botão para fazer ligação
                        builder.setNegativeButton("Ligar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Substitua "SEU_NUMERO_DE_TELEFONE" pelo número do motorista que você deseja ligar
                                String numeroMotorista = numeroTelefoneMotorista;

                                entrarEmContatoligacaoM(numeroMotorista);
                            }
                        });

                        // Exibir o diálogo
                        builder.show();
                    }
                }
            }
        });

        // Obtenha o motoristaId, numeroRota e código único do motorista da Intent
        Intent intent = getIntent();
        if (intent != null) {
            int motoristaId = intent.getIntExtra("id_motoristaagora", -1);
            int numeroRota = intent.getIntExtra("numeroRota", -1);
            int idRota = intent.getIntExtra("idRota", -1);
            String codigoUnicoMotorista = intent.getStringExtra("codigoUnicoMotorista"); // Obtenha o código único do motorista

            Log.d("Adapter_motoristas", "ID do motorista: " + motoristaId);
            Log.d("Adapter_motoristas", "ID da rota: " + numeroRota);
            Log.d("Adapter_motoristas", "Código único do motorista: " + codigoUnicoMotorista);

            if (motoristaId != -1 && numeroRota != -1 && idRota != -1 && !TextUtils.isEmpty(codigoUnicoMotorista)) {
                // Crie uma instância do seu Dao
                Dao dao = new Dao(this);

                // Chame o método buscaMotoristaPorCodigoUnico para obter os dados do motorista usando o código único
                Motorista motorista = dao.buscaMotoristaPorCodigoUnico(codigoUnicoMotorista);

                if (motorista != null) {
                    // Crie uma instância de Dao para buscar a rota por númeroRota e idRota
                    Rotas rota = dao.buscaRotaPorId(motoristaId, numeroRota, idRota);

                    // Defina os valores nos TextViews
                    nomeTextView.setText(motorista.getPessoa_nome());
                    emailTextView.setText(motorista.getPessoa_email());
                    origemTextView.setText("Origem: " + rota.getOrigem());
                    destinoTextView.setText("Destino: " + rota.getDestino());
                    horarioTextView.setText("Horário: " + rota.getHorario());
                    diasTextView.setText("Dia(s): " + mapearDiasSemana(rota.getDiasAtivos()));
                    tipoVeiculoTextView.setText("Tipo: " + rota.getTipo());
                    valorTextView.setText("R$: " + rota.getValor());

                    // Chame o método obterFotoPorCodigoMotorista para obter a foto do motorista
                    String fotoMotorista = dao.obterFotoPorCodigoMotorista(codigoUnicoMotorista);

                    if (fotoMotorista != null) {
                        // Decodifique a string Base64 em um array de bytes
                        byte[] decodedBytes = android.util.Base64.decode(fotoMotorista, android.util.Base64.DEFAULT);

                        // Crie um objeto Bitmap a partir do array de bytes decodificado
                        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

                        // Use o Glide para carregar a imagem decodificada no ImageView
                        Glide.with(this)
                                .load(bitmap)
                                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                                .into(imageView);
                    }
                }
            }
        }
    }

    private void entrarEmContatoligacaoM(String numeroMotorista) {
        Uri uri = Uri.parse("tel:" + numeroMotorista);
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
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
            Toast.makeText(this, "Temporariamente indisponível, tente mais tarde!", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para mapear os nomes dos dias da semana
    private String mapearDiasSemana(List<String> diasAtivos) {
        StringBuilder diasStringBuilder = new StringBuilder();
        for (String dia : diasAtivos) {
            String diaFormatado = diaSemanaMapping.get(dia.toLowerCase());
            if (diaFormatado != null) {
                diasStringBuilder.append(diaFormatado).append(", ");
            }
        }
        if (diasStringBuilder.length() > 2) {
            // Remova a vírgula e o espaço extras no final
            diasStringBuilder.setLength(diasStringBuilder.length() - 2);
        }
        return diasStringBuilder.toString();
    }
}
