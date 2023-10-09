package com.android.tokentravel.objetos;
import java.util.List;

import java.util.List;

public class Rotas extends DiasSemanas {
    Integer rota_id;
    int numero_rota;
    static int numeroRotaAtual = 1;
    String origem;
    String destino;
    String tipo;
    float valor;
    String horario;
    Integer id_motorista;
    String codigo_motorista; // Adicione o campo para armazenar o código do motorista
    List<String> diasAtivos; // Adicione um campo para armazenar os nomes dos dias ativos

    // Construtor que recebe os dias da semana como parâmetros individuais
    public Rotas(String origem, String destino, String tipo, float valor, String horario, Integer id_motorista, String codigo_motorista, boolean domingo, boolean segunda, boolean terca, boolean quarta, boolean quinta, boolean sexta, boolean sabado) {
        super(domingo, segunda, terca, quarta, quinta, sexta, sabado); // Chame o construtor da superclasse
        this.numero_rota = numeroRotaAtual; // Define o número da rota com o número atual
        numeroRotaAtual++;
        this.origem = origem;
        this.destino = destino;
        this.tipo = tipo;
        this.valor = valor;
        this.horario = horario;
        this.id_motorista = id_motorista;
        this.codigo_motorista = codigo_motorista; // Inclua o código do motorista

        // Defina as variáveis booleanas com base nos nomes dos dias ativos
    }

    // Construtor que recebe a lista de nomes dos dias ativos
    public Rotas(int numero, String origem, String destino, String tipo, float valor, String horario, Integer id_motorista, String codigo_motorista, List<String> diasAtivos) {
        super( // Chame o construtor da superclasse com base na lista de nomes de dias ativos
                diasAtivos.contains("domingo"),
                diasAtivos.contains("segunda"),
                diasAtivos.contains("terca"),
                diasAtivos.contains("quarta"),
                diasAtivos.contains("quinta"),
                diasAtivos.contains("sexta"),
                diasAtivos.contains("sabado")
        );
        this.numero_rota = numero;
        this.origem = origem;
        this.destino = destino;
        this.tipo = tipo;
        this.valor = valor;
        this.horario = horario;
        this.id_motorista = id_motorista;
        this.codigo_motorista = codigo_motorista; // Inclua o código do motorista
        this.diasAtivos = diasAtivos; // Inicialize o campo de lista de dias ativos
    }

    public Integer getRota_id() {
        return rota_id;
    }

    public void setRota_id(Integer rota_id) {
        this.rota_id = rota_id;
    }

    public int getNumero_rota() {
        return numero_rota;
    }

    public void setNumero_rota(int numero_rota) {
        this.numero_rota = numero_rota;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public Integer getId_motorista() {
        return id_motorista;
    }

    public void setId_motorista(Integer id_motorista) {
        this.id_motorista = id_motorista;
    }

    public String getCodigo_motorista() {
        return codigo_motorista;
    }

    public void setCodigo_motorista(String codigo_motorista) {
        this.codigo_motorista = codigo_motorista;
    }

    public List<String> getDiasAtivos() {
        return diasAtivos;
    }

    public void setDiasAtivos(List<String> diasAtivos) {
        this.diasAtivos = diasAtivos;
    }
}
