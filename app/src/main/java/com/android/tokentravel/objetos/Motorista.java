package com.android.tokentravel.objetos;

import java.util.UUID;

public class Motorista extends Pessoa {
    Integer id_pessoas;
    String cnh;
    String modelo_carro;
    String placa_veiculo;
    UUID codigoMotorista; // Adicione o campo UUID


    public Motorista(String pessoa_nome, String pessoa_cpf, String pessoa_email, String pessoa_senha, String pessoa_telefone, String pessoa_tipo, String cnh, String modelo_carro, String placa_veiculo, byte[] foto) {
        super(pessoa_nome, pessoa_cpf, pessoa_email, pessoa_senha, pessoa_telefone, pessoa_tipo, String.valueOf(foto));
        this.cnh = cnh;
        this.modelo_carro = modelo_carro;
        this.placa_veiculo = placa_veiculo;
        this.codigoMotorista = UUID.randomUUID(); // Gere um UUID único para o passageiro

    }

    public String getCnh() {
        return cnh;
    }

    public void setCnh(String cnh) {
        this.cnh = cnh;
    }

    public String getModelo_carro() {
        return modelo_carro;
    }

    public void setModelo_carro(String modelo_carro) {
        this.modelo_carro = modelo_carro;
    }

    public String getPlaca_veiculo() {
        return placa_veiculo;
    }

    public void setPlaca_veiculo(String placa_veiculo) {
        this.placa_veiculo = placa_veiculo;
    }

    public Integer getId_pessoas() {
        return id_pessoas;
    }
}