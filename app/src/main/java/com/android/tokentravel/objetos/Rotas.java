package com.android.tokentravel.objetos;
import java.util.List;

public class Rotas {
    Integer rota_id;
    String origem;
    String destino;
    String tipo;
    float valor;
    String horario;
    Integer id_motorista;
    boolean domingo, segunda, terca, quarta, quinta, sexta, sabado;
    List<String> diasAtivos; // Adicione um campo para armazenar os nomes dos dias ativos

    public Rotas(String origem, String destino, String tipo, float valor, String horario, Integer id_motorista, boolean domingo, boolean segunda, boolean terca, boolean quarta, boolean quinta, boolean sexta, boolean sabado) {
        this.origem = origem;
        this.destino = destino;
        this.tipo = tipo;
        this.valor = valor;
        this.horario = horario;
        this.id_motorista = id_motorista;
        this.domingo = domingo;
        this.segunda = segunda;
        this.terca = terca;
        this.quarta = quarta;
        this.quinta = quinta;
        this.sexta = sexta;
        this.sabado = sabado;
    }

    // Construtor alternativo que aceita a lista de nomes dos dias ativos
    public Rotas(String origem, String destino, String tipo, float valor, String horario, Integer id_motorista, List<String> diasAtivos) {
        this.origem = origem;
        this.destino = destino;
        this.tipo = tipo;
        this.valor = valor;
        this.horario = horario;
        this.id_motorista = id_motorista;
        this.diasAtivos = diasAtivos; // Inicialize o campo de lista de dias ativos
        // Defina as variáveis booleanas com base nos nomes dos dias ativos
        this.domingo = diasAtivos.contains("domingo");
        this.segunda = diasAtivos.contains("segunda");
        this.terca = diasAtivos.contains("terca");
        this.quarta = diasAtivos.contains("quarta");
        this.quinta = diasAtivos.contains("quinta");
        this.sexta = diasAtivos.contains("sexta");
        this.sabado = diasAtivos.contains("sabado");
    }

    public Integer getRota_id() {
        return rota_id;
    }

    public void setRota_id(Integer rota_id) {
        this.rota_id = rota_id;
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

    public boolean isDomingo() {
        return domingo;
    }

    public void setDomingo(boolean domingo) {
        this.domingo = domingo;
    }

    public boolean isSegunda() {
        return segunda;
    }

    public void setSegunda(boolean segunda) {
        this.segunda = segunda;
    }

    public boolean isTerca() {
        return terca;
    }

    public void setTerca(boolean terca) {
        this.terca = terca;
    }

    public boolean isQuarta() {
        return quarta;
    }

    public void setQuarta(boolean quarta) {
        this.quarta = quarta;
    }

    public boolean isQuinta() {
        return quinta;
    }

    public void setQuinta(boolean quinta) {
        this.quinta = quinta;
    }

    public boolean isSexta() {
        return sexta;
    }

    public void setSexta(boolean sexta) {
        this.sexta = sexta;
    }

    public boolean isSabado() {
        return sabado;
    }

    public void setSabado(boolean sabado) {
        this.sabado = sabado;
    }
}
